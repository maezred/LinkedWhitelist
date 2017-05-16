package cafe.neso.minecraft.bouncer.settings

/**
 * Created by moltendorf on 2017-05-05.
 */

interface Handler {
  val config: Map<Any?, Any?>
  val defaultConfig: Map<Any?, Any?>

  fun set(key: String, value: Any?)

  companion object {
    val dummyHandler = object : Handler {
      override val config = emptyMap<Any?, Any?>()
      override val defaultConfig = emptyMap<Any?, Any?>()

      override fun set(key: String, value: Any?) {}
    }
  }
}
