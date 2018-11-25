package pl.kostka.restaurantclient.ui.product

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.widget.Button
import android.widget.ImageButton
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.content_product.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.OrderService

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
        val fragment = this@ProductActivity //TODO handle adding more than one product
        val productList: List<Long> = longArrayOf(product.id).toList()

        buttonAddToOrder.setOnClickListener {
            OrderService.addProductToBasket(product)
            this@ProductActivity.setResult(Activity.RESULT_OK)
            this@ProductActivity.finish()
        }

        buttonBack.setOnClickListener {
            this.setResult(Activity.RESULT_CANCELED)
            this.finish()
        }

    }

}
