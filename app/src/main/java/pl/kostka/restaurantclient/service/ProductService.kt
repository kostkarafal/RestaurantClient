package pl.kostka.restaurantclient.service


import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.callback.ProductArrayCallback

object ProductService {

        fun getMenu(callback: ProductArrayCallback) {
            Http.get("/products", Array<Product>::class.java, callback)
        }
}