package pl.kostka.restaurantclient.service.callback

interface MainCallback<in T: Any> {
    fun onResponse(response: T)
    fun onFailure(errMessage: String)
}

