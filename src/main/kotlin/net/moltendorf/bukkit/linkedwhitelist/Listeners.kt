package net.moltendorf.bukkit.linkedwhitelist

import net.moltendorf.bukkit.linkedwhitelist.storage.StorageException
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

/**
 * Created by moltendorf on 2015-08-01.

 * @author moltendorf
 */
class Listeners : Listener {
  @EventHandler
  fun AsyncPlayerPreLoginEventHandler(event: AsyncPlayerPreLoginEvent) {
    val settings = Settings.instance

    if (settings.isWhitelistEnabled) {
      val id = event.uniqueId
      val name = event.name

      try {
        val permission = settings.storage!!.getPermissionForPlayer(id, name)

        if (permission == null || permission == false) {
          event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "You are not whitelisted.")
        }
      } catch (exception: StorageException) {
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "An exception occurred while trying to check if you are on the " + "whitelist. Please try again and notify an administrator.")
      }

    }
  }
}
