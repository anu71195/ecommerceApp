package com.raunakgarments.admin

import android.app.Activity
import android.content.Intent
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.UserOrderAddresses
import com.raunakgarments.model.UserOrders

class AdminOrdersAdapterByDates : RecyclerView.Adapter<AdminOrdersAdapterByDates.AdminOrderViewHolder>() {
    var userOrdersList: MutableList<UserOrders> = ArrayList()
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
                                var userOrders = snapshot.getValue(UserOrders::class.java)
                                if (userOrders != null) {
                                    userOrdersList.add(userOrders)
                                    userOrdersList.sortBy { it.timeStamp }
                                    notifyItemInserted(userOrdersList.size - 1)
                                }
                                d("AdminOrdersAdapterByDates", "populate -> ${Gson().toJson(userOrders)}}")
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
        holder.titleButton.text = userOrdersList[position].userOrderProfile.userName +" -> " + userOrdersList[position].userOrderProfile.id + "\n" + userOrdersList[position].dateStamp + " -> " +userOrdersList[position].timeStamp
        holder.informationTextView.text =
            "Total Cost = \u20B9" + userOrdersList[position].totalCost + "\n" + "Delivery Status = " + userOrdersList[position].deliveryStatus + "\n" + "Order Status = " + userOrdersList[position].orderStatus + "\n" + "Total Items = " + userOrdersList[position].orders.size
        titleButtonOnClickListener(holder, position)
        informationTextViewOnClickListener(holder, position)

    }

    private fun titleButtonOnClickListener(holder: AdminOrderViewHolder, position: Int) {

        holder.titleButton.setOnClickListener {
            d("AdminUserOrdersAdapter", "titleButtonOnClickListener - titlebutton clicked")
            var intent = Intent(adminOrdersActivity, AdminUserOrderDetailsActivity::class.java)
            intent.putExtra("userOrders", Gson().toJson(userOrdersList[position]))
            adminOrdersActivity.startActivity(intent)
        }
    }

    private fun informationTextViewOnClickListener(holder: AdminOrderViewHolder, position: Int) {

        holder.informationTextView.setOnClickListener {
            if (!holder.showDetailsOnInformationTextView) {
                holder.informationTextView.text = getDetailedText(userOrdersList, position)
                holder.showDetailsOnInformationTextView = true
            } else {
                holder.informationTextView.text =
                    "Total Cost = \u20B9" + userOrdersList[position].totalCost + "\n" + "Delivery Status = " + userOrdersList[position].deliveryStatus + "\n" + "Order Status = " + userOrdersList[position].orderStatus + "\n" + "Total Items = " + userOrdersList[position].orders.size
                holder.showDetailsOnInformationTextView = false
            }
        }
    }

    private fun getDetailedText(userOrdersList: MutableList<UserOrders>, position: Int): CharSequence? {

        var detailedText = "SUMMARY \nTotal Cost = \u20B9" + userOrdersList[position].totalCost + "\n" + "Delivery Status = " + userOrdersList[position].deliveryStatus + "\n" + "Order Status = " + userOrdersList[position].orderStatus + "\n" + "Total Items = " + userOrdersList[position].orders.size +"\n\n\n"
        detailedText += "ORDERS\n"
        detailedText += getOrderText(userOrdersList, position)
        detailedText += "PROFILE\n"
        detailedText += "Name = ${userOrdersList[position].userOrderProfile.userName} \n\n"
        detailedText += "Address = ${userOrdersList[position].userOrderProfile.address} \n\n"
        detailedText += "Email = ${userOrdersList[position].userOrderProfile.email} \n\n"
        detailedText += "Number = +${userOrdersList[position].userOrderProfile.areaPhoneCode + " " + userOrdersList[position].userOrderProfile.number } \n\n"
        detailedText += "Pincode = ${userOrdersList[position].userOrderProfile.pinCode}"

        return detailedText
    }

    private fun getOrderText(userOrdersList: MutableList<UserOrders>, position: Int): String {

        var userOrderFirebaseUtil = FirebaseUtil()
        userOrderFirebaseUtil.openFbReference("userOrders" + "/" + FirebaseAuth.getInstance().uid)

        var orderText = ""

        for (orderedProduct in userOrdersList[position].orders) {
            orderText += "${orderedProduct.value.title}\n"
            orderText += "₹" + orderedProduct.value.price.toString() + " X " + orderedProduct.value.quantity + " = ₹" + orderedProduct.value.totalPrice + "\n"
            if(orderedProduct.value.deliveryStatus == "") {
                orderedProduct.value.deliveryStatus = userOrdersList[position].deliveryStatus
                userOrderFirebaseUtil.mDatabaseReference.child(userOrdersList[position].id).child("orders").child(orderedProduct.value.id).child("deliveryStatus").setValue(userOrdersList[position].deliveryStatus)
            }

            if(orderedProduct.value.orderStatus == "") {
                orderedProduct.value.orderStatus = userOrdersList[position].orderStatus
                userOrderFirebaseUtil.mDatabaseReference.child(userOrdersList[position].id).child("orders").child(orderedProduct.value.id).child("orderStatus").setValue(userOrdersList[position].orderStatus)
            }

            orderText += "Delivery Status = ${orderedProduct.value.deliveryStatus}\n"
            orderText += "Order Status = ${orderedProduct.value.orderStatus}\n\n"
        }
        orderText += "\n"
        return orderText
    }

    override fun getItemCount(): Int {
        return userOrdersList.size
    }
}