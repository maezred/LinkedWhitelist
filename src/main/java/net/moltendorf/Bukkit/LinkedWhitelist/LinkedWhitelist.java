package net.moltendorf.Bukkit.LinkedWhitelist;

import net.moltendorf.Bukkit.LinkedWhitelist.storage.AbstractStorage;
import net.moltendorf.Bukkit.LinkedWhitelist.storage.StorageException;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by moltendorf on 15/06/29.
 *
 * @author moltendorf
 */
public class LinkedWhitelist extends JavaPlugin {
	// Main instance.
	private static LinkedWhitelist instance = null;

	public static LinkedWhitelist getInstance() {
		return instance;
	}

	// Variable data.
	protected Settings settings = null;

	@Override
	public void onEnable() {
		instance = this;

		// Construct new settings.
		settings = new Settings();

		if (settings.isEnabled()) {
			final Server server = getServer();

			// Make sure the built in whitelist is off.
			server.setWhitelist(false);

			// Database link.
			final AbstractStorage storage = settings.getStorage();

			Set<OfflinePlayer> players = new LinkedHashSet<>();

			if (settings.isWhitelistExistingPlayers()) {
				// Whitelist existing players.
				players.addAll(Arrays.asList(server.getOfflinePlayers()));
			}

			if (settings.isInheritDefaultWhitelist()) {
				// Inherit the default whitelist.
				players.addAll(server.getWhitelistedPlayers());
			}

			for (final OfflinePlayer player : players) {
				try {
					storage.setPermissionForPlayer(player.getUniqueId(), player.getName(), true, false);
				} catch (final StorageException exception) {
					// Ignore.
				}
			}

			// Create our commands instance.
			getCommand("whitelist").setExecutor(new WhitelistCommand());

			// Register our listeners.
			getServer().getPluginManager().registerEvents(new Listeners(), this);
		}
	}

	@Override
	public void onDisable() {
		instance = null;
	}
}
