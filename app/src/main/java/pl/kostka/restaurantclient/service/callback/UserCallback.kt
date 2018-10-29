package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.User


interface UserCallback {
    fun onResponse(user: User)
    fun onFailure(errMessage: String)
}