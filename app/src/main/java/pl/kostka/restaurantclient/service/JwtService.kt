package pl.kostka.restaurantclient.service

import android.content.SharedPreferences
import ca.mimic.oauth2library.OAuth2Client
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.model.TokenResponse
import pl.kostka.restaurantclient.model.User
import pl.kostka.restaurantclient.service.callback.GetAuthHeaderCallback
import pl.kostka.restaurantclient.service.callback.LoginResponseCallback
import pl.kostka.restaurantclient.service.callback.TokenResponeCallback
import pl.kostka.restaurantclient.service.listener.IsLoggdInListener
import java.util.*
import kotlin.properties.Delegates

object JwtService {

        var listener: IsLoggdInListener? = null
        var isLoggedIn: Boolean by Delegates.observable(
                initialValue = false,
                onChange = {
                    property, oldValue, newValue ->
                    listener?.onChange(newValue)
                }
        )
        private var accessToken: String = ""
        private var refreshToken: String = ""
        private var expiredAt: Date = Date()
        private var user: User? = null
        private var sharedPreferences: SharedPreferences? = null
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val hostUrl = BuildConfig.HOST_URL + "/oauth/token"
        private const val clientId = "clientid"
        private const val clientSecret = "clientsecret"
        private const val scope = "read"
        fun setLoggedInListener(listener: IsLoggdInListener){
            this.listener = listener
        }

        fun tryLoginFromSharedPreferences(sharedPreferences: SharedPreferences){
            this.sharedPreferences = sharedPreferences

            val result = sharedPreferences.getString(REFRESH_TOKEN, "")
            if(result != null && result.isNotEmpty()){
                refreshToken = result
                refreshToken()
            }
        }

        fun login(username: String, password: String, callback: LoginResponseCallback){
            OAuth2Client.Builder(username, password, clientId, clientSecret, hostUrl)
                    .scope(scope).build()
                    .requestAccessToken {
                if (it.isSuccessful) {
                    accessToken = it.accessToken
                    refreshToken = it.refreshToken
                    expiredAt = Date(it.expiresAt)
                    isLoggedIn = true
                    sharedPreferences!!.edit().putString(REFRESH_TOKEN, refreshToken).apply()
                    callback.onResponse()
                } else {
                    val error = it.oAuthError
                    val errorMsg = error.error
                    logout()
                    callback.onFailure(errorMsg)
                }
            }
        }

        fun changeToLoggedIn(token: TokenResponse){
            accessToken = token.access_token
            refreshToken = token.refresh_token
            expiredAt = Date(token.expires_in * 1000 + System.currentTimeMillis())
            isLoggedIn = true
            sharedPreferences!!.edit().putString(REFRESH_TOKEN, refreshToken).apply()
        }

        fun loginFacebook(token: String, callback: TokenResponeCallback){
            Http.get("/login/facebook?token=$token", TokenResponse::class.java, callback)
        }

        fun logout(){
            refreshToken = ""
            sharedPreferences!!.edit().putString(REFRESH_TOKEN, refreshToken).apply()
            accessToken = ""
            isLoggedIn = false
            expiredAt = Date()
        }

        fun getAuthorizationHeader(callback: GetAuthHeaderCallback) {
            if (isLoggedIn) {
                if (Date() < expiredAt) {
                    callback.onResponse(accessToken)
                } else {
                    refreshToken(object: LoginResponseCallback {
                        override fun onResponse() {
                            callback.onResponse(accessToken)
                        }

                        override fun onFailure(errMessage: String?) {
                            if (errMessage == null) {
                                callback.onFailure("Serwer nie odpowiada")
                            } else {
                                callback.onFailure(errMessage)
                            }
                        }
                    })
                }
            } else {
                callback.onFailure("Token stracił ważność, zaloguj się ponownie!")
                }
        }

        private fun refreshToken(callback: LoginResponseCallback? = null){
                OAuth2Client.Builder(null, null, clientId, clientSecret, hostUrl).build()
                        .refreshAccessToken(refreshToken) {
                            if (it.isSuccessful) {
                                accessToken = it.accessToken
                                refreshToken = it.refreshToken
                                expiredAt = Date(it.expiresAt)
                                isLoggedIn = true
                                sharedPreferences!!.edit().putString(REFRESH_TOKEN, refreshToken).apply()
                                callback?.onResponse()
                            } else {
                                val error = it.oAuthError
                                val errorMsg = error.error
                                isLoggedIn = false
                                callback?.onFailure(errorMsg)
                            }
                        }
        }

}