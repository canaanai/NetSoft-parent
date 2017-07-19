package netsoft

import rx.Subscription
import kotlin.reflect.KClass

/**
 * @author chenp
 * @version 2017-07-13 14:32
 */
data class HttpTaskInfo(val className: KClass<*>? = null, val tag: String? = null, val subscription: Subscription)