package cafe.neso.minecraft.bouncer.bukkit.storage

import cafe.neso.minecraft.bouncer.bukkit.*
import java.sql.*

/**
 * Created by moltendorf on 15/07/27.

 * @author moltendorf
 */
class Database(info : Map<String, Any>) {
  private val host = info["host"]?.toString() ?: "localhost"
  private val port = info["port"]?.toString()?.toOptionalInt() ?: 3306
  private val database = info["database"]?.toString() ?: "database"
  private val username = info["username"]?.toString()
  private val password = info["password"]?.toString()

  private val url = "jdbc:mysql://$host:$port/$database?dontTrackOpenResources=true&useAffectedRows=true"

  private val initConnection =
    if (username != null && password != null) {
      DriverManager.getConnection(url, username, password)
    } else {
      DriverManager.getConnection(url)
    }

  val prefix = info["prefix"]?.toString() ?: "qc__"

  val connection = initConnection
    get() {
      if (!field.isValid(0)) {
        field = initConnection
      }

      return field
    }
}
