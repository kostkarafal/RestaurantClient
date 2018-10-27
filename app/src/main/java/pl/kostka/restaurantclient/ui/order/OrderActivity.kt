package pl.kostka.restaurantclient.ui.order

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
        val totalPrice = findViewById<TextView>(R.id.textView_order_total_price)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        val basket = OrderService.getBasket()

        if(basket.products.size == 0) {
            container_order_summary.visibility = View.INVISIBLE
            textView_order_empty_basket.visibility = View.VISIBLE
        } else {
            totalPrice?.text = String.format("%.2f",basket.totalPrize)
            recyclerView.adapter = OrderAdapter(OrderService.getBasket(),this@OrderActivity)
        }


        button_confirmOrder.setOnClickListener {
            OrderService.makeOrder(object: OrderCallback {
                override fun onResponse(order: Order) {
                    runOnUiThread {
                        Toast.makeText(this@OrderActivity, "Udało sie", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(errMessage: String) {
                    runOnUiThread {
                        Toast.makeText(this@OrderActivity, errMessage, Toast.LENGTH_LONG).show()
                    }

                }
            })
        }
    }
}
