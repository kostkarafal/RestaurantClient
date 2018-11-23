package pl.kostka.restaurantclient.ui.basket

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_basket.*
import kotlinx.android.synthetic.main.basket_top_panel.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.service.AddressService
import pl.kostka.restaurantclient.service.OrderService
import pl.kostka.restaurantclient.service.callback.AddressCallback
import pl.kostka.restaurantclient.service.callback.OrderCallback

class BasketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_basket)
        val totalPrice = findViewById<TextView>(R.id.textView_basket_total_price)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar_basket)
        progressBar.visibility = View.INVISIBLE
        val basket = OrderService.getBasket()

        if(basket.products.size == 0) {
            container_basket_summary.visibility = View.INVISIBLE
            button_basket_confirm_order.visibility = View.INVISIBLE
            textView_basket_empty.visibility = View.VISIBLE
        } else {
            textView_basket_empty.visibility = View.INVISIBLE
            totalPrice?.text = String.format("%.2f",basket.totalPrize)
            recyclerView.adapter = BasketAdapter(OrderService.getBasket(),this@BasketActivity)
        }

        radioButton_basket_delivery.setOnClickListener {
            radioButton_basket_self_pickup.isChecked = false
            textView_basket_addres_type.text = getText(R.string.delivery_address)

        }



        radioButton_basket_self_pickup.setOnClickListener {
            radioButton_basket_delivery.isChecked = false
            textView_basket_addres_type.text = getText(R.string.yours_restaurant)

        }

        AddressService.getSelectedAddress(object : AddressCallback{
            override fun onResponse(address: Address) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(errMessage: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        button_basket_confirm_order.setOnClickListener {

            progressBar.visibility = View.VISIBLE
            OrderService.makeOrder(object: OrderCallback {
                override fun onResponse(order: Order) {
                    runOnUiThread {
                        progressBar.visibility = View.INVISIBLE

                        this@BasketActivity.setResult(Activity.RESULT_OK)
                        this@BasketActivity.finish()
                    }

                }

                override fun onFailure(errMessage: String) {
                    runOnUiThread {
                        progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@BasketActivity, errMessage, Toast.LENGTH_LONG).show()
                    }

                }
            })
        }
    }
}
