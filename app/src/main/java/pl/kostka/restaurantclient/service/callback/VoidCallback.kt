package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.ErrorResponse

interface VoidCallback{
    fun onResponse()
    fun onFailure(error: ErrorResponse)
}