package cafe.neso.minecraft.bouncer.settings

/**
 * Created by moltendorf on 2017-05-06.
 */

abstract class Bind<in T> {
  lateinit var key : String
  lateinit var parent : Settings

  fun bind(key : String, parent : Settings) {
    this.parent = parent
    this.key = "${parent.key}.$key"
  }

  abstract fun load(value : T)
  abstract fun loadDefaults()
}
