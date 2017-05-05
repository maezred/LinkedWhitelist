package cafe.neso.minecraft.bouncer

import cafe.neso.minecraft.bouncer.settings.*
import cafe.neso.minecraft.bouncer.storage.*
import kotlin.reflect.*
import kotlin.reflect.full.*

/**
 * Created by moltendorf on 2017-04-30.
 */

internal typealias Core = BouncerCore

class BouncerCore(val provider : CoreProvider) {
  var enabled = false

  lateinit var settings : GeneralSettings
    private set

  val database : Database
  val players : PlayerAccess
  val version = Version(provider.version)

  init {
    core = this

    loadSettings()

    if (settings.ignore || settings.test) {
      val new = GeneralSettings()
      val config = provider.defaultConfig.toMutableMap()

      if (settings.test) {
        // Keep this
        new.test = true
        config["test"] = true

        w { "Test mode: ENABLED." }
      }

      // Keep this
      new.ignore = true
      config["ignore"] = true

      w { "Ignoring config." }

      settings = new
      loadSettings(config, settings)
    }

    enabled = settings.enabled

    database = Database(settings.database)
    players = PlayerAccess()
  }

  fun loadSettings() {
    settings = GeneralSettings()
    loadSettings(provider.mainConfig, settings)
  }


  private fun loadSettings(config : Map<*, *>, settings : Settings, prefix : String = "") {
    try {
      settings::class.memberProperties.forEach { property ->
        property.annotations.forEach properties@ {
          val key = prefix + when (it) {
            is Key -> it.key
            is Id -> property.name
            else -> return@properties
          }

          val new = config[key] ?: return@properties
          val type = property.returnType

          test {
            i { "  $key: ${property.call(settings)}" }
          }

          // Make sure the types match
          if (!type.isSupertypeOf(new::class.starProjectedType)) {
            if (property is KMutableProperty1) {
              if (type.isSupertypeOf(String::class.starProjectedType)) {

                property.setter.call(settings, "$new")

                test {
                  if (property.call(settings) != new) {
                    i { "    ${property.name} does not match default value in config.yml" }
                  }
                }
              }
            } else if (type.isSubtypeOf(Settings::class.starProjectedType)) {
              val child = property.call(settings)

              if (child is Settings) {
                loadSettings(config, child, "$key.")
              } else {
                test {
                  if (property.call(settings) != new) {
                    i { "    ${property.name} does not match default value in config.yml" }
                  }
                }
              }
            } else {
              test {
                if (property.call(settings) != new) {
                  i { "    ${property.name} does not match default value in config.yml" }
                }
              }
            }

            return@properties
          }

          if (property is KMutableProperty1) {
            property.setter.call(settings, new)
          }
        }
      }
    } catch (e : IllegalAccessError) {
      trace("$name cannot be reloaded in some conditions. Please restart to restore functionality.", e)
    }
  }

  companion object {
    lateinit var core : Core
      private set
  }
}
