package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Order

interface BooleanCallback {
    fun onResponse(response: Boolean)
    fun onFailure(errMessage: String)
}