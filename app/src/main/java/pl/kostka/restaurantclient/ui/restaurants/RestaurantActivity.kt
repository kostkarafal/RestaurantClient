package pl.kostka.restaurantclient.ui.restaurants

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import pl.kostka.restaurantclient.R

class RestaurantActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        supportFragmentManager.addOnBackStackChangedListener {
            println("test")
        }
    }

}