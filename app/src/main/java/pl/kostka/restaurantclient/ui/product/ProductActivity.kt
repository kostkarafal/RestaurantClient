package pl.kostka.restaurantclient.ui.product

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.content_product.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.OrderService
import pl.kostka.restaurantclient.service.callback.OrderCallback
import pl.kostka.restaurantclient.service.callback.OrderListCallback

class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val buttonAddToOrder = findViewById<Button>(R.id.button_addToOrder)
        val buttonBack = findViewById<ImageButton>(R.id.button_productBack)
        val product = intent.getSerializableExtra("Product") as Product

        textView_product_name.text = product.name
        textView_product_description.text = product.description
        textView_product_price.text = String.format("%.2f", product.price)
        val imageView = imageView_product_image
        Picasso.with(applicationContext).load(BuildConfig.HOST_URL + "/downloadFile/" + product.imageId.toString()).into(imageView)
        val fragment = this@ProductActivity
        val productList: List<Long> = longArrayOf(product.id).toList()

        buttonAddToOrder.setOnClickListener {
            OrderService.addProductToBasket(productList, object: OrderCallback {
                override fun onResponse(order: Order) {
                   fragment.setResult(Activity.RESULT_OK)
                   fragment.finish()
                }
                override fun onFailure(errMessage: String) {
                        Toast.makeText(this@ProductActivity, errMessage, Toast.LENGTH_LONG).show()
                }
            })
        }

        buttonBack.setOnClickListener {
            this.setResult(Activity.RESULT_CANCELED)
            this.finish()
        }

    }

}
