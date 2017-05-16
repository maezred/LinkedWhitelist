package cafe.neso.minecraft.bouncer

import cafe.neso.minecraft.bouncer.settings.GeneralSettings
import cafe.neso.minecraft.bouncer.settings.Handler
import cafe.neso.minecraft.bouncer.storage.Database
import cafe.neso.minecraft.bouncer.storage.PlayerAccess

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

    enabled = settings.enabled.value

    database = Database(settings.database)
    players = PlayerAccess()
  }

  fun loadSettings() {
    settings = GeneralSettings(object : Handler {
      override val config = provider.config
      override val defaultConfig = provider.defaultConfig

      override fun set(key: String, value: Any?) {
          provider.configChanged(key, value)
      }
    })
    settings.load(provider.mainConfig)
  }

  companion object {
    lateinit var core : Core
      private set
  }
}
