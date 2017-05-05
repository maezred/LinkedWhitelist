package cafe.neso.minecraft.bouncer.bukkit.commands

import cafe.neso.minecraft.bouncer.*
import cafe.neso.minecraft.bouncer.bukkit.*
import cafe.neso.minecraft.bouncer.storage.*
import org.bukkit.command.*

/**
 * Created by moltendorf on 2017-04-21.
 */

class WhitelistCommand : BaseCommand("whitelist") {
  override fun onCommand(sender : CommandSender?, command : Command, label : String, args : Array<out String>) : Bool {
    if (sender == null) {
      return false
    }

    when (args.size) {
      2 -> {
        val (action, search) = args

        val name : String
        var message : String

        when (action) {
          "check" -> {
            if (sender.hasPermission("linkedwhitelist.manage")) {
              val player = server.getOfflinePlayer(search)

              if (player != null) {
                name = player.name

                try {
                  val permission = core.players[player.uniqueId]

                  if (permission != null && permission) {
                    message = "§aPlayer %s is on the whitelist."
                  } else {
                    message = "§cPlayer %s is not on the whitelist."
                  }
                } catch (e : StorageException) {
                  message = "§4Failed to check if %s is on the whitelist!"
                }
              } else {
                name = search
                message = "§cCould not find player: %s."
              }
            } else {
              name = search
              message = command.permissionMessage
            }

            sender.sendMessage(message.format(name))

            return true
          }

          "add" -> {
            if (sender.hasPermission("linkedwhitelist.manage.add")) {
              val player = server.getOfflinePlayer(search)

              if (player != null) {
                name = player.name

                try {
                  core.players[player.uniqueId] = true
                  message = "§aAdded %s to the whitelist!"
                  //message = "§aPlayer %s is already on the whitelist!"
                } catch (exception : StorageException) {
                  message = "§4Failed to add %s to the whitelist!"
                }

              } else {
                name = search
                message = "§cCould not find player: %s."
              }
            } else {
              name = search
              message = command.permissionMessage
            }

            sender.sendMessage(message.format(name))

            return true
          }

          "remove" -> {
            if (sender.hasPermission("linkedwhitelist.manage.remove")) {
              val player = server.getOfflinePlayer(search)

              if (player != null) {
                name = player.name

                try {
                  core.players[player.uniqueId] = false
                  message = "§cRemoved %s from the whitelist!"
                  //message = "§cPlayer %s is not on the whitelist!"
                } catch (exception : StorageException) {
                  message = "§4Failed to add %s to the whitelist!"
                }

              } else {
                name = search
                message = "§cCould not find player: %s."
              }
            } else {
              name = search
              message = command.permissionMessage
            }

            sender.sendMessage(message.format(name))

            return true
          }

          else -> return false
        }
      }

      1 -> {
        val (action) = args

        val message : String

        if (sender.hasPermission("linkedwhitelist.manage.enable")) {
          when (action) {
            "enable" -> {
              if (settings.whitelistEnabled) {
                message = "§aThe whitelist on this server is already enabled!"
              } else {
                settings.whitelistEnabled = true
                message = "§aEnabled the whitelist on this server!"
              }
            }

            "disable" -> {
              if (settings.whitelistEnabled) {
                settings.whitelistEnabled = false
                message = "§cDisabled the whitelist on this server!"
              } else {
                message = "§cThe whitelist on this server is already disabled!"
              }
            }

            else -> return false
          }
        } else {
          message = command.permissionMessage
        }

        sender.sendMessage(message)

        return true
      }

      else -> return false
    }
  }
}
