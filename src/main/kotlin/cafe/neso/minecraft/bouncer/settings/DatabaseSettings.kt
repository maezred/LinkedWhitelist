package cafe.neso.minecraft.bouncer.settings

/**
 * Created by moltendorf on 2017-04-30.
 */

class DatabaseSettings : Settings {
  @Id var host = "localhost"
  @Id var port = 3306
  @Id var database = "database"

  @Id var prefix = "lw__"

  @Id var username : String? = null
  @Id var password : String? = null

  val url get() = "jdbc:mysql://$host:$port/$database?dontTrackOpenResources=true&useAffectedRows=true"
}
