package net.moltendorf.Bukkit.LinkedWhitelist.storage;

import java.util.UUID;

/**
 * Created by moltendorf on 15/07/27.
 *
 * @author moltendorf
 */
abstract public class AbstractStorage {
	public boolean setPermissionForPlayer(UUID id, String name, boolean permission) throws StorageException {
		return setPermissionForPlayer(id, name, permission, true);
	}

	abstract public boolean setPermissionForPlayer(UUID id, String name, boolean permission, boolean update) throws StorageException;

	abstract public Boolean getPermissionForPlayer(UUID id, String name) throws StorageException;
}
