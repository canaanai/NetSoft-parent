package netsoft

import retrofit2.Retrofit
import rx.Subscription
import kotlin.reflect.KClass

/**
 * @author chenp
 * @version 2017-07-12 16:59
 */
abstract class HttpClient(val apiImpi: BaseApiInterface) {

    //lateinit var retrofit: Retrofit
    //lateinit var baseApi: BaseApiInterface
    val httpTasks = ArrayList<HttpTaskInfo>()

    abstract fun doRequest(params: Any?, tag: String?): Subscription

    fun request(params: Any, tag: String?){

        httpTasks.add(HttpTaskInfo(params::class, tag, doRequest(params, tag)))
    }

    fun requestNoParam(tag: String){
        httpTasks.add(HttpTaskInfo(null, tag, doRequest(null, tag)))
    }

    fun cancel(className: KClass<*>){
        val canceldTasks = httpTasks.filter {
            if (it.className == className ){
                it.subscription.unsubscribe()

                return@filter true
            }else
                return@filter false
        }

        httpTasks.removeAll(canceldTasks)
    }

    fun cancel(tag: String){
        val canceldTasks = httpTasks.filter {
            if (it.tag == tag){
                it.subscription.unsubscribe()

                return@filter true
            }else
                return@filter false
        }

        httpTasks.removeAll(canceldTasks)
    }

    fun cancelAll(){
        httpTasks.forEach { it.subscription.unsubscribe() }
        httpTasks.clear()
    }
}