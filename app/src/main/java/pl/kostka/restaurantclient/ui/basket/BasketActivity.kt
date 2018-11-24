package pl.kostka.restaurantclient.ui.basket

import android.app.Activity
import android.content.Intent
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
import pl.kostka.restaurantclient.model.Restaurant
import pl.kostka.restaurantclient.service.OrderService
import pl.kostka.restaurantclient.service.UserService
import pl.kostka.restaurantclient.service.callback.AddressCallback
import pl.kostka.restaurantclient.service.callback.OrderCallback
import pl.kostka.restaurantclient.service.callback.RestaurantCallback
import pl.kostka.restaurantclient.ui.myaccount.AddressActivity
import pl.kostka.restaurantclient.ui.restaurants.RestaurantActivity

class BasketActivity : AppCompatActivity() {

    var deliveryAddress: Address? = null
    var selectedRestaurant: Restaurant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_basket)
        val totalPrice = findViewById<TextView>(R.id.textView_basket_total_price)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar_basket)
        progressBar.visibility = View.INVISIBLE
        val basket = OrderService.getBasket()
        radioButton_basket_delivery.isChecked = true


        if(basket.products.size == 0) {
            container_basket_summary.visibility = View.INVISIBLE
            button_basket_confirm_order.visibility = View.INVISIBLE
            textView_basket_empty.visibility = View.VISIBLE
        } else {
            textView_basket_empty.visibility = View.INVISIBLE
            totalPrice?.text = String.format("%.2f",basket.totalPrize)
            recyclerView.adapter = BasketAdapter(OrderService.getBasket(),this@BasketActivity)
        }

        getDeliveryAddress()

        radioButton_basket_delivery.setOnClickListener {
            if(deliveryAddress != null) {
                radioButton_basket_self_pickup.isChecked = false
                textView_basket_title.text = deliveryAddress!!.title
                textView_basket_addres_type.text = getString(R.string.delivery_address)
            } else {
                getDeliveryAddress()
            }
        }



        radioButton_basket_self_pickup.setOnClickListener {
            if(selectedRestaurant != null) {
                radioButton_basket_delivery.isChecked = false
                textView_basket_title.text = selectedRestaurant!!.name
                textView_basket_addres_type.text = getString(R.string.yours_restaurant)
            } else {
                getRestaurantAddress()
            }
        }

        button_basket_change.setOnClickListener {
            if( radioButton_basket_delivery.isChecked) {
                val intent = Intent(it.context, AddressActivity::class.java)
                intent.putExtra("selectedAddressId", deliveryAddress?.id)
                startActivityForResult(intent, 1)
            } else if (radioButton_basket_self_pickup.isChecked) {
                val intent = Intent(it.context, RestaurantActivity::class.java)
                intent.putExtra("selectedAddressId", deliveryAddress?.id)
                startActivityForResult(intent, 2)
            }
        }


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

    private fun getDeliveryAddress() {
        UserService.getSelectedAddress(object : AddressCallback{
            override fun onResponse(address: Address) {
                deliveryAddress = address
                runOnUiThread {
                    radioButton_basket_self_pickup.isChecked = false
                    textView_basket_title.text = address.title
                    textView_basket_addres_type.text = getString(R.string.delivery_address)
                }
            }

            override fun onFailure(errMessage: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }

    private fun getRestaurantAddress() {
        UserService.getSelectedRestaurant(object : RestaurantCallback{
            override fun onResponse(restaurant: Restaurant) {
                selectedRestaurant = restaurant
                runOnUiThread {
                    radioButton_basket_delivery.isChecked = false
                    textView_basket_title.text = restaurant.name
                    textView_basket_addres_type.text = getString(R.string.yours_restaurant)
                }
            }

            override fun onFailure(errMessage: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            getDeliveryAddress()
        } else if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            getRestaurantAddress()
        }
    }

}
