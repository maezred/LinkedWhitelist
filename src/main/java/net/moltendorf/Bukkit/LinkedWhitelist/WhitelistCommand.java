package net.moltendorf.Bukkit.LinkedWhitelist;

import net.moltendorf.Bukkit.LinkedWhitelist.storage.StorageException;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by moltendorf on 2015-08-01.
 *
 * @author moltendorf
 */
public class WhitelistCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (strings.length == 2) {
			String action = strings[0];

			switch (action) {
				case "check":
					if (commandSender.hasPermission("linkedwhitelist.manage")) {
						final OfflinePlayer player = LinkedWhitelist.getInstance().getServer().getOfflinePlayer(strings[1]);

						if (player != null) {
							try {
								final Boolean permission = Settings.getInstance().getStorage().getPermissionForPlayer(player.getUniqueId(), player.getName());

								if (permission != null && permission.equals(true)) {
									commandSender.sendMessage(String.format("§aPlayer %s is on the whitelist.", player.getName()));
								} else {
									commandSender.sendMessage(String.format("§cPlayer %s is not on the whitelist.", player.getName()));
								}
							} catch (final StorageException exception) {
								commandSender.sendMessage(String.format("§4Failed to check if %s is on the whitelist!", player.getName()));
							}
						} else {
							commandSender.sendMessage(String.format("§cCould not find player: %s.", strings[1]));
						}
					} else {
						commandSender.sendMessage(command.getPermissionMessage());
					}

					return true;

				case "add":
					if (commandSender.hasPermission("linkedwhitelist.manage.add")) {
						final OfflinePlayer player = LinkedWhitelist.getInstance().getServer().getOfflinePlayer(strings[1]);

						if (player != null) {
							try {
								if (Settings.getInstance().getStorage().setPermissionForPlayer(player.getUniqueId(), player.getName(), true)) {
									commandSender.sendMessage(String.format("§aAdded %s to the whitelist!", player.getName()));
								} else {
									commandSender.sendMessage(String.format("§aPlayer %s is already on the whitelist!", player.getName()));
								}
							} catch (final StorageException exception) {
								commandSender.sendMessage(String.format("§4Failed to add %s to the whitelist!", player.getName()));
							}
						} else {
							commandSender.sendMessage(String.format("§cCould not find player: %s.", strings[1]));
						}
					} else {
						commandSender.sendMessage(command.getPermissionMessage());
					}

					return true;

				case "remove":
					if (commandSender.hasPermission("linkedwhitelist.manage.remove")) {
						final OfflinePlayer player = LinkedWhitelist.getInstance().getServer().getOfflinePlayer(strings[1]);

						if (player != null) {
							try {
								if (Settings.getInstance().getStorage().setPermissionForPlayer(player.getUniqueId(), player.getName(), false)) {
									commandSender.sendMessage(String.format("§cRemoved %s from the whitelist!", player.getName()));
								} else {
									commandSender.sendMessage(String.format("§cPlayer %s is not on the whitelist!", player.getName()));
								}
							} catch (final StorageException exception) {
								commandSender.sendMessage(String.format("§4Failed to add %s to the whitelist!", player.getName()));
							}
						} else {
							commandSender.sendMessage(String.format("§cCould not find player: %s.", strings[1]));
						}
					} else {
						commandSender.sendMessage(command.getPermissionMessage());
					}

					return true;
			}
		} else if (strings.length == 1) {
			if (commandSender.hasPermission("linkedwhitelist.manage.toggle")) {
				final String action = strings[0];
				final Settings settings = Settings.getInstance();

				switch (action) {
					case "enable":
						if (settings.isWhitelistEnabled()) {
							commandSender.sendMessage("§aThe whitelist on this server is already enabled!");
						} else {
							settings.toggleWhitelistEnabled();
							commandSender.sendMessage("§aEnabled the whitelist on this server!");
						}

						return true;

					case "disable":
						if (settings.isWhitelistEnabled()) {
							settings.toggleWhitelistEnabled();
							commandSender.sendMessage("§cDisabled the whitelist on this server!");
						} else {
							commandSender.sendMessage("§cThe whitelist on this server is already disabled!");
						}

						return true;
				}

				return false;
			} else {
				commandSender.sendMessage(command.getPermissionMessage());
			}

			return true;
		}

		return false;
	}
}
