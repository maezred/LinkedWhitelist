package cafe.neso.minecraft.bouncer.bukkit

import cafe.neso.minecraft.bouncer.bukkit.commands.*
import cafe.neso.minecraft.bouncer.bukkit.storage.*
import org.bukkit.plugin.java.*

internal typealias BukkitPlugin = Bouncer

/**
 * Created by moltendorf on 15/06/29.

 * @author moltendorf
 */
class Bouncer : JavaPlugin() {
  lateinit var database : Database private set
  lateinit var players : PlayerAccess private set
  lateinit var settings : Settings private set

  override fun onEnable() {
    instance = this

    // Construct new settings.
    settings = Settings()

    // Are we enabled?
    enabled = settings.enabled

    if (enabled) {
      // Connect to database
      database = Database(settings.database)

      // Load player access table
      players = PlayerAccess()

      // Make sure the built in whitelist is off.
      server.setWhitelist(false)

      if (settings.whitelistExistingPlayers) {
        // Whitelist existing players
        server.offlinePlayers.forEach { players[it.uniqueId] = true }
      }

      if (settings.inheritDefaultWhitelist) {
        // Inherit the default whitelist
        server.whitelistedPlayers.forEach { players[it.uniqueId] = true }
      }

      // Register commands.
      listOf(
        WhitelistCommand()
      ).forEach {
        try {
          val command = getCommand(it.command)

          command.executor = it

          i { "Registered command executor for /${command.name}" }

          command.aliases.forEach {
            i { "Registered command /$it (alias of /${command.name})" }
          }
        } catch (e : Exception) {
          trace("Exception occurred while registering command executor for /${it.command}", e)
        }
      }

      if (settings.whitelistEnabled) {
        // Register listeners
        val manager = server.pluginManager

        manager.registerEvents(Listeners(), this)
        i { "Enabled listeners." }
      }
    }
  }

  override fun onDisable() {
    settings.save()

    enabled = false
  }

  companion object {
    var enabled = false
      private set

    lateinit var instance : BukkitPlugin
      private set
  }
}
