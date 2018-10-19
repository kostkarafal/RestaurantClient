package pl.kostka.restaurantclient.ui.order

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_order.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.service.JwtService

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        button_confirmOrder.setOnClickListener {
          //  JwtService.getAuthToken()
        }
    }
}
