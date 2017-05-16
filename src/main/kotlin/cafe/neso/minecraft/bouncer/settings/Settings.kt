package cafe.neso.minecraft.bouncer.settings

import cafe.neso.minecraft.bouncer.isAlso
import cafe.neso.minecraft.bouncer.settings
import cafe.neso.minecraft.bouncer.test
import kotlin.reflect.full.memberProperties

/**
 * Created by moltendorf on 2017-05-01.
 */

abstract class Settings(key: String? = null) : Bind<Any?>() {
  open val handler: Handler get() = parent.handler

  private val values by lazy {
    this::class.memberProperties.mapNotNull { p -> p.call(p)?.isAlso<Bind<Any?>> { it.bind(p.name, this) } }
  }

  override fun load(value: Any?) {
    values.forEach {
      handler.config["$key${it.key}"]?.let { v -> it.load(v) }
    }

    test { "Test mode: ENABLED." }

    if (settings.ignore.value) {
      values.filterIsInstance<Value<Any?>>()
        .onEach { it.value = handler.config[it.key] ?: it.default }
        .test({ it.default != handler.defaultConfig[it.key] }, { "${it.key} doesn't match its default" })
    }
  }

  override fun loadDefaults() = values.forEach(Bind<Any?>::loadDefaults)
}
