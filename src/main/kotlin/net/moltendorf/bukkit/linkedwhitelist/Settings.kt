package net.moltendorf.bukkit.linkedwhitelist

import net.moltendorf.bukkit.linkedwhitelist.storage.AbstractStorage
import net.moltendorf.bukkit.linkedwhitelist.storage.MySQL
import org.bukkit.configuration.file.FileConfiguration

/**
 * Created by moltendorf on 15/06/29.

 * @author moltendorf
 */
class Settings {

  private val config: FileConfiguration
  private var dirty: Boolean = false

  var isEnabled = true
    private set // Whether or not the plugin is enabled at all; interface mode.

  @Synchronized fun toggleWhitelistEnabled(): Boolean {
    isWhitelistEnabled = !isWhitelistEnabled

    set("whitelist-enabled", isWhitelistEnabled)
    save()

    return isWhitelistEnabled
  }

  var isWhitelistEnabled = true
    private set

  var isWhitelistExistingPlayers = true
    private set
  var isInheritDefaultWhitelist = true
    private set

  var storage: AbstractStorage? = null
    private set

  init {
    val instance = LinkedWhitelist.instance!!
    val log = instance.getLogger()

    // Make sure the default configuration is saved.
    instance.saveDefaultConfig()

    config = instance.getConfig()

    if (config.isBoolean("enabled")) {
      isEnabled = config.getBoolean("enabled", isEnabled)
    } else {
      set("enabled", isEnabled)
    }

    if (config.isBoolean("whitelist-enabled")) {
      isWhitelistEnabled = config.getBoolean("whitelist-enabled", isWhitelistEnabled)
    } else {
      set("whitelist-enabled", isWhitelistEnabled)
    }

    if (config.isBoolean("whitelist-existing-players")) {
      isWhitelistExistingPlayers = config.getBoolean("whitelist-existing-players", isWhitelistExistingPlayers)
    } else {
      set("whitelist-existing-players", isWhitelistExistingPlayers)
    }

    if (config.isBoolean("inherit-default-whitelist")) {
      isInheritDefaultWhitelist = config.getBoolean("inherit-default-whitelist", isInheritDefaultWhitelist)
    } else {
      set("inherit-default-whitelist", isInheritDefaultWhitelist)
    }

    if (config.isConfigurationSection("storage")) {
      val storageSection = config.getConfigurationSection("storage")

      if (storageSection.isBoolean("enabled") && storageSection.getBoolean("enabled", false) && storageSection.isString("type")) {
        when (storageSection.getString("type", "")) {
          "mysql" -> storage = MySQL(storageSection)
        }
      }
    }

    save()
  }

  private fun save() {
    if (dirty) {
      LinkedWhitelist.instance!!.saveConfig()
      dirty = false
    }
  }

  operator fun set(path: String, value: Any) {
    config.set(path, value)
    dirty = true
  }

  companion object {
    val instance: Settings
      get() = LinkedWhitelist.instance!!.settings!!
  }
}
