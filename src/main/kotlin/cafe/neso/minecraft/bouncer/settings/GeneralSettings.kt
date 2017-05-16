package cafe.neso.minecraft.bouncer.settings

import cafe.neso.minecraft.bouncer.settings
import java.util.logging.Level.INFO

/**
 * Created by moltendorf on 15/06/29.

 * @author moltendorf
 */
class GeneralSettings(override var handler : Handler) : Settings("") {
  val enabled = Value(true) // Whether or not the plugin is enabled at all; interface mode
  val ignore = Value(false) // Ignore the (majority) of the config
  val logging = Value(INFO)
  val test = Value(false)

  val database = DatabaseSettings()

  val whitelistEnabled = Value(true, "whitelist-enabled")

  val whitelistExistingPlayers = Value(false, "whitelist-existing-players")
  val inheritDefaultWhitelist = Value(false, "inherit-default-whitelist")

  override fun load(value: Any?) {
    val handler = settings.handler

    // Just to prevent log spam
    settings.handler = Handler.dummyHandler
    settings.test.value = false
    settings.logging.value = INFO

    super.load(value)

    settings.handler = handler
  }
}
