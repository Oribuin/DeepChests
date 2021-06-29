package xyz.oribuin.upgradeablechests.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import xyz.oribuin.orilibrary.database.DatabaseConnector;
import xyz.oribuin.orilibrary.database.MySQLConnector;
import xyz.oribuin.orilibrary.database.SQLiteConnector;
import xyz.oribuin.orilibrary.manager.Manager;
import xyz.oribuin.orilibrary.util.FileUtils;
import xyz.oribuin.upgradeablechests.UpgradeableChests;
import xyz.oribuin.upgradeablechests.migration._1_CreateTables;
import xyz.oribuin.upgradeablechests.obj.Chest;
import xyz.oribuin.upgradeablechests.util.PluginUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DataManager extends Manager {

    private final UpgradeableChests plugin = (UpgradeableChests) this.getPlugin();
    private DatabaseConnector connector = null;

    private final Map<Integer, Chest> cachedChests = new HashMap<>();
    private String tablePrefix;

    public DataManager(final UpgradeableChests plugin) {
        super(plugin);
    }

    @Override
    public void enable() {

        final FileConfiguration config = this.plugin.getConfig();

        this.tablePrefix = config.getString("mysql.table-prefix") != null ? config.getString("mysql.table-prefix") : "upgradeablechests_";
        if (config.getBoolean("mysql.enabled")) {
            final String hostname = config.getString("mysql.host");
            final int port = config.getInt("mysql.port");
            final String dbname = config.getString("mysql.dbname");
            final String username = config.getString("mysql.username");
            final String password = config.getString("mysql.password");
            final boolean ssl = config.getBoolean("mysql.ssl");

            // Connect to MySQL
            this.connector = new MySQLConnector(this.plugin, hostname, port, dbname, username, password, ssl);
            this.plugin.getLogger().info("Using MySQL For Database Saving!");

        } else {
            FileUtils.createFile(this.plugin, "database.db");

            // Connect to SQLite
            this.connector = new SQLiteConnector(this.plugin, "database.db");
            this.plugin.getLogger().info("Using SQLite for Database Saving!");
        }

        // Disable plugin if connector is still null
        if (connector == null) {
            this.plugin.getLogger().severe("Unable to connect to MySQL or SQLite, Disabling plugin...");
            this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
            return;
        }


        this.async(task -> this.connector.connect(connection -> new _1_CreateTables(tablePrefix).migrate(this.connector, connection)));
    }

    /**
     * Cache all the plugin's upgradeable chests
     */
    public void cacheChests() {

        // this also feels like a mess
        final List<Chest> chests = new ArrayList<>();

        CompletableFuture.runAsync(() -> this.connector.connect(connection -> {
            final String query = "SELECT * FROM " + tablePrefix + "chests";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                final ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    final int id = resultSet.getInt("chestID");
                    final int tier = resultSet.getInt("tier");
                    final Location loc = new Location(Bukkit.getWorld(resultSet.getString("world")),
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z"));

                    final Chest chest = new Chest(id, tier, loc);
                    final String newQuery = "SELECT item FROM " + tablePrefix + "items WHERE chestID = ?";

                    // like holy fuck what is going on here, two??? while loops, thats asking for the server to have a mental breakdown
                    final List<ItemStack> items = new ArrayList<>();
                    CompletableFuture.runAsync(() -> {
                        try (PreparedStatement newStatement = connection.prepareStatement(newQuery)) {
                            final ResultSet newResult = newStatement.executeQuery();
                            while (resultSet.next()) {
                                items.add(PluginUtils.handleDeserialization(newResult.getString(1)));
                            }

                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                    }).thenRun(() -> {
                        chest.getItems().addAll(items);
                        chests.add(chest);
                    });

                }

            }

        })).thenRunAsync(() -> chests.forEach(chest -> this.cachedChests.put(chest.getId(), chest)));

    }


    /**
     * Save the chest into the database and cache it..
     *
     * @param chest The Chest
     */
    public void saveChest(Chest chest) {
        this.cachedChests.put(chest.getId(), chest);

        this.async(task -> this.connector.connect(connection -> {

            // Delete all chest's current items.
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.tablePrefix + "items" + "WHERE chestID = ?")) {
                statement.setInt(1, chest.getId());
                statement.execute();
            }

            // Save the chest in the chests table
            final String addChest = "INSERT INTO " + this.tablePrefix + "chests (tier, x, y, z, world) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(addChest)) {
                statement.setInt(1, chest.getTier());
                statement.setDouble(2, chest.getLocation().getX());
                statement.setDouble(3, chest.getLocation().getY());
                statement.setDouble(4, chest.getLocation().getZ());
                statement.setString(5, chest.getLocation().getWorld().getName());
                statement.execute();
            }

            // this feels like a mess :)
            try (Statement statement = connection.createStatement()) {
                chest.getItems().stream()
                        .map(PluginUtils::handleSerialization)
                        .forEach(s -> {

                            try {
                                statement.addBatch("INSERT INTO " + tablePrefix + "items(chestID, item) VALUES(" + chest.getId() + ", \"" + s + "\"");
                            } catch (SQLException exception) {
                                exception.printStackTrace();
                            }

                        });
                statement.executeBatch();

            }

        }));
    }

    @Override
    public void disable() {

        if (this.connector != null) {
            this.connector.closeConnection();
        }

    }

    public void async(Consumer<BukkitTask> callback) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, callback);
    }


    public String getTablePrefix() {
        return tablePrefix;
    }
}
