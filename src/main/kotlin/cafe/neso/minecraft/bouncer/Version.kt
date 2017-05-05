package cafe.neso.minecraft.bouncer

/**
 * Created by moltendorf on 2017-05-05.
 */

class Version(private val version : String) {
  val dirty = version.contains("uncommitted")
  val release = version.contains('-')
  val snapshot = version.contains("dev")

  override fun toString() : String {
    return version
  }
}
