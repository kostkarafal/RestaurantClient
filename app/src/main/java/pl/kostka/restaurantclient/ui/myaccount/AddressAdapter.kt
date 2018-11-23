package pl.kostka.restaurantclient.ui.myaccount

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.address_row.view.*

import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.service.AddressService
import pl.kostka.restaurantclient.service.callback.AddressCallback
import pl.kostka.restaurantclient.service.callback.AddressListCallback
import pl.kostka.restaurantclient.service.callback.VoidCallback

class AddressAdapter(var addresses: List<Address>, val activity: Activity,private var lastSelectedPosition: Int,val isAddressSupported: Boolean): RecyclerView.Adapter<AddresViewHolder>() {


    override fun getItemCount(): Int {
        return addresses.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddresViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AddresViewHolder(layoutInflater.inflate(R.layout.address_row, parent, false))
    }

    override fun onBindViewHolder(holder: AddresViewHolder, position: Int) {
            val address = addresses.get(position)
        holder.view.textView_address_title?.text = address.title

        var addressString = "${address.street} ${address.buildingNumber}"
        if(address.apartmentNumber != null)
            addressString += "/${address.apartmentNumber!!}"
        addressString += ", ${address.city}"

        holder.view.textView_addres?.text = addressString


        holder.view.imageButton_address_delete.setOnClickListener {
            AddressService.deleteAddress(address.id!!, object : VoidCallback {
                override fun onResponse() {
                   AddressService.getAddresses(object : AddressListCallback {
                       override fun onResponse(address: List<Address>) {
                           this@AddressAdapter.activity.runOnUiThread {
                               addresses = address
                               if(lastSelectedPosition == position)
                                   lastSelectedPosition = -1
                               notifyDataSetChanged()
                           }
                       }

                       override fun onFailure(errMessage: String) {
                           TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                       }
                   })
                }

                override fun onFailure(errMessage: String) {

                }
            })
        }

        holder.view.imageButton_address_edit.setOnClickListener {
            val intent = Intent(holder.view.context, NewAddressActivity::class.java)
            intent.putExtra("Address",address)
            this@AddressAdapter.activity.startActivityForResult(intent, 1)
        }

        if(isAddressSupported) {
            holder.view.radioButton_address.isChecked = lastSelectedPosition == position
            holder.view.radioButton_address.setOnClickListener {
                lastSelectedPosition = position
                notifyDataSetChanged()
                AddressService.selectMainAddress(address.id!!, object : AddressCallback {
                    override fun onResponse(address: Address) {

                    }

                    override fun onFailure(errMessage: String) {
                        this@AddressAdapter.activity.runOnUiThread {
                            Toast.makeText(this@AddressAdapter.activity, errMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        } else {
            holder.view.radioButton_address.isEnabled = false
        }

    }

}

class AddresViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    init {

    }
}