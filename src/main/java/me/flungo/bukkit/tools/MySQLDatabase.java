package me.flungo.bukkit.tools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Fabrizio
 */
public abstract class MySQLDatabase {

	private final Plugin plugin;
	private final String username;
	private final String password;
	private final String host;
	private final String port;
	private final String database;

	private Connection conn = null;

	public MySQLDatabase(Plugin plugin) {
		this.plugin = plugin;
		ConfigurationSection dbCS = plugin.getConfig().getConfigurationSection("database");
		this.username = dbCS.getString("username");
		this.password = dbCS.getString("password");
		this.host = dbCS.getString("host");
		this.port = dbCS.getString("port");
		this.database = dbCS.getString("database");
	}

	public Connection getConnection() throws SQLException {
		if (conn == null) {
			conn = initialize();
		}
		if (!conn.isValid(10)) {
			conn = initialize();
		}
		return conn;
	}

	private Connection initialize() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			conn.setAutoCommit(false);

			createTable();
		} catch (ClassNotFoundException ex) {
			throw new SQLException("You need the MySQL library!", ex);
		} catch (SQLException ex) {
			throw new SQLException("SQL exception on initialize", ex);
		}

		return conn;
	}

	protected abstract void createTable();

	public boolean tableExists(String name) {
		ResultSet rs = null;
		try {
			Connection conn = getConnection();

			DatabaseMetaData dbm = conn.getMetaData();
			rs = dbm.getTables(null, null, name, null);
			return rs.next();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Table Check Exception", ex);
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, "Table Check SQL Exception (on closing)", ex);
			}
		}
	}

}
