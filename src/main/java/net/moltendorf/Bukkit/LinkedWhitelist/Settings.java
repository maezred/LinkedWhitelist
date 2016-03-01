package net.moltendorf.Bukkit.LinkedWhitelist;

import net.moltendorf.Bukkit.LinkedWhitelist.storage.AbstractStorage;
import net.moltendorf.Bukkit.LinkedWhitelist.storage.MySQL;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

/**
 * Created by moltendorf on 15/06/29.
 *
 * @author moltendorf
 */
public class Settings {
	public static Settings getInstance() {
		return LinkedWhitelist.getInstance().settings;
	}

	final private FileConfiguration config;
	private       boolean           dirty;

	public boolean isEnabled() {
		return enabled;
	}

	private boolean enabled = true; // Whether or not the plugin is enabled at all; interface mode.

	synchronized public boolean isWhitelistEnabled() {
		return whitelistEnabled;
	}

	synchronized public boolean toggleWhitelistEnabled() {
		whitelistEnabled = !whitelistEnabled;

		set("whitelist-enabled", whitelistEnabled);
		save();

		return whitelistEnabled;
	}

	private boolean whitelistEnabled = true;

	public boolean isWhitelistExistingPlayers() {
		return whitelistExistingPlayers;
	}

	public boolean isInheritDefaultWhitelist() {
		return inheritDefaultWhitelist;
	}

	private boolean whitelistExistingPlayers = true;
	private boolean inheritDefaultWhitelist  = true;

	public AbstractStorage getStorage() {
		return storage;
	}

	private AbstractStorage storage = null;

	public Settings() {
		final LinkedWhitelist instance = LinkedWhitelist.getInstance();
		final Logger          log      = instance.getLogger();

		// Make sure the default configuration is saved.
		instance.saveDefaultConfig();

		config = instance.getConfig();

		if (config.isBoolean("enabled")) {
			enabled = config.getBoolean("enabled", enabled);
		} else {
			set("enabled", enabled);
		}

		if (config.isBoolean("whitelist-enabled")) {
			whitelistEnabled = config.getBoolean("whitelist-enabled", whitelistEnabled);
		} else {
			set("whitelist-enabled", whitelistEnabled);
		}

		if (config.isBoolean("whitelist-existing-players")) {
			whitelistExistingPlayers = config.getBoolean("whitelist-existing-players", whitelistExistingPlayers);
		} else {
			set("whitelist-existing-players", whitelistExistingPlayers);
		}

		if (config.isBoolean("inherit-default-whitelist")) {
			inheritDefaultWhitelist = config.getBoolean("inherit-default-whitelist", inheritDefaultWhitelist);
		} else {
			set("inherit-default-whitelist", inheritDefaultWhitelist);
		}

		if (config.isConfigurationSection("storage")) {
			final ConfigurationSection storageSection = config.getConfigurationSection("storage");

			if (storageSection.isBoolean("enabled") && storageSection.getBoolean("enabled", false) && storageSection.isString("type")) {
				switch (storageSection.getString("type", "")) {
					case "mysql":
						storage = new MySQL(storageSection);
				}
			}
		}

		save();
	}

	private void save() {
		if (dirty) {
			LinkedWhitelist.getInstance().saveConfig();
			dirty = false;
		}
	}

	public void set(final String path, final Object value) {
		config.set(path, value);
		dirty = true;
	}
}
