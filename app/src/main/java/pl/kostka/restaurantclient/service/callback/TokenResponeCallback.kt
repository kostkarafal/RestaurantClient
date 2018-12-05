package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.TokenResponse
import pl.kostka.restaurantclient.model.User


interface TokenResponeCallback : MainCallback<TokenResponse>{
    override fun onResponse(response: TokenResponse)
}