package netsoft

import com.google.gson.Gson
import org.json.JSONObject
import java.util.HashMap

/**
 * @author chenp
 * @version 2017-07-12 11:36
 */
class NetSoft {

    fun showClass(obj: Any){
        print(obj::class.java.canonicalName)
    }

    companion object{
        @JvmStatic
        fun <G> jsonToBean(json: String, clazz: Class<G>) :G{
            @Suppress("UNCHECKED_CAST")
            if ( clazz == String::class.java)
                return json as G

            return Gson().fromJson(json, clazz)
        }

        @JvmStatic
        fun beanToMap(bean: Any?): Map<String, String>{
            val map = HashMap<String, String>()

            bean?.let {
                val json = Gson().toJson(bean)
                val jsonObject = JSONObject(json)

                for ( key in jsonObject.keys()){
                    map[key] = jsonObject.getString(key)
                }
            }

            return map
        }
    }
}