package net.moltendorf.Bukkit.LinkedWhitelist;

import net.moltendorf.Bukkit.LinkedWhitelist.storage.StorageException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

/**
 * Created by moltendorf on 2015-08-01.
 *
 * @author moltendorf
 */
public class Listeners implements Listener {
	@EventHandler
	public void AsyncPlayerPreLoginEventHandler(final AsyncPlayerPreLoginEvent event) {
		final Settings settings = Settings.getInstance();

		if (settings.isWhitelistEnabled()) {
			final UUID id = event.getUniqueId();
			final String name = event.getName();

			try {
				Boolean permission = settings.getStorage().getPermissionForPlayer(id, name);

				if (permission == null || permission.equals(false)) {
					event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "You are not whitelisted.");
				}
			} catch (final StorageException exception) {
				event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "An exception occurred while trying to check if you are on the " +
					"whitelist. Please try again and notify an administrator.");
			}
		}
	}
}
