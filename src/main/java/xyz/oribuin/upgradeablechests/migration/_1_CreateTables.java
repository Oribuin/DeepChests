package xyz.oribuin.upgradeablechests.migration;

import xyz.oribuin.orilibrary.database.DatabaseConnector;
import xyz.oribuin.orilibrary.database.MySQLConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _1_CreateTables extends DataMigration {

    private final String prefix;

    public _1_CreateTables(final String prefix) {
        super(1);
        this.prefix = prefix;
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection) throws SQLException {

        final String autoIncrement = connection instanceof MySQLConnector ? "AUTO_INCREMENT" : "";

        final String createFirstTable = "CREATE TABLE IF NOT EXISTS " + prefix + "chests (" +
                "chestID INT" + autoIncrement + ", " +
                "tier INT, " +
                "x DOUBLE, " +
                "y DOUBLE, " +
                "z DOUBLE, " +
                "world DOUBLE, " +
                "PRIMARY KEY(chestID))";

        final String createItemsTable = "CREATE TABLE IF NOT EXISTS " + prefix + "items (" +
                "chestID INT, item LONGTEXT)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createFirstTable);
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute(createItemsTable);
        }

    }

}
