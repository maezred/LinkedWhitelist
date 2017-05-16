package cafe.neso.minecraft.bouncer.bukkit

import cafe.neso.minecraft.bouncer.core
import cafe.neso.minecraft.bouncer.settings
import cafe.neso.minecraft.bouncer.storage.StorageException
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST

/**
 * Created by moltendorf on 2015-08-01.

 * @author moltendorf
 */
class Listeners : Listener {
  @EventHandler
  fun handleAsyncPlayerPreLoginEvent(event : AsyncPlayerPreLoginEvent) {
    if (settings.whitelistEnabled.value) {
      val id = event.uniqueId

      try {
        val permission = core.players[id]

        if (permission == null || permission == false) {
          event.disallow(KICK_WHITELIST, "You are not whitelisted.")
        }
      } catch (exception : StorageException) {
        event.disallow(KICK_WHITELIST, "An exception occurred while trying to check if you are on the " + "whitelist. Please try again and notify an administrator.")
      }
    }
  }
}
