package dev.insilicon.artifactFramework.BaseInternal.InternalLibs;

import dev.insilicon.artifactFramework.ArtifactFramework;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLInstance {
    private final ArtifactFramework plugin;
    private final String SQLName;
    private Connection connection;

    public SQLInstance(ArtifactFramework plugin, String SQLName) {
        this.plugin = plugin;
        this.SQLName = SQLName;
        setupDatabase();
    }

    private void setupDatabase() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        String dbPath = dataFolder.getAbsolutePath() + File.separator + SQLName;
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            Bukkit.getLogger().info("SQLite database connected successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("Could not connect to SQLite database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a table if it doesn't exist
     * @param tableName The name of the table
     * @param columns Map of column names and their SQL types
     */
    public void createTableIfNotExists(String tableName, Map<String, String> columns) {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        boolean first = true;

        for (Map.Entry<String, String> column : columns.entrySet()) {
            if (!first) {
                query.append(", ");
            }
            query.append(column.getKey()).append(" ").append(column.getValue());
            first = false;
        }
        query.append(")");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query.toString());
        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating table " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inserts data into a table
     * @param tableName The name of the table
     * @param data Map of column names and values
     */
    public void insert(String tableName, Map<String, Object> data) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<Object> valuesList = new ArrayList<>();
        boolean first = true;

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                columns.append(", ");
                values.append(", ");
            }
            columns.append(entry.getKey());
            values.append("?");
            valuesList.add(entry.getValue());
            first = false;
        }

        String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 0; i < valuesList.size(); i++) {
                pstmt.setObject(i + 1, valuesList.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error inserting data into " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Performs a fuzzy search on a specific column
     * @param tableName The name of the table
     * @param column The column to search in
     * @param searchTerm The term to search for
     * @return List of matching rows
     */
    public List<Map<String, Object>> getBestMatch(String tableName, String column, String searchTerm) {
        String query = "SELECT * FROM " + tableName + " WHERE " + column + " LIKE ? ORDER BY LENGTH(" + column + ") - LENGTH(?) ASC LIMIT 5";
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, searchTerm);

            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error performing fuzzy search in " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Executes a custom query and returns the results
     * @param query The SQL query to execute
     * @return List of rows matching the query
     */
    public List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Updates existing records in a table
     * @param tableName The name of the table
     * @param data Map of column names and new values
     * @param whereClause The WHERE clause for the update
     * @param whereParams Parameters for the WHERE clause
     */
    public void update(String tableName, Map<String, Object> data, String whereClause, List<Object> whereParams) {
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        List<Object> allParams = new ArrayList<>();
        boolean first = true;

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                query.append(", ");
            }
            query.append(entry.getKey()).append(" = ?");
            allParams.add(entry.getValue());
            first = false;
        }

        query.append(" WHERE ").append(whereClause);
        if (whereParams != null) {
            allParams.addAll(whereParams);
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < allParams.size(); i++) {
                pstmt.setObject(i + 1, allParams.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error updating data in " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deletes records from a table
     * @param tableName The name of the table
     * @param whereClause The WHERE clause for deletion
     * @param whereParams Parameters for the WHERE clause
     */
    public void delete(String tableName, String whereClause, List<Object> whereParams) {
        String query = "DELETE FROM " + tableName + " WHERE " + whereClause;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            if (whereParams != null) {
                for (int i = 0; i < whereParams.size(); i++) {
                    pstmt.setObject(i + 1, whereParams.get(i));
                }
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error deleting from " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets the underlying SQL connection
     * @return The SQL connection
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                setupDatabase();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error checking connection: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}