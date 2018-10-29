package pl.kostka.restaurantclient.service

import ca.mimic.oauth2library.OAuth2Client
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.service.callback.GetAuthHeaderCallback
import pl.kostka.restaurantclient.service.callback.LoginResponseCallback
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
        private const val hostUrl = BuildConfig.HOST_URL + "/oauth/token"
        private const val clientId = "clientid"
        private const val clientSecret = "clientsecret"
        private const val scope = "read"

        fun setLoggedInListener(listener: IsLoggdInListener){
            this.listener = listener
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
                    callback.onResponse()
                } else {
                    val error = it.oAuthError
                    val errorMsg = error.error
                    isLoggedIn = false
                    callback.onFailure(errorMsg)
                }
            }
        }

        fun logout(){
            refreshToken = ""
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

        private fun refreshToken(callback: LoginResponseCallback){
                OAuth2Client.Builder(null, null, clientId, clientSecret, hostUrl).build()
                        .refreshAccessToken(refreshToken) {
                            if (it.isSuccessful) {
                                accessToken = it.accessToken
                                refreshToken = it.refreshToken
                                expiredAt = Date(it.expiresAt)
                                isLoggedIn = true
                                callback.onResponse()
                            } else {
                                val error = it.oAuthError
                                val errorMsg = error.error
                                isLoggedIn = false
                                callback.onFailure(errorMsg)
                            }
                        }
        }

}