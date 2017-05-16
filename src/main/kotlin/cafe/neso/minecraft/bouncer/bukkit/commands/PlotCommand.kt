package cafe.neso.minecraft.bouncer.bukkit.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by moltendorf on 2017-05-15.
 */

class PlotCommand: BaseCommand("plot") {
  override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
    if (sender !is Player) {
      return false
    }

//    sender.teleport(Location(sender.world, ))

    return true
  }
}
