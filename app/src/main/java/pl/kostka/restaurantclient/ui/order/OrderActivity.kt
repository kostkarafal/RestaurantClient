package pl.kostka.restaurantclient.ui.order

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_order.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Basket
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.service.OrderService
import pl.kostka.restaurantclient.service.callback.BasketCallback
import pl.kostka.restaurantclient.service.callback.OrderCallback

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_order)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        OrderService.getBasket(object: BasketCallback{
            override fun onResponse(basket: Basket) {
                recyclerView.adapter = OrderAdapter(basket, this@OrderActivity)
            }

            override fun onFailure(errMessage: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        button_confirmOrder.setOnClickListener {

        }
    }
}
