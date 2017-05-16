package cafe.neso.minecraft.bouncer

import java.util.logging.Logger

/**
 * Created by moltendorf on 2017-05-05.
 */

interface CoreProvider {
  val name : String
  val version : String

  val logger : Logger

  val config : Map<Any?, Any?>
  val defaultConfig : Map<Any?, Any?>

  val mainConfig get() = config.takeIf { it.isNotEmpty() } ?: defaultConfig

  fun configChanged(key : String, value : Any?) {}
}
