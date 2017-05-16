package cafe.neso.minecraft.bouncer

import java.util.logging.Level

/**
 * Created by moltendorf on 2017-04-30.
 */

// Welcome to Kotlin
internal typealias Bool = Boolean

// Package variables.

internal inline val core get() = Core.core
internal inline val enabled get() = core.enabled
internal inline val database get() = core.database
internal inline val name get() = core.provider.name
internal inline val settings get() = core.settings
internal inline val version get() = core.version

// Just for us.
private inline val logger get() = core.provider.logger

private val infoEnabled = logger.isLoggable(Level.INFO)
private val fineEnabled = logger.isLoggable(Level.FINE)
private val testEnabled get() = settings.test.value

// Logging

internal infix fun Throwable.trace(s: String) = logger.log(Level.SEVERE, s, this)

// Severe/Warning should always attempt to spit out messages.
internal fun s(s: Any?) = logger.severe("$s")

internal inline fun s(s: () -> Any?) = s(s())
internal fun w(s: Any?) = logger.warning("$s")
internal inline fun w(s: () -> Any?) = w(s())

internal fun i(s: Any?) = logger.info("$s")
internal inline fun i(s: () -> Any?) {
  if (infoEnabled) {
    i(s())
  }
}

internal fun f(s: Any?) = logger.fine("$s")
internal inline fun f(s: () -> Any?) {
  if (fineEnabled) {
    f(s())
  }
}

internal inline fun test(s: () -> Any?) {
  if (testEnabled) {
    i(s())
  }
}

internal inline fun <T, C : Iterable<T>> C.test(predicate: (T) -> Bool, message: (T) -> Any?): C {
  if (testEnabled) {
    filter { predicate(it) }.forEach { i { message(it) } }
  }

  return this
}

internal inline val <T>T.i: T get() {
  i { this }

  return this
}

internal inline val <T>T.f: T get() {
  f { this }

  return this
}

// Extensions.

inline internal fun <T, R> T.isLet(block: (T) -> R): R? =
  when (this) {
    is T -> block(this)
    else -> null
  }

inline internal fun <reified R> Any?.isAlso(block: (R) -> Unit): R? =
  if (this is R) {
    block(this)
    this
  } else null

internal fun <K, V> Map<out K, V>?.getBool(key: K?): Bool? {
  val bool = this?.get(key)

  return when (bool) {
    is Bool -> bool
    is String -> bool.toBoolean()
    is Int -> bool > 0
    is Any -> bool as? Bool
    else -> null
  }
}

internal fun <K, V> Map<out K, V>?.getBool(key: K?, default: Bool) = getBool(key) ?: default
internal operator fun <K, V> Map<out K, V>?.get(key: K?, default: Bool) = getBool(key, default)

internal fun <K, V> Map<out K, V>?.getInt(key: K?): Int? {
  val int = this?.get(key)

  return when (int) {
    is Int -> int
    is String -> int.toIntOrNull()
    is Bool -> if (int) 1 else 0
    is Any -> int as? Int
    else -> null
  }
}

internal fun <K, V> Map<out K, V>?.getInt(key: K?, default: Int) = getInt(key) ?: default
internal operator fun <K, V> Map<out K, V>?.get(key: K?, default: Int) = getInt(key, default)

internal fun <K, V> Map<out K, V>?.getMap(key: K?) = this?.get(key) as? Map<*, *>
internal fun <K, V> Map<out K, V>?.getMap(key: K?, default: Map<*, *>) = getMap(key) ?: default
internal operator fun <K, V> Map<out K, V>?.get(key: K?, default: Map<*, *>) = getMap(key, default)

internal fun <K, V> Map<out K, V>?.getString(key: K?): String? {
  val string = this?.get(key)

  return when (string) {
    is String -> string
    is Bool, is Double, is Float, is Int, is Long -> string.toString()
    else -> null
  }
}

internal fun <K, V> Map<out K, V>?.getString(key: K?, default: String) = getString(key) ?: default
internal operator fun <K, V> Map<out K, V>?.get(key: K?, default: String) = getString(key, default)
