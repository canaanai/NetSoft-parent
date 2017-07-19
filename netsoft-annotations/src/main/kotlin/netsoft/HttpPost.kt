package netsoft

import kotlin.reflect.KClass

/**
 * @author chenp
 * @version 2017-07-11 11:00
 */
@Retention(AnnotationRetention.BINARY) @Target(AnnotationTarget.FUNCTION)
annotation class HttpPost(val requestClass: KClass<*>, val requestCode: Int = -1, val scheduler: Schedulers = Schedulers.MAIN)