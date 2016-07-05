package net.moltendorf.bukkit.linkedwhitelist.storage

import java.util.UUID

/**
 * Created by moltendorf on 15/07/27.

 * @author moltendorf
 */
abstract class AbstractStorage {
  fun setPermissionForPlayer(id: UUID, name: String, permission: Boolean): Boolean {
    return setPermissionForPlayer(id, name, permission, true)
  }

  @Throws(StorageException::class)
  abstract fun setPermissionForPlayer(id: UUID, name: String, permission: Boolean, update: Boolean): Boolean

  @Throws(StorageException::class)
  abstract fun getPermissionForPlayer(id: UUID, name: String): Boolean?
}
