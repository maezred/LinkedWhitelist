package cafe.neso.minecraft.bouncer.storage

import cafe.neso.minecraft.bouncer.settings.*
import java.sql.*

/**
 * Created by moltendorf on 15/07/27.

 * @author moltendorf
 */
class Database(val settings : DatabaseSettings) {
  private val initConnection : Connection? = try {
    if (settings.username != null && settings.password != null) {
      DriverManager.getConnection(settings.url, settings.username, settings.password)
    } else {
      DriverManager.getConnection(settings.url)
    }
  } catch (e : SQLException) {
    null
  }

  val connection = initConnection
    get() {
      if (field?.isValid(0) ?: false) {
        field = initConnection
      }

      return field
    }

  inline val prefix get() = settings.prefix
}
