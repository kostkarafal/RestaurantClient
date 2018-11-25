package pl.kostka.restaurantclient.service.callback


interface BooleanCallback: MainCallback<Boolean> {
    override fun onResponse(response: Boolean)
}