package cafe.neso.minecraft.bouncer.settings

/**
 * Created by moltendorf on 2017-04-30.
 */

class DatabaseSettings : Settings() {
  var host = Value("localhost")
  var port = Value(3306)
  var database = Value("database")

  var prefix = Value("lw__")

  var username = Value<String?>(null)
  var password = Value<String?>(null)

  val url get() = "jdbc:mysql://$host:$port/$database?dontTrackOpenResources=true&useAffectedRows=true"
}
