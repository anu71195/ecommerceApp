package com.raunakgarments.admin

import android.app.Activity
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.UserOrderAddresses
import com.raunakgarments.model.UserOrderProfile

//todo
class AdminOrdersAdapterByDates : RecyclerView.Adapter<AdminOrdersAdapterByDates.AdminOrderViewHolder>() {
    var userOrderProfileList: MutableList<UserOrderProfile> = ArrayList()
    private lateinit var adminOrdersActivity: Activity

    fun populate(userOrdersRef: String, adminOrdersActivity: AdminOrdersActivity) {
        this.adminOrdersActivity = adminOrdersActivity
        var userOrderAddressesFirebaseUtil = FirebaseUtil()
        userOrderAddressesFirebaseUtil.openFbReference("userOrderAddresses")

        userOrderAddressesFirebaseUtil.mDatabaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists()){
                    d("AdminOrdersAdapterByDates", "populate -> ${snapshot.key}")
                    var userOrderAddresses = UserOrderAddresses()
                    userOrderAddresses.timestamp = snapshot.value as String
                    userOrderAddresses.databaseAddress = snapshot.key as String
                    userOrderAddresses.databaseAddress = userOrderAddresses.databaseAddress.replace("**", "/")
                    d("AdminOrdersAdapterByDates", "populate -> ${Gson().toJson(userOrderAddresses)}")

                    var userOrderFirebaseUtil = FirebaseUtil()
                    userOrderFirebaseUtil.openFbReference(userOrderAddresses.databaseAddress)
                    userOrderFirebaseUtil.mDatabaseReference.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                //todo
                                d("AdminOrdersAdapterByDates", "populate -> ${snapshot.key}}")
                                d("AdminOrdersAdapterByDates", "populate -> ${snapshot.value}}")
                            } else {
                                d("AdminOrdersAdapterByDates", "populate -> snapshot does not exist}")

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })


                } else {
                    d("AdminOrdersAdapterByDates", "populate -> snapshot does not exist")
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })
    }

    class AdminOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var titleButton: Button =
            itemView.findViewById(R.id.activity_admin_orders_adapter_by_dates_admin_orders_row_Button)
        var informationTextView: TextView =
            itemView.findViewById(R.id.activity_admin_orders_adapter_by_dates_admin_orders_row_textViewInformation)
        var showDetailsOnInformationTextView = false
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_admin_orders_adapter_by_dates_admin_orders_row, parent, false)
        return AdminOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminOrderViewHolder, position: Int) {
        holder.titleButton.text = "${position}"
    }

    override fun getItemCount(): Int {
        return 10
    }
}