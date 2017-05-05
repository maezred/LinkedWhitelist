package cafe.neso.minecraft.bouncer

import java.util.logging.*

/**
 * Created by moltendorf on 2017-05-05.
 */

interface CoreProvider {
  val name : String
  val version : String

  val logger : Logger

  val config : Map<*, *>
  val defaultConfig : Map<*, *>

  val mainConfig get() = config.takeIf { it.isNotEmpty() } ?: defaultConfig
}
