package pl.kostka.restaurantclient.model

class TokenResponse(val access_token: String,
                    val refresh_token: String,
                    val tokenType: String,
                    val expires_in: Int,
                    val scope: String,
                    val jti: String)