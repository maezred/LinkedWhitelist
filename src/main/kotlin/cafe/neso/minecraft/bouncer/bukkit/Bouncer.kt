package cafe.neso.minecraft.bouncer.bukkit

import cafe.neso.minecraft.bouncer.*
import cafe.neso.minecraft.bouncer.bukkit.commands.WhitelistCommand
import org.bukkit.plugin.java.JavaPlugin

internal typealias BukkitPlugin = Bouncer

/**
 * Created by moltendorf on 15/06/29.

 * @author moltendorf
 */
class Bouncer : JavaPlugin() {
  override fun onEnable() {
    saveDefaultConfig()

    instance = this

    BouncerCore(object : CoreProvider {
      override val name get() = description.name
      override val version get() = description.version
      override val logger get() = this@Bouncer.logger
      override val config get() = this@Bouncer.config.getValues(true)
      override val defaultConfig get() = this@Bouncer.config.defaults.getValues(true)
    })

    if (enabled) {
      // Make sure the built in whitelist is off.
      server.setWhitelist(false)

      if (settings.whitelistExistingPlayers.value) {
        server.offlinePlayers.forEach { core.players[it.uniqueId] = true }
        w("Whitelisting existing players: it's recommended you set this to false after the first run.")
      }

      if (settings.inheritDefaultWhitelist.value) {
        server.whitelistedPlayers.forEach { core.players[it.uniqueId] = true }
        w("Inheriting default whitelist: it's recommended you set this to false after the first run.")
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
          e.trace("Exception occurred while registering command executor for /${it.command}")
        }
      }

      if (settings.whitelistEnabled.value) {
        // Register listeners
        val manager = server.pluginManager

        manager.registerEvents(Listeners(), this)
        i { "Enabled listeners." }
      }
    }
  }

  override fun onDisable() {
    enabled = false
  }

  companion object {
    var enabled = false
      private set

    lateinit var instance : BukkitPlugin
      private set
  }
}
