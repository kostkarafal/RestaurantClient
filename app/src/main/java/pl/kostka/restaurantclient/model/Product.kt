package pl.kostka.restaurantclient.model

import pl.kostka.restaurantclient.model.enums.ProductType

class Product(val id: Long,
              val name: String,
              val description: String,
              val type: ProductType,
              val price: Float,
              val imageId: Long)
