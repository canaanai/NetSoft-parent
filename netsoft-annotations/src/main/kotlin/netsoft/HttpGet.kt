package netsoft

import kotlin.reflect.KClass

/**
 * @author chenp
 * @version 2017-07-11 10:33
 */
@Retention(AnnotationRetention.BINARY) @Target(AnnotationTarget.FUNCTION)
annotation class HttpGet(val requestClass: KClass<*> = NullClass::class, val requestTag: String = "", val scheduler: Schedulers = Schedulers.MAIN, val url: String = "")