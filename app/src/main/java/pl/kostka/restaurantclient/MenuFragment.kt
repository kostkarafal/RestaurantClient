package pl.kostka.restaurantclient

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_menu.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.ProductService
import java.io.IOException

class MenuFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_menu, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_Menu)

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        ProductService.getMenu().enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                val menu = ProductService.gson.fromJson(body, Array<Product>::class.java).toList()
                activity?.runOnUiThread {
                    recyclerView.adapter = MenuAdapter(menu)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute")
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
    }


}