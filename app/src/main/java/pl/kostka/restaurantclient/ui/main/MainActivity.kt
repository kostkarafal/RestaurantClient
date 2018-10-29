package pl.kostka.restaurantclient.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import pl.kostka.restaurantclient.ui.login.LoginFragment
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.service.IsLoggdInListener
import pl.kostka.restaurantclient.service.JwtService
import pl.kostka.restaurantclient.ui.login.RegisterFragment
import pl.kostka.restaurantclient.ui.menu.MenuFragment
import pl.kostka.restaurantclient.ui.orders.OrdersFragment
import pl.kostka.restaurantclient.ui.restaurants.RestaurantsFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        val menu = nav_view.menu

        val loginItem = menu.findItem(R.id.nav_login).setVisible(true)
        val logoutItem = menu.findItem(R.id.nav_log_out).setVisible(false)
        val myAccountItem = menu.findItem(R.id.nav_my_account).setVisible(false)
        val registerItem = menu.findItem(R.id.nav_register).setVisible(true)

        JwtService.setLoggedInListener(object : IsLoggdInListener {
            override fun onChange(isLoggedIn: Boolean) {
                when (isLoggedIn) {
                    true -> {
                        runOnUiThread {
                            loginItem.setVisible(false)
                            registerItem.setVisible(false)
                            logoutItem.setVisible(true)
                            myAccountItem.setVisible(true)
                            Snackbar.make(nav_view, getString(R.string.logged_in), Snackbar.LENGTH_LONG).show()
                        }
                    }
                    false -> {
                        runOnUiThread {
                            loginItem.setVisible(true)
                            registerItem.setVisible(true)
                            logoutItem.setVisible(false)
                            myAccountItem.setVisible(false)
                            Snackbar.make(nav_view, getString(R.string.logged_out), Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
        nav_view.setNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
            }
            R.id.nav_menu -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MenuFragment()).commit()
            }
            R.id.nav_restaurants -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, RestaurantsFragment()).commit()
            }
            R.id.nav_orders -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, OrdersFragment()).commit()
            }
            R.id.nav_login -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, LoginFragment()).commit()
            }
            R.id.nav_my_account -> {
                //TODO my account
            }
            R.id.nav_log_out -> {
                JwtService.logout()
              //  item.setVisible(false)
             //    Snackbar.make(nav_view, getString(R.string.logged_out), Snackbar.LENGTH_LONG).show()


                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
            }
            R.id.nav_register -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, RegisterFragment()).commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
