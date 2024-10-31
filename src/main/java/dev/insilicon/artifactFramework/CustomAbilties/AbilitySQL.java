package dev.insilicon.artifactFramework.CustomAbilties;

import dev.insilicon.artifactFramework.ArtifactFramework;
import dev.insilicon.artifactFramework.BaseInternal.InternalLibs.SQLInstance;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;

public class AbilitySQL {

    private final ArtifactFramework plugin;
    private Connection connection;
    private final String dbName = "abilities.db";

    public AbilitySQL(ArtifactFramework plugin) {
        this.plugin = plugin;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            // Ensure plugin data folder exists
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            // Create database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" +
                    new File(plugin.getDataFolder(), dbName).getAbsolutePath());

            // Create abilities table
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS player_abilities (" +
                        "uuid VARCHAR(36) NOT NULL," +
                        "ability_name VARCHAR(255) NOT NULL," +
                        "ability_data TEXT," +  // No limit on ability_data
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "PRIMARY KEY (uuid)" +
                        ")");
            }
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getAbility(Player player) {
        if (player == null) return null;

        String query = "SELECT ability_name FROM player_abilities WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, player.getUniqueId().toString());

            ResultSet results = pstmt.executeQuery();
            if (results.next()) {
                return results.getString("ability_name");
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to get ability for player " +
                    player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public String getAbilityData(Player player) {
        if (player == null) return null;

        String query = "SELECT ability_data FROM player_abilities WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, player.getUniqueId().toString());

            ResultSet results = pstmt.executeQuery();
            if (results.next()) {
                return results.getString("ability_data");
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to get ability data for player " +
                    player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean setAbility(Player player, String abilityName, String abilityData) {
        if (player == null || abilityName == null) return false;

        String upsertQuery = "INSERT OR REPLACE INTO player_abilities (uuid, ability_name, ability_data, updated_at) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try (PreparedStatement pstmt = connection.prepareStatement(upsertQuery)) {
            pstmt.setString(1, player.getUniqueId().toString());
            pstmt.setString(2, abilityName);
            pstmt.setString(3, abilityData);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to set ability for player " +
                    player.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Additional utility methods

    public boolean removeAbility(Player player) {
        if (player == null) return false;

        String query = "DELETE FROM player_abilities WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, player.getUniqueId().toString());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to remove ability for player " +
                    player.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createEmptyPlayerEntry(Player player) {
        //set the ability data and name to null
        setAbility(player, null, null);
    }
}