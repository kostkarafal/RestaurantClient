package pl.kostka.restaurantclient.ui.myaccount

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.address_row.view.*

import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.model.ErrorResponse
import pl.kostka.restaurantclient.service.AddressService
import pl.kostka.restaurantclient.service.UserService
import pl.kostka.restaurantclient.service.callback.AddressArrayCallback
import pl.kostka.restaurantclient.service.callback.AddressCallback
import pl.kostka.restaurantclient.service.callback.VoidCallback
import pl.kostka.restaurantclient.service.listener.OnChangeListener
import kotlin.properties.Delegates

class AddressAdapter(val addressList: List<Address>, val activity: Activity,private var lastSelectedPosition: Int,val isAddressSupported: Boolean,val addressListListener: OnChangeListener): RecyclerView.Adapter<AddresViewHolder>() {

    var addresses: List<Address>  by Delegates.observable(
            initialValue = addressList,
            onChange = {
                _, _, _ ->
                addressListListener.onChange(addresses.size)
            } )

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
            holder.view.progressBar_address_item.visibility = View.VISIBLE
            AddressService.deleteAddress(address.id!!, object : VoidCallback {
                override fun onResponse() {
                    if(isAddressSupported) {
                        AddressService.getAddresses(object : AddressArrayCallback {
                            override fun onResponse(response: Array<Address>) {
                                this@AddressAdapter.activity.runOnUiThread {
                                    addresses = response.toList()
                                    if (lastSelectedPosition == position)
                                        lastSelectedPosition = -1
                                    notifyDataSetChanged()
                                    holder.view.progressBar_address_item.visibility = View.INVISIBLE

                                }
                            }

                            override fun onFailure(error: ErrorResponse) {
                                this@AddressAdapter.activity.runOnUiThread {
                                    Toast.makeText(this@AddressAdapter.activity, error.getMsg(), Toast.LENGTH_LONG).show()
                                    holder.view.progressBar_address_item.visibility = View.INVISIBLE
                                }
                            }
                        })
                    } else {
                        AddressService.getUnsupportedAddresses(object : AddressArrayCallback {
                            override fun onResponse(response: Array<Address>) {
                                this@AddressAdapter.activity.runOnUiThread {
                                    addresses = response.toList()
                                    if (lastSelectedPosition == position)
                                        lastSelectedPosition = -1
                                    notifyDataSetChanged()
                                    holder.view.progressBar_address_item.visibility = View.INVISIBLE
                                }
                            }

                            override fun onFailure(error: ErrorResponse) {
                                this@AddressAdapter.activity.runOnUiThread {
                                    Toast.makeText(this@AddressAdapter.activity, error.getMsg(), Toast.LENGTH_LONG).show()
                                    holder.view.progressBar_address_item.visibility = View.INVISIBLE
                                }
                            }
                        })
                    }
                }

                override fun onFailure(error: ErrorResponse) {
                    this@AddressAdapter.activity.runOnUiThread {
                        Toast.makeText(this@AddressAdapter.activity, error.getMsg(), Toast.LENGTH_LONG).show()
                        holder.view.progressBar_address_item.visibility = View.INVISIBLE
                    }
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
                UserService.selectDeliveryAddress(address.id!!, object : AddressCallback {
                    override fun onResponse(response: Address) {

                    }

                    override fun onFailure(error: ErrorResponse) {
                        this@AddressAdapter.activity.runOnUiThread {
                            Toast.makeText(this@AddressAdapter.activity, error.getMsg(), Toast.LENGTH_LONG).show()
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