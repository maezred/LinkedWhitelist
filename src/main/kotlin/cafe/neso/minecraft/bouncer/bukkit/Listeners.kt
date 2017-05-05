package cafe.neso.minecraft.bouncer.bukkit

import cafe.neso.minecraft.bouncer.*
import cafe.neso.minecraft.bouncer.storage.*
import org.bukkit.event.*
import org.bukkit.event.player.*
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.*

/**
 * Created by moltendorf on 2015-08-01.

 * @author moltendorf
 */
class Listeners : Listener {
  @EventHandler
  fun handleAsyncPlayerPreLoginEvent(event : AsyncPlayerPreLoginEvent) {
    if (settings.whitelistEnabled) {
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
