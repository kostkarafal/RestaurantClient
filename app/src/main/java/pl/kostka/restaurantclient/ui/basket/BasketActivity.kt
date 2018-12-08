package pl.kostka.restaurantclient.ui.basket

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import pl.kostka.restaurantclient.model.ErrorResponse
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
        radioButton_basket_delivery.isChecked = false
        radioButton_basket_self_pickup.isChecked = false


        if(basket.products.isEmpty()) {
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
            textView_basket_addres_type.text = getString(R.string.delivery_address)

            if(deliveryAddress != null) {
                textView_basket_title.text = deliveryAddress!!.title
            } else {
                getDeliveryAddress(progressBar_basket)
            }
        }



        radioButton_basket_self_pickup.setOnClickListener {
            radioButton_basket_delivery.isChecked = false
            textView_basket_addres_type.text = getString(R.string.yours_restaurant)
            if(selectedRestaurant != null) {
                textView_basket_title.text = selectedRestaurant!!.name
            } else {
                getRestaurantAddress(progressBar_basket)
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

            if (radioButton_basket_self_pickup.isChecked) {
                OrderService.makeSelfPickupOrder(selectedRestaurant!!.id, object : OrderCallback {
                    override fun onResponse(response: Order) {
                        runOnUiThread {
                            progressBar.visibility = View.INVISIBLE

                            this@BasketActivity.setResult(Activity.RESULT_OK)
                            this@BasketActivity.finish()
                        }

                    }

                    override fun onFailure(error: ErrorResponse) {
                        runOnUiThread {
                            progressBar.visibility = View.INVISIBLE
                            Toast.makeText(this@BasketActivity, error.getMsg(), Toast.LENGTH_LONG).show()
                        }

                    }
                })
            } else if (radioButton_basket_delivery.isChecked) {
                OrderService.makeDeliveryOrder(deliveryAddress!!.id!!, object: OrderCallback {
                    override fun onResponse(response: Order) {
                        runOnUiThread {
                            progressBar.visibility = View.INVISIBLE

                            this@BasketActivity.setResult(Activity.RESULT_OK)
                            this@BasketActivity.finish()
                        }
                    }

                    override fun onFailure(error: ErrorResponse) {
                        runOnUiThread {
                            progressBar.visibility = View.INVISIBLE
                            Toast.makeText(this@BasketActivity, error.getMsg(), Toast.LENGTH_LONG).show()
                        }                    }
                })
            } else {
                Snackbar.make(basket_view, getString(R.string.select_pickup_option), Snackbar.LENGTH_LONG ).show()
                progressBar.visibility = View.INVISIBLE

            }
        }
    }

    private fun getDeliveryAddress(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        UserService.getSelectedAddress(object : AddressCallback{
            override fun onResponse(response: Address) {
                deliveryAddress = response
                runOnUiThread {
                    progressBar.visibility = View.INVISIBLE
                    radioButton_basket_self_pickup.isChecked = false
                    textView_basket_title.text = response.title
                }
            }

            override fun onFailure(error: ErrorResponse) {
                runOnUiThread {
                    progressBar.visibility = View.INVISIBLE
                    if(error.status == 406) {
                        val intent = Intent(this@BasketActivity, AddressActivity::class.java)
                        startActivityForResult(intent, 1)
                    } else {
                        Toast.makeText(this@BasketActivity, error.getMsg(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

    }

    private fun getRestaurantAddress(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE

        UserService.getSelectedRestaurant(object : RestaurantCallback{
            override fun onResponse(response: Restaurant) {
                selectedRestaurant = response
                runOnUiThread {
                    progressBar.visibility = View.INVISIBLE
                    radioButton_basket_delivery.isChecked = false
                    textView_basket_title.text = response.name
                }
            }

            override fun onFailure(error: ErrorResponse) {
                runOnUiThread {
                    progressBar.visibility = View.INVISIBLE
                    if(error.status == 406) {
                        val intent = Intent(this@BasketActivity, RestaurantActivity::class.java)
                        startActivityForResult(intent, 2)
                    } else {
                        Toast.makeText(this@BasketActivity, error.getMsg(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                getDeliveryAddress(progressBar_basket)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                radioButton_basket_delivery.isChecked = false
            }
        } else if (requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                getRestaurantAddress(progressBar_basket)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                radioButton_basket_self_pickup.isChecked = false
            }
        }
    }

}
