package cafe.neso.minecraft.bouncer.settings

/**
 * Created by moltendorf on 2017-05-05.
 */

class Value<T>(val default : T, key : String? = null) : Bind<T>() {

  var value : T = default
    set(value) {
      field = value
      parent.handler.set(key, field)
    }

  override fun load(value : T) {
    this.value = value
  }

  override fun loadDefaults() {
    value = default
  }

  operator fun component1() = key
  operator fun component2() = value
}
