package pl.kostka.restaurantclient.model

class Basket (var id: Long? = null,
              var totalPrize: Float,
              var productsAmount: ArrayList<Int>,
              var products: ArrayList<Product>,
                var restaurantId: Long? = null)