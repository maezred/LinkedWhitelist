package net.moltendorf.bukkit.linkedwhitelist

import net.moltendorf.bukkit.linkedwhitelist.storage.StorageException
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * Created by moltendorf on 15/06/29.

 * @author moltendorf
 */
class LinkedWhitelist : JavaPlugin() {

  // Variable data.
  var settings: Settings? = null

  override fun onEnable() {
    instance = this

    // Construct new settings.
    settings = Settings()

    if (settings!!.isEnabled) {
      val server = server

      // Make sure the built in whitelist is off.
      server.setWhitelist(false)

      // Database link.
      val storage = settings!!.storage

      val players = LinkedHashSet<OfflinePlayer>()

      if (settings!!.isWhitelistExistingPlayers) {
        // Whitelist existing players.
        players.addAll(Arrays.asList(*server.offlinePlayers))
      }

      if (settings!!.isInheritDefaultWhitelist) {
        // Inherit the default whitelist.
        players.addAll(server.whitelistedPlayers)
      }

      for (player in players) {
        try {
          storage.setPermissionForPlayer(player.uniqueId, player.name, true, false)
        } catch (exception: StorageException) {
          // Ignore.
        }

      }

      // Create our commands instance.
      getCommand("whitelist").executor = WhitelistCommand()

      // Register our listeners.
      getServer().pluginManager.registerEvents(Listeners(), this)
    }
  }

  override fun onDisable() {
    instance = null
  }

  companion object {
    // Main instance.
    var instance: LinkedWhitelist? = null
      private set
  }
}
