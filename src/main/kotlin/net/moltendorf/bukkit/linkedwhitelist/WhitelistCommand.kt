package net.moltendorf.bukkit.linkedwhitelist

import net.moltendorf.bukkit.linkedwhitelist.storage.StorageException
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * Created by moltendorf on 2015-08-01.

 * @author moltendorf
 */
class WhitelistCommand : CommandExecutor {
  override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
    if (strings.size == 2) {
      val action = strings[0]

      when (action) {
        "check" -> {
          if (commandSender.hasPermission("linkedwhitelist.manage")) {
            val player = LinkedWhitelist.instance!!.getServer().getOfflinePlayer(strings[1])

            if (player != null) {
              try {
                val permission = Settings.instance.storage!!.getPermissionForPlayer(player!!.getUniqueId(), player!!.getName())

                if (permission != null && permission == true) {
                  commandSender.sendMessage(String.format("§aPlayer %s is on the whitelist.", player!!.getName()))
                } else {
                  commandSender.sendMessage(String.format("§cPlayer %s is not on the whitelist.", player!!.getName()))
                }
              } catch (exception: StorageException) {
                commandSender.sendMessage(String.format("§4Failed to check if %s is on the whitelist!", player!!.getName()))
              }

            } else {
              commandSender.sendMessage(String.format("§cCould not find player: %s.", strings[1]))
            }
          } else {
            commandSender.sendMessage(command.permissionMessage)
          }

          return true
        }

        "add" -> {
          if (commandSender.hasPermission("linkedwhitelist.manage.add")) {
            val player = LinkedWhitelist.instance!!.getServer().getOfflinePlayer(strings[1])

            if (player != null) {
              try {
                if (Settings.instance.storage!!.setPermissionForPlayer(player!!.getUniqueId(), player!!.getName(), true)) {
                  commandSender.sendMessage(String.format("§aAdded %s to the whitelist!", player!!.getName()))
                } else {
                  commandSender.sendMessage(String.format("§aPlayer %s is already on the whitelist!", player!!.getName()))
                }
              } catch (exception: StorageException) {
                commandSender.sendMessage(String.format("§4Failed to add %s to the whitelist!", player!!.getName()))
              }

            } else {
              commandSender.sendMessage(String.format("§cCould not find player: %s.", strings[1]))
            }
          } else {
            commandSender.sendMessage(command.permissionMessage)
          }

          return true
        }

        "remove" -> {
          if (commandSender.hasPermission("linkedwhitelist.manage.remove")) {
            val player = LinkedWhitelist.instance!!.getServer().getOfflinePlayer(strings[1])

            if (player != null) {
              try {
                if (Settings.instance.storage!!.setPermissionForPlayer(player!!.getUniqueId(), player!!.getName(), false)) {
                  commandSender.sendMessage(String.format("§cRemoved %s from the whitelist!", player!!.getName()))
                } else {
                  commandSender.sendMessage(String.format("§cPlayer %s is not on the whitelist!", player!!.getName()))
                }
              } catch (exception: StorageException) {
                commandSender.sendMessage(String.format("§4Failed to add %s to the whitelist!", player!!.getName()))
              }

            } else {
              commandSender.sendMessage(String.format("§cCould not find player: %s.", strings[1]))
            }
          } else {
            commandSender.sendMessage(command.permissionMessage)
          }

          return true
        }
      }
    } else if (strings.size == 1) {
      if (commandSender.hasPermission("linkedwhitelist.manage.toggle")) {
        val action = strings[0]
        val settings = Settings.instance

        when (action) {
          "enable" -> {
            if (settings.isWhitelistEnabled) {
              commandSender.sendMessage("§aThe whitelist on this server is already enabled!")
            } else {
              settings.toggleWhitelistEnabled()
              commandSender.sendMessage("§aEnabled the whitelist on this server!")
            }

            return true
          }

          "disable" -> {
            if (settings.isWhitelistEnabled) {
              settings.toggleWhitelistEnabled()
              commandSender.sendMessage("§cDisabled the whitelist on this server!")
            } else {
              commandSender.sendMessage("§cThe whitelist on this server is already disabled!")
            }

            return true
          }
        }

        return false
      } else {
        commandSender.sendMessage(command.permissionMessage)
      }

      return true
    }

    return false
  }
}
