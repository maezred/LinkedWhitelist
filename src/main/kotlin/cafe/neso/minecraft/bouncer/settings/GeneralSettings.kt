package cafe.neso.minecraft.bouncer.settings

/**
 * Created by moltendorf on 15/06/29.

 * @author moltendorf
 */
class GeneralSettings : Settings {
  @Id var enabled = true // Whether or not the plugin is enabled at all; interface mode
  @Id var ignore = false // Ignore the config
  @Id var test = false // Test mode

  @Id val database = DatabaseSettings()

  @Key("whitelist-enabled") var whitelistEnabled = true

  @Key("whitelist-existing-players") var whitelistExistingPlayers = false
  @Key("inherit-default-whitelist") var inheritDefaultWhitelist = false
}
