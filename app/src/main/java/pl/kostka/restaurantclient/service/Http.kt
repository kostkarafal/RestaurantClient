package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.service.callback.GetAuthHeaderCallback
import pl.kostka.restaurantclient.service.callback.MainCallback
import pl.kostka.restaurantclient.service.callback.VoidCallback
import java.io.IOException

object Http {
    private const val hostUrl = BuildConfig.HOST_URL
    private val gson = GsonBuilder().create()
    private val mediaType = MediaType.parse("application/json; charset=utf-8")
    private val client = OkHttpClient()
    private const val SERVER_CONNECTION_ERROR = "Błąd połączenia z serwerem"


    fun get(url: String, responseClass: Class<out Any>, callbackAny: Any) {
        val request = Request.Builder()
                .url("$hostUrl/$url").get().build()

        runRequest(request, responseClass, callbackAny)
    }

    fun post(url: String, body: Any?, responseClass: Class<out Any>, callbackAny: Any) {
        val request = Request.Builder()
                .url("$hostUrl/$url").post(RequestBody.create(mediaType, checkRequestBody(body))).build()
        runRequest(request, responseClass, callbackAny)
    }

    fun put(url: String, body: Any?, responseClass: Class<out Any>, callbackAny: Any) {
        val request = Request.Builder()
                .url("$hostUrl/$url").put(RequestBody.create(mediaType, checkRequestBody(body))).build()
        runRequest(request, responseClass, callbackAny)
    }

    fun delete(url: String, callback: VoidCallback) {
        val request = Request.Builder()
                .url("$hostUrl/$url").delete().build()
        runVoidRequest(request, callback)
    }

    fun authGet(url: String, responseClass: Class<out Any>, callbackAny: Any) {

        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/$url")
                        .get()
                        .addHeader("Authorization", "bearer $accesToken").build()
                runRequest(request, responseClass, callbackAny)
            }

            override fun onFailure(errMessage: String) {
                val callback = castCallbackToAny(callbackAny)
                callback.onFailure(errMessage)
            }
        })
    }


    fun authPost(url: String, body: Any?, responseClass: Class<out Any>, callbackAny: Any) {

        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/$url")
                        .post(RequestBody.create(mediaType, checkRequestBody(body)))
                        .addHeader("Authorization", "bearer $accesToken").build()
                runRequest(request, responseClass, callbackAny)
            }

            override fun onFailure(errMessage: String) {
                val callback = castCallbackToAny(callbackAny)
                callback.onFailure(errMessage)
            }
        })
    }

    fun authPut(url: String, body: Any?, responseClass: Class<out Any>, callbackAny: Any) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/$url")
                        .put(RequestBody.create(mediaType, checkRequestBody(body)))
                        .addHeader("Authorization", "bearer $accesToken").build()
               runRequest(request, responseClass, callbackAny)
            }
            override fun onFailure(errMessage: String) {
                val callback = castCallbackToAny(callbackAny)
                callback.onFailure(errMessage)
            }
        })
    }

    fun authDelete(url: String, callback: VoidCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/$url")
                        .delete()
                        .addHeader("Authorization", "bearer $accesToken").build()
                runVoidRequest(request, callback)
            }
            override fun onFailure(errMessage: String) {
                callback.onFailure(errMessage)
            }
        })
    }

    private fun runVoidRequest(request: Request, callback: VoidCallback) {
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.code() == 200) {
                    callback.onResponse()
                } else {
                    callback.onFailure(response.code().toString() + " " + response.message())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(SERVER_CONNECTION_ERROR)
            }
        })
    }

    private fun runRequest(request: Request, responseClass: Class<out Any>, callbackAny: Any) {
        val callback = castCallbackToAny(callbackAny)
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                handleResponse(response, responseClass, callback)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(SERVER_CONNECTION_ERROR)
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun castCallbackToAny(callback: Any): MainCallback<Any> {
        return callback as MainCallback<Any>
    }


    private fun handleResponse(response: Response, bodyClass: Class<out Any>, callback: MainCallback<Any>) {
        if(response.code() == 200) {
                val body = response.body()?.string()
                val result = gson.fromJson(body, bodyClass)
                callback.onResponse(result)
        } else {
            callback.onFailure(response.code().toString() + " " +response.message())
        }
    }

    private fun checkRequestBody(body: Any?) : String {
       return when (body is String) {
         true -> return body as String
         false -> gson.toJson(body)
        }
    }
}