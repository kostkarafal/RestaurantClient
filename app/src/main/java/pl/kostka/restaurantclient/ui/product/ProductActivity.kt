package pl.kostka.restaurantclient.ui.product

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.content_product.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Product

class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val product = intent.getSerializableExtra("Product") as Product

        textView_product_name.text = product.name
        textView_product_description.text = product.description
        textView_product_price.text = String.format("%.2f", product.price)
        val imageView = imageView_product_image
        Picasso.with(applicationContext).load(BuildConfig.HOST_URL + "/downloadFile/" + product.imageId.toString()).into(imageView)
    }

}
