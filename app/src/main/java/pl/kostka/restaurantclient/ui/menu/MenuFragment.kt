package pl.kostka.restaurantclient.ui.menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.menu_category_row.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.ErrorResponse
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.model.enums.ProductType
import pl.kostka.restaurantclient.service.OrderService
import pl.kostka.restaurantclient.service.ProductService
import pl.kostka.restaurantclient.service.callback.ProductArrayCallback
import pl.kostka.restaurantclient.ui.basket.BasketActivity
import pl.kostka.restaurantclient.ui.orders.OrdersFragment


class MenuFragment: Fragment(){

    var menuList = emptyList<Product>()
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_menu, container, false)
        recyclerView = view.findViewById(R.id.recyclerView_Menu)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar_menu)
        recyclerView!!.layoutManager = LinearLayoutManager(view.context)

        val spinner = view.findViewById<Spinner>(R.id.spinner_menu_category)

        val spinnerMap = mapOf(ProductType.ALL to getString(R.string.all),
                                    ProductType.BURGER to getString(R.string.Burger),
                                    ProductType.PIZZA to getString(R.string.Pizza),
                                    ProductType.WRAP to getString(R.string.Wrap),
                                    ProductType.COFFEE_TEA to getString(R.string.coffee_tea),
                                    ProductType.COLD_DRINKS to getString(R.string.cold_drinks),
                                    ProductType.SALAD to getString(R.string.salad))

        spinner.adapter = ArrayAdapter<String>(activity!!, R.layout.my_spinner_item, spinnerMap.values.toTypedArray())

       spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
           override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               val type = spinnerMap.keys.elementAt(position)
               if(type == ProductType.ALL){
                   recyclerView!!.adapter = MenuAdapter(menuList, this@MenuFragment)

               } else {
                   val filteredMenuList = menuList.filter { it -> it.type == type }
                   recyclerView!!.adapter = MenuAdapter(filteredMenuList, this@MenuFragment)
               }
           }

           override fun onNothingSelected(parent: AdapterView<*>?) {
               TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
           }
       }



        ProductService.getMenu(object : ProductArrayCallback{
            override fun onResponse(response: Array<Product>) {
                activity?.runOnUiThread {
                    menuList = response.toList()
                    recyclerView!!.adapter = MenuAdapter(response.toList(), this@MenuFragment)
                    progressBar.visibility = View.INVISIBLE
                }
            }
            override fun onFailure(error: ErrorResponse) {
                activity?.runOnUiThread {
                    Toast.makeText(this@MenuFragment.context, error.getMsg(), Toast.LENGTH_LONG).show()
                }
                progressBar.visibility = View.INVISIBLE
            }
        })
        val floatingButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingButton.setOnClickListener {
            val intent = Intent(it.context, BasketActivity::class.java)

            startActivityForResult(intent, 2)
          //  view.context.(intent)
        }
        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Snackbar.make(this@MenuFragment.view!!, getString(R.string.AddedItemToBasket), Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) {
                        println("Canceling recently added product to basket")
                        if(OrderService.undoRecentlyAddedProduct()) {
                            Snackbar.make(this@MenuFragment.view!!, getString(R.string.canceled), Snackbar.LENGTH_LONG).show()
                        }
                    }.show()
        } else if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            activity?.runOnUiThread {
                fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, OrdersFragment())?.commit()
                Snackbar.make(view!!, "Utworzono nowe zamówienie!", Snackbar.LENGTH_LONG).show()
            }
        }
    }

}