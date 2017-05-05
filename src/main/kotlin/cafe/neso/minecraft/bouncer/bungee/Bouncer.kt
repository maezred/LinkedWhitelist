package cafe.neso.minecraft.bouncer.bungee

import cafe.neso.minecraft.bouncer.*
import com.google.common.io.*
import net.md_5.bungee.api.plugin.*
import net.md_5.bungee.config.*
import java.io.*


internal typealias BungeePlugin = Bouncer

/**
 * Created by moltendorf on 17/04/30.

 * @author moltendorf
 */
class Bouncer : Plugin() {
  override fun onEnable() {
    if (!dataFolder.exists()) {
      dataFolder.mkdir()
    }

    getResourceAsStream("config.yml").use { input ->
      val configFile = File(dataFolder, "config.yml")

      if (!configFile.exists()) {
        try {
          configFile.createNewFile()
          FileOutputStream(configFile).use { output -> ByteStreams.copy(input, output) }
        } catch (e : Throwable) {
          throw RuntimeException("Unable to create configuration file", e)
        }
      }

      val config = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(configFile)
      val defaultConfig = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(input)

      BouncerCore(object : CoreProvider {
        override val name get() = description.name
        override val version get() = description.version
        override val logger get() = this@Bouncer.logger
        override val config get() = config.keys.associateBy({ it }, { config[it] })
        override val defaultConfig get() = defaultConfig.keys.associateBy({ it }, { defaultConfig[it] })
      })
    }

    instance = this
  }

  override fun onDisable() {
    core.enabled = false
  }

  companion object {
    lateinit var instance : BungeePlugin
      private set
  }
}
