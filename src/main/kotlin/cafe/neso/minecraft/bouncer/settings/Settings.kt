package cafe.neso.minecraft.bouncer.settings

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

/**
 * Created by moltendorf on 2017-05-01.
 */

@Retention(RUNTIME)
@Target(PROPERTY)
annotation class Id

@Retention(RUNTIME)
@Target(PROPERTY)
annotation class Key(val key : String)

interface Settings
