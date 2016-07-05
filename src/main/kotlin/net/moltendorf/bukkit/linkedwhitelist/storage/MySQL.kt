package net.moltendorf.bukkit.linkedwhitelist.storage

import net.moltendorf.bukkit.linkedwhitelist.LinkedWhitelist
import net.moltendorf.bukkit.linkedwhitelist.Settings
import org.bukkit.configuration.ConfigurationSection

import java.sql.*
import java.util.UUID

/**
 * Created by moltendorf on 15/07/27.

 * @author moltendorf
 */
class MySQL(storageSection: ConfigurationSection) : AbstractStorage() {
  internal var host = "localhost"
  internal var port = 3306
  internal var prefix = "qc__"
  internal var database = "database"
  internal var username = "username"
  internal var password = "password"

  internal var connection: Connection? = null

  init {
    if (storageSection.isString("host")) {
      host = storageSection.getString("host", host)
    } else {
      set("host", host)
    }

    if (storageSection.isInt("port")) {
      port = storageSection.getInt("port", port)
    } else {
      set("port", port)
    }

    if (storageSection.isString("prefix")) {
      prefix = storageSection.getString("prefix", prefix)
    } else {
      set("prefix", prefix)
    }

    if (storageSection.isString("database")) {
      database = storageSection.getString("database", database)
    } else {
      set("database", database)
    }

    if (storageSection.isString("username")) {
      username = storageSection.getString("username", username)
    } else {
      set("username", username)
    }

    if (storageSection.isString("password")) {
      password = storageSection.getString("password", password)
    } else {
      set("password", password)
    }

    connect()
  }

  private operator fun set(path: String, value: Any) {
    Settings.instance["storage." + path] = value
  }

  private fun connect() {
    connection = null

    try {
      val url = "jdbc:mysql://$host:$port/$database?dontTrackOpenResources=true&useAffectedRows=true"
      connection = DriverManager.getConnection(url, username, password)
    } catch (exception: Exception) {
      LinkedWhitelist.getInstance().getLogger().warning("Could not connect to MySQL database with provided information.")
      exception.printStackTrace()
    }

  }

  @Throws(StorageException::class)
  override fun setPermissionForPlayer(id: UUID, name: String, permission: Boolean, update: Boolean): Boolean {
    try {
      if (connection == null || !connection!!.isValid(0)) {
        connect()

        if (connection == null) {
          LinkedWhitelist.getInstance().getLogger().warning("Failed to set permission for player $name.")

          return false
        }
      }
    } catch (exception: SQLException) {
      LinkedWhitelist.getInstance().getLogger().warning("Failed to set permission for player $name.")

      throw StorageException()
    }

    val lookup = id.toString().replace("-", "")

    var statement: PreparedStatement? = null

    var updated = false

    try {
      var sql = "insert into " + prefix + "permissions " +
        "(id, permission) " +
        "values (UNHEX(?), ?) "

      if (update) {
        sql += "on duplicate key update " + "permission = ?"
      } else {
        sql += "on duplicate key update " + "permission = permission"
      }

      statement = connection!!.prepareStatement(sql)

      var i = 0

      statement!!.setString(++i, lookup)
      statement.setBoolean(++i, permission)

      if (update) {
        // Update values.
        statement.setBoolean(++i, permission)
      }

      updated = statement.executeUpdate() > 0
    } catch (exception: SQLException) {
      LinkedWhitelist.getInstance().getLogger().warning("Failed to set permission for player $name.")
      exception.printStackTrace()

      throw StorageException()
    } finally {
      if (statement != null) {
        try {
          statement.close()
        } catch (exception: SQLException) {
          // Quiet!
        }

      }
    }

    return updated
  }

  @Throws(StorageException::class)
  override fun getPermissionForPlayer(id: UUID, name: String): Boolean? {
    try {
      if (connection == null || !connection!!.isValid(0)) {
        connect()

        if (connection == null) {
          LinkedWhitelist.getInstance().getLogger().warning("Failed to get permission for player $id.")

          return false
        }
      }
    } catch (exception: SQLException) {
      LinkedWhitelist.getInstance().getLogger().warning("Failed to get permission for player $id.")

      throw StorageException()
    }

    val lookup = id.toString().replace("-", "")

    var permission: Boolean? = null

    var statement: PreparedStatement? = null
    var result: ResultSet? = null

    try {
      statement = connection!!.prepareStatement(
        "select permission " +
          "from " + prefix + "permissions " +
          "where id = UNHEX(?) " +
          "limit 1")

      var i = 0

      statement!!.setString(++i, lookup)

      result = statement.executeQuery()

      if (result!!.next()) {
        i = 0

        permission = result.getBoolean(++i)
      }
    } catch (exception: SQLException) {
      LinkedWhitelist.getInstance().getLogger().warning("Failed to get permission for player $id.")
      exception.printStackTrace()

      throw StorageException()
    } finally {
      if (result != null) {
        try {
          result.close()
        } catch (exception: SQLException) {
          // Quiet!
        }

      }

      if (statement != null) {
        try {
          statement.close()
        } catch (exception: SQLException) {
          // Quiet!
        }

      }
    }

    return permission
  }
}
