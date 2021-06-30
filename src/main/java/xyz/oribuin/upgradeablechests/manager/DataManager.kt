package xyz.oribuin.upgradeablechests.manager

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.scheduler.BukkitTask
import xyz.oribuin.orilibrary.database.DatabaseConnector
import xyz.oribuin.orilibrary.database.MySQLConnector
import xyz.oribuin.orilibrary.database.SQLiteConnector
import xyz.oribuin.orilibrary.manager.Manager
import xyz.oribuin.orilibrary.util.FileUtils
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.migration._1_CreateTables
import xyz.oribuin.upgradeablechests.obj.Chest
import xyz.oribuin.upgradeablechests.obj.Tier
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer


class DataManager(private val plugin: UpgradeableChests) : Manager(plugin) {

    private var connector: DatabaseConnector? = null
    val cachedChests = mutableMapOf<Int, Chest>()

    private lateinit var tablePrefix: String

    override fun enable() {
        val config = this.plugin.config
        this.tablePrefix = config.getString("mysql.table-prefix") ?: "upgradeablechests_"

        if (config.getBoolean("mysql.enabled")) {
            val hostname = config.getString("mysql.host")!!
            val port = config.getInt("mysql.port")
            val dbname = config.getString("mysql.dbname")!!
            val username = config.getString("mysql.username")!!
            val password = config.getString("mysql.password")!!
            val ssl = config.getBoolean("mysql.ssl")

            // Connect to MySQL
            connector = MySQLConnector(this.plugin, hostname, port, dbname, username, password, ssl)
            this.plugin.logger.info("Using MySQL For Database Saving!")
        } else {
            FileUtils.createFile(this.plugin, "database.db")

            // Connect to SQLite
            connector = SQLiteConnector(this.plugin, "database.db")
            this.plugin.logger.info("Using SQLite for Database Saving!")
        }

        // Disable plugin if connector is still null
        if (connector == null) {
            this.plugin.logger.severe("Unable to connect to MySQL or SQLite, Disabling plugin...")
            this.plugin.server.pluginManager.disablePlugin(this.plugin)
            return
        }

        async {
            connector?.connect { connection ->
                _1_CreateTables(tablePrefix).migrate(connector, connection)
            }
            cacheChests()
        }

    }

    /**
     * Cache all the plugin's upgradeable chests
     * @since 1.0
     */
    private fun cacheChests() {

        // this also feels like a mess
        val chests = mutableListOf<Chest>()

        CompletableFuture.runAsync {
            connector?.connect { connection ->
                val query = "SELECT * FROM ${tablePrefix}chests"

                connection.prepareStatement(query).use { statement ->
                    val resultSet = statement.executeQuery()

                    while (resultSet.next()) {
                        val id = resultSet.getInt("chestID")
                        val tier = resultSet.getInt("tier")
                        val loc = Location(
                            Bukkit.getWorld(resultSet.getString("world")),
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z")
                        )

                        val chest = Chest(id, plugin.getManager(TierManager::class.java).getTier(tier), loc)
                        chests.add(chest)
                    }
                }
            }

        }.thenRunAsync {
            chests.forEach { cachedChests[it.id] = it }
        }

    }

    /**
     * Create a new [Chest] object and save it into the database.
     * @since 1.0
     *
     * @param tier The tier of the [Chest]
     * @param location The location of the [Chest]
     * @return a nullable [Chest]
     */
    fun createChest(tier: Tier, location: Location): Chest {

        val nextId = getNextChestID(cachedChests.map { chest -> chest.value.id }.toList())
        val chest = Chest(nextId, tier, location)
        this.cachedChests[chest.id] = chest

        async { _ ->

            connector?.connect { connection ->
                val query = "REPLACE INTO " + tablePrefix + "chests (chestID, tier, x, y, z, world) VALUES (?, ?, ?, ?, ?, ?)"

                connection.prepareStatement(query).use {
                    it.setInt(1, chest.id)
                    it.setInt(2, tier.id)
                    it.setDouble(3, location.x)
                    it.setDouble(4, location.y)
                    it.setDouble(5, location.z)
                    it.setString(6, location.world?.name)
                    it.executeUpdate()
                }

            }
        }

        return chest
    }

    /**
     * Get a nullable [Chest] from a [Location].
     * @since 1.0
     *
     * @param loc The [Location]
     */
    fun getChest(loc: Location): Optional<Chest> {
        return this.cachedChests
            .values
            .stream()
            .filter { it.location == loc }
            .findAny()
    }

    /**
     * Delete a chest from the database and removeit from the cache
     * @since 1.0
     *
     * @id The id of the [Chest]
     */
    fun deleteChest(id: Int) {
        cachedChests.remove(id)

        async { _ ->

            this.connector?.connect { connection ->
                connection.prepareStatement("DELETE FROM ${tablePrefix}items WHERE chestID = ?").use {
                    it.setInt(1, id)
                    it.executeUpdate()
                }

                connection.prepareStatement("DELETE FROM ${tablePrefix}chests WHERE chestID = ?").use {
                    it.setInt(1, id)
                    it.executeUpdate()
                }

            }
        }

    }

    /**
     * @author Esophose
     *
     * Gets the smallest positive integer greater than 0 from a list
     *
     * @param existingIds The list containing non-available ids
     * @return The smallest positive integer not in the given list
     */
    private fun getNextChestID(existingIds: Collection<Int>): Int {
        val copy = existingIds.sorted().toMutableList()
        copy.removeIf { it <= 0 }

        var current = 1
        for (i in copy) {
            if (i == current) {
                current++
            } else break
        }

        return current
    }

    override fun disable() {
        if (connector != null) {
            connector!!.closeConnection()
        }
    }

    private fun async(callback: Consumer<BukkitTask>) {
        this.plugin.server.scheduler.runTaskAsynchronously(plugin, callback)
    }
}