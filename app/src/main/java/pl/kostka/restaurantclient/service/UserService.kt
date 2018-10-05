package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import org.mindrot.jbcrypt.BCrypt
import pl.kostka.restaurantclient.model.User
import java.io.IOException


class UserService{

    companion object {
        val hostUrl = "http://192.168.8.107:8060"
        val gson = GsonBuilder().create()
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val client = OkHttpClient()
        var loggedIn: Boolean = false

        fun getUsers() {
            val request = Request.Builder()
                    .url("$hostUrl/users").get().build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    val body = response?.body()?.string()
                    val users = gson.fromJson(body, Array<User>::class.java).toList()
                    println(users.get(0).password)
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    println("Failed to execute")
                }
            })
        }

        fun postUser() {
            val user = User(name = "Jan",
                    surname = "Kowalski",
                    login = "pokaz",
                    password = "Proste123",
                    phoneNumber = "123123123",
                    email = "pokaz@test.pl")

            client.newCall(Request.Builder().url("$hostUrl/users")
                    .post(RequestBody.create(mediaType, gson.toJson(user))).build())
                    .enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            val body = response?.body()?.string()
                            val result = gson.fromJson(body, User::class.java)
                            println(result.login)
                        }

                        override fun onFailure(call: Call?, e: IOException?) {
                            println("Failed to execute")
                        }
                    })
        }

        fun login(login: String, pass: String): Call {

            val user = User(login = login, password = pass)

            return client.newCall(Request.Builder().url("$hostUrl/users/login")
                    .post(RequestBody.create(mediaType, gson.toJson(user))).build())

        }
    }
}