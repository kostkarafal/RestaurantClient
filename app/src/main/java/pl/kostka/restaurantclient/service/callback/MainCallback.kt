package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.ErrorResponse

interface MainCallback<in T: Any> {
    fun onResponse(response: T)
    fun onFailure(error: ErrorResponse)
}

