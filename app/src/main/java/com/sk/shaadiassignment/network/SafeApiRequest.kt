package com.sk.shaadiassignment.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

/**
 * This class in use for requesting API call
 */
class SafeApiRequest() {

    /**
     * This is use to perform app API call using Coroutine
     *
     * @param T
     * @param call
     * @param successCallback
     * @param errorCallback
     * @param internetCallback
     */
    fun <T : Any> apiRequest(
        call: suspend () -> Response<T>,
        successCallback: ((T?) -> Unit),
        errorCallback: (String) -> Unit,
        internetCallback: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = call.invoke()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        successCallback(response.body())
                    } else {
                        val error = response.errorBody()?.string()
                        val message = StringBuilder()
                        error?.let {
                            try {
                                message.append(JSONObject(it).getString("message"))
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            message.append("\n")
                        }
                        message.append("Error Code: ${response.code()}")
                        errorCallback(message.toString())
                    }
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    e.message?.let { internetCallback(it) }
                }
            }
        }
    }
}