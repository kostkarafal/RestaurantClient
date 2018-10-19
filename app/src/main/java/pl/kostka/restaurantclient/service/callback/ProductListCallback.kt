package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Product

interface ProductListCallback {
    fun onResponse(menu: List<Product>)
    fun onFailure(errMessage: String)
}