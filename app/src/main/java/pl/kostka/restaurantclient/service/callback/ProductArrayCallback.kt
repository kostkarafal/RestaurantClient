package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Product

interface ProductArrayCallback: MainCallback<Array<Product>> {
     override fun onResponse(response: Array<Product>)
}