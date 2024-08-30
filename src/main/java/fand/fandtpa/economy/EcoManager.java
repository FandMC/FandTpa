package fand.fandtpa.economy;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EcoManager {

    private final String dbUrl;

    public EcoManager(String dbPath) {
        this.dbUrl = "jdbc:sqlite:" + dbPath;
        initializeDatabase();
    }

    // 初始化数据库，创建玩家余额表
    private void initializeDatabase() {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS player_balance (" +
                             "uuid TEXT PRIMARY KEY," +
                             "balance TEXT NOT NULL" +
                             ")"
             )) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取玩家的余额，并提供一个默认值
    public BigDecimal getBalance(UUID playerUUID) {
        return getBalance(playerUUID, BigDecimal.ZERO);
    }

    // 获取玩家的余额，如果不存在，则返回指定的默认值
    public BigDecimal getBalance(UUID playerUUID, BigDecimal defaultValue) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT balance FROM player_balance WHERE uuid = ?"
             )) {
            stmt.setString(1, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BigDecimal(rs.getString("balance"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    // 设置玩家的余额
    public void setBalance(UUID playerUUID, BigDecimal amount) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO player_balance (uuid, balance) VALUES (?, ?)"
             )) {
            stmt.setString(1, playerUUID.toString());
            stmt.setString(2, amount.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 增加玩家的余额
    public void addBalance(UUID playerUUID, BigDecimal amount) {
        BigDecimal newBalance = getBalance(playerUUID).add(amount);
        setBalance(playerUUID, newBalance);
    }

    // 获取数据库连接，并添加简单的重试机制
    private Connection getConnection() throws SQLException {
        int retryCount = 0;
        while (retryCount < 3) {
            try {
                return DriverManager.getConnection(dbUrl);
            } catch (SQLException e) {
                retryCount++;
                if (retryCount == 3) {
                    throw e;  // 如果重试次数用尽，抛出异常
                }
                try {
                    Thread.sleep(100);  // 等待100毫秒再重试
                } catch (InterruptedException ignored) {
                }
            }
        }
        throw new SQLException("无法建立数据库连接");
    }
    public String getBalanceAsString(UUID playerUUID) {
        return getBalance(playerUUID).toPlainString();
    }
}
