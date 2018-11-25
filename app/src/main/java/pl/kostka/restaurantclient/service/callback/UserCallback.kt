package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.User


interface UserCallback : MainCallback<User>{
    override fun onResponse(user: User)
}