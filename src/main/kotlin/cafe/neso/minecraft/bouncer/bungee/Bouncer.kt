package cafe.neso.minecraft.bouncer.bungee

import net.md_5.bungee.api.plugin.*

internal typealias BungeePlugin = Bouncer

/**
 * Created by moltendorf on 17/04/30.

 * @author moltendorf
 */
class Bouncer : Plugin() {
  override fun onEnable() {
    instance = this
  }

  override fun onDisable() {
    enabled = false
  }

  companion object {
    var enabled = false
      private set

    lateinit var instance : BungeePlugin
      private set
  }
}
