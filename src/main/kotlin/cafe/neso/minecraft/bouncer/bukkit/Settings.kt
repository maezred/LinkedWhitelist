package cafe.neso.minecraft.bouncer.bukkit

/**
 * Created by moltendorf on 15/06/29.

 * @author moltendorf
 */
class Settings {
  private var dirty = false

  var enabled = true // Whether or not the plugin is enabled at all; interface mode.
  var whitelistEnabled = true
  var whitelistExistingPlayers = false
  var inheritDefaultWhitelist = false

  var database : Map<String, Any> = mapOf(
    Pair("type", "mysql"),
    Pair("host", "localhost"),
    Pair("port", 3306),
    Pair("prefix", "lw__"),
    Pair("database", "test")
  )

  init {
    // Make sure the default configuration is saved.
    instance.saveDefaultConfig()

    enabled = config.getBoolean("enabled", enabled)

    whitelistEnabled = config.getBoolean("whitelist-enabled", whitelistEnabled)
    whitelistExistingPlayers = config.getBoolean("whitelist-existing-players", whitelistExistingPlayers)
    inheritDefaultWhitelist = config.getBoolean("inherit-default-whitelist", inheritDefaultWhitelist)

    database = config.getConfigurationSection("database").getValues(false)
  }

  internal fun save() {
    if (dirty) {
      instance.saveConfig()

      dirty = false
    }
  }

  operator fun set(path : String, value : Any) {
    config.set(path, value)
    dirty = true
  }
}
