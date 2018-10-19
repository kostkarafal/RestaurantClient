package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Product

interface GetMenuCallback {
    fun onResponse(menu: List<Product>)
    fun onFailure(errMessage: String)
}