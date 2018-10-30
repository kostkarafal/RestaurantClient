package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import org.mindrot.jbcrypt.BCrypt
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.model.User
import pl.kostka.restaurantclient.service.callback.BooleanCallback
import pl.kostka.restaurantclient.service.callback.UserCallback
import java.io.IOException


object UserService{
     val hostUrl = BuildConfig.HOST_URL
        val gson = GsonBuilder().create()
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val client = OkHttpClient()
        var loggedIn: Boolean = false
        val string = "string"


        fun registerUser(user: User, callback: UserCallback) {
            client.newCall(Request.Builder().url("$hostUrl/users")
                    .post(RequestBody.create(mediaType, gson.toJson(user))).build())
                    .enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            val body = response?.body()?.string()
                            val result = gson.fromJson(body, User::class.java)
                            callback.onResponse(result)
                        }

                        override fun onFailure(call: Call?, e: IOException?) {
                            callback.onFailure("Błąd połączenia z serwerem")

                        }
                    })
        }

    fun checkIfUsernameIsFree(username: String, callback: BooleanCallback) {
        client.newCall(Request.Builder().url("$hostUrl/users/check-username")
                .post(RequestBody.create(mediaType, username)).build())
                .enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, Boolean::class.java)
                        callback.onResponse(result)
                    }

                    override fun onFailure(call: Call?, e: IOException?) {
                        callback.onFailure("Błąd połączenia z serwerem")

                    }
                })
    }

    fun checkIfEmailIsFree(email: String, callback: BooleanCallback) {
        client.newCall(Request.Builder().url("$hostUrl/users/check-email")
                .post(RequestBody.create(mediaType, email)).build())
                .enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, Boolean::class.java)
                        callback.onResponse(result)
                    }

                    override fun onFailure(call: Call?, e: IOException?) {
                        callback.onFailure("Błąd połączenia z serwerem")

                    }
                })
    }
    
}