package net.moltendorf.Bukkit.LinkedWhitelist.storage;

import net.moltendorf.Bukkit.LinkedWhitelist.LinkedWhitelist;
import net.moltendorf.Bukkit.LinkedWhitelist.Settings;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.*;
import java.util.UUID;

/**
 * Created by moltendorf on 15/07/27.
 *
 * @author moltendorf
 */
public class MySQL extends AbstractStorage {
	String host     = "localhost";
	int    port     = 3306;
	String prefix   = "qc__";
	String database = "database";
	String username = "username";
	String password = "password";

	Connection connection = null;

	public MySQL(final ConfigurationSection storageSection) {
		if (storageSection.isString("host")) {
			host = storageSection.getString("host", host);
		} else {
			set("host", host);
		}

		if (storageSection.isInt("port")) {
			port = storageSection.getInt("port", port);
		} else {
			set("port", port);
		}

		if (storageSection.isString("prefix")) {
			prefix = storageSection.getString("prefix", prefix);
		} else {
			set("prefix", prefix);
		}

		if (storageSection.isString("database")) {
			database = storageSection.getString("database", database);
		} else {
			set("database", database);
		}

		if (storageSection.isString("username")) {
			username = storageSection.getString("username", username);
		} else {
			set("username", username);
		}

		if (storageSection.isString("password")) {
			password = storageSection.getString("password", password);
		} else {
			set("password", password);
		}

		connect();
	}

	private void set(final String path, final Object value) {
		Settings.getInstance().set("storage." + path, value);
	}

	private void connect() {
		connection = null;

		try {
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?dontTrackOpenResources=true&useAffectedRows=true";
			connection = DriverManager.getConnection(url, username, password);
		} catch (final Exception exception) {
			LinkedWhitelist.getInstance().getLogger().warning("Could not connect to MySQL database with provided information.");
			exception.printStackTrace();
		}
	}

	@Override
	public boolean setPermissionForPlayer(final UUID id, final String name, final boolean permission, final boolean update)
		throws StorageException {
		try {
			if (connection == null || !connection.isValid(0)) {
				connect();

				if (connection == null) {
					LinkedWhitelist.getInstance().getLogger().warning("Failed to set permission for player " + name + ".");

					return false;
				}
			}
		} catch (final SQLException exception) {
			LinkedWhitelist.getInstance().getLogger().warning("Failed to set permission for player " + name + ".");

			throw new StorageException();
		}

		final String lookup = id.toString().replace("-", "");

		PreparedStatement statement = null;

		boolean updated = false;

		try {
			String sql = "insert into " + prefix + "permissions " +
				"(id, permission) " +
				"values (UNHEX(?), ?) ";

			if (update) {
				sql += "on duplicate key update " +
					"permission = ?";
			} else {
				sql += "on duplicate key update " +
					"permission = permission";
			}

			statement = connection.prepareStatement(sql);

			int i = 0;

			statement.setString(++i, lookup);
			statement.setBoolean(++i, permission);

			if (update) {
				// Update values.
				statement.setBoolean(++i, permission);
			}

			updated = statement.executeUpdate() > 0;
		} catch (final SQLException exception) {
			LinkedWhitelist.getInstance().getLogger().warning("Failed to set permission for player " + name + ".");
			exception.printStackTrace();

			throw new StorageException();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (final SQLException exception) {
					// Quiet!
				}
			}
		}

		return updated;
	}

	@Override
	public Boolean getPermissionForPlayer(final UUID id, final String name)
		throws StorageException {
		try {
			if (connection == null || !connection.isValid(0)) {
				connect();

				if (connection == null) {
					LinkedWhitelist.getInstance().getLogger().warning("Failed to get permission for player " + id + ".");

					return false;
				}
			}
		} catch (final SQLException exception) {
			LinkedWhitelist.getInstance().getLogger().warning("Failed to get permission for player " + id + ".");

			throw new StorageException();
		}

		final String lookup = id.toString().replace("-", "");

		Boolean permission = null;

		PreparedStatement statement = null;
		ResultSet         result    = null;

		try {
			statement = connection.prepareStatement(
				"select permission " +
					"from " + prefix + "permissions " +
					"where id = UNHEX(?) " +
					"limit 1"
			);

			int i = 0;

			statement.setString(++i, lookup);

			result = statement.executeQuery();

			if (result.next()) {
				i = 0;

				permission = result.getBoolean(++i);
			}
		} catch (final SQLException exception) {
			LinkedWhitelist.getInstance().getLogger().warning("Failed to get permission for player " + id + ".");
			exception.printStackTrace();

			throw new StorageException();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (final SQLException exception) {
					// Quiet!
				}
			}

			if (statement != null) {
				try {
					statement.close();
				} catch (final SQLException exception) {
					// Quiet!
				}
			}
		}

		return permission;
	}
}
