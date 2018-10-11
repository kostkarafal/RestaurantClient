package pl.kostka.restaurantclient.model

import pl.kostka.restaurantclient.model.enums.ProductType
import java.io.Serializable

data class Product(var id: Long,
              var name: String,
                   var description: String,
                   var type: ProductType,
                   var price: Float,
                   var imageId: Long) : Serializable
