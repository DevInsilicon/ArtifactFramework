package dev.insilicon.artifactFramework.CustomBlocks;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;

import dev.insilicon.artifactFramework.BaseInternal.CustomClasses.BlockDataType;

public class BlockSQL {

    private Connection connection;
    private Gson gson = new Gson();

    public void initialize(JavaPlugin plugin) {
        File dataFolder = new File(plugin.getDataFolder(), "database");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File databaseFile = new File(dataFolder, "blocks.db");
        try {
            if (!databaseFile.exists()) {
                databaseFile.createNewFile();
            }
            String url = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            String createTableQuery = "CREATE TABLE IF NOT EXISTS blocks (" +
                                      "id TEXT PRIMARY KEY, " +
                                      "x REAL, " +
                                      "y REAL, " +
                                      "z REAL, " +
                                      "blockData TEXT, " +
                                      "dataType TEXT)";
            connection.createStatement().execute(createTableQuery);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void addBlock(String id, double x, double y, double z, BlockDataType blockData) {
        String insertQuery = "INSERT INTO blocks (id, x, y, z, blockData, dataType) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, id);
            pstmt.setDouble(2, x);
            pstmt.setDouble(3, y);
            pstmt.setDouble(4, z);
            pstmt.setString(5, gson.toJson(blockData));
            pstmt.setString(6, blockData.getClass().getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Block> getAllBlocks() {
        List<Block> blocks = new ArrayList<>();
        String selectQuery = "SELECT * FROM blocks";
        try (PreparedStatement pstmt = connection.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                String blockDataJson = rs.getString("blockData");
                Object blockData = gson.fromJson(blockDataJson, Object.class);
                blocks.add(new Block(id, x, y, z, blockData));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blocks;
    }

    public Block getBlockFromLocation(double x, double y, double z) {
        String selectQuery = "SELECT * FROM blocks WHERE x = ? AND y = ? AND z = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
            pstmt.setDouble(1, x);
            pstmt.setDouble(2, y);
            pstmt.setDouble(3, z);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String blockDataJson = rs.getString("blockData");
                    String dataType = rs.getString("dataType");

                    @SuppressWarnings("unchecked")
                    Class<? extends BlockDataType> typeClass =
                            (Class<? extends BlockDataType>) Class.forName(dataType);
                    BlockDataType blockData = gson.fromJson(blockDataJson, typeClass);

                    return new Block(id, x, y, z, blockData);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Block {
        private String id;
        private double x, y, z;
        private Object blockData;

        public Block(String id, double x, double y, double z, Object blockData) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.z = z;
            this.blockData = blockData;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public Object getBlockData() {
            return blockData;
        }

        public void setBlockData(Object blockData) {
            this.blockData = blockData;
        }
    }
}
