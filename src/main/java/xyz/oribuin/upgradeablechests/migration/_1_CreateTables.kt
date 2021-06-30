package xyz.oribuin.upgradeablechests.migration

import xyz.oribuin.orilibrary.database.DatabaseConnector
import xyz.oribuin.orilibrary.database.MySQLConnector
import java.sql.Connection
import java.sql.SQLException

class _1_CreateTables(private val prefix: String) : DataMigration(1) {

    @Throws(SQLException::class)
    override fun migrate(connector: DatabaseConnector?, connection: Connection?) {

        if (connector == null || connection == null) {
            return
        }

        val autoIncrement = if (connection is MySQLConnector) "AUTO_INCREMENT" else ""

        val createFirstTable = "CREATE TABLE IF NOT EXISTS " + prefix + "chests (" +
                "chestID INT" + autoIncrement + " NOT NULL, " +
                "tier INT NOT NULL." +
                "x DOUBLE NOT NULL, " +
                "y DOUBLE NOT NULL, " +
                "z DOUBLE NOT NULL, " +
                "world VARCHAR(100) NOT NULL, " +
                "PRIMARY KEY(chestID))"

        val createItemsTable = "CREATE TABLE IF NOT EXISTS " + prefix + "items (" +
                "chestID INT NOT NULL, item LONGTEXT NOT NULL)"

        connection.createStatement().use { statement -> statement?.execute(createFirstTable) }
        connection.createStatement().use { statement -> statement?.execute(createItemsTable) }
    }
}