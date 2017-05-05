@file:Suppress("DEPRECATION")

package cafe.neso.minecraft.bouncer.bukkit

import cafe.neso.minecraft.bouncer.settings.*
import org.bukkit.block.*

/**
 * Created by moltendorf on 2016-06-07.
 *
 * Extensions file that contains a bunch of useful stuff.
 */

// Package Variables

internal inline val instance get() = BukkitPlugin.instance
internal inline val server get() = instance.server

// Just for us.
private inline val scheduler get() = server.scheduler

// Messages

internal fun broadcast(s : String) = server.broadcastMessage(s)
internal fun console(s : String) = server.consoleSender.sendMessage("[${instance.name}] $s")

// Tasks

internal fun runTask(s : () -> Unit) = scheduler.runTask(instance, s)
internal fun runTask(delay : Long, s : () -> Unit) = scheduler.runTaskLater(instance, s, delay)
internal fun runTask(period : Long, delay : Long = 0, s : () -> Unit) = scheduler.runTaskTimer(instance, s, delay, period)
internal fun runAsyncTask(s : () -> Unit) = scheduler.runTaskAsynchronously(instance, s)
internal fun runAsyncTask(delay : Long, s : () -> Unit) = scheduler.runTaskLaterAsynchronously(instance, s, delay)
internal fun runAsyncTask(period : Long, delay : Long = 0, s : () -> Unit) = scheduler.runTaskTimerAsynchronously(instance, s, delay, period)

// Extensions.

internal fun Settings.saveConfig(path : String = "") {
//  map.forEach { key, value ->
//    when (value) {
//      is Settings -> value.saveConfig("$key.")
//      else -> instance.config["$path$key"] = value
//    }
//  }
}

internal inline var Block.intData get() = data.toInt()
  set(byte) {
    data = byte.toByte()
  }

internal inline var BlockState.intData get() = rawData.toInt()
  set(byte) {
    rawData = byte.toByte()
  }
