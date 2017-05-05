package cafe.neso.minecraft.bouncer.storage

import cafe.neso.minecraft.bouncer.*
import java.sql.*
import java.util.*

/**
 * Created by moltendorf on 2017-04-30.
 */
class PlayerAccess {
  private val players = HashMap<UUID, Bool>()

  private fun store(id : UUID, permission : Bool) : Bool {
    val sql = """
        INSERT INTO ${database.prefix}permissions
          (id, permission)
        VALUES
          (UNHEX(?), ?)
        ON DUPLICATE KEY UPDATE
          permission = permission
      """

    val connection = database.connection ?: throw StorageException()

    try {
      val statement = connection.prepareStatement(sql)
      statement.closeOnCompletion()

      statement.setString(1, "$id".replace("-", ""))
      statement.setBoolean(2, permission)
      statement.setBoolean(3, permission)

      return statement.executeUpdate() > 0
    } catch (e : SQLException) {
      throw StorageException(e)
    }
  }

  private fun fetch(id : UUID) : Bool? {
    val sql = """
        SELECT permission
        FROM ${database.prefix}permissions
        WHERE id = UNHEX(?)
        LIMIT 1
      """

    val connection = database.connection ?: throw StorageException()

    try {
      val statement = connection.prepareStatement(sql)
      statement.closeOnCompletion()
      statement.setString(1, "$id".replace("-", ""))

      val result = statement.executeQuery()
      var permission : Bool? = null

      if (result.next()) {
        permission = result.getBoolean(1)
      }

      result.close()

      return permission
    } catch (e : SQLException) {
      throw StorageException(e)
    }
  }

  @Synchronized operator fun get(uuid : UUID) = fetch(uuid)
  @Synchronized operator fun set(uuid : UUID, permission : Bool) = store(uuid, permission)
}
