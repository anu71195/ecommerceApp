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
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.UserOrderProfile
import com.raunakgarments.model.UserOrders

class AdminUserOrdersAdapter :
    RecyclerView.Adapter<AdminUserOrdersAdapter.AdminUserOrderViewHolder>() {

    var userOrdersList: MutableList<UserOrders> = ArrayList()
    private lateinit var adminUserOrdersActivity: Activity
    private lateinit var userOrderProfile: UserOrderProfile

    fun populate(
        userOrdersRef: String,
        intent: Intent,
        adminUserOrdersActivity: AdminUserOrdersActivity
    ) {
        this.adminUserOrdersActivity = adminUserOrdersActivity
        this.userOrderProfile =
            Gson().fromJson<UserOrderProfile>(intent.getStringExtra("userOrderProfile"), UserOrderProfile::class.java)

        d("AdminUserOrdersAdapter", "populate-${Gson().toJson(userOrderProfile)}")

        var userOrderFirebaseUtil = FirebaseUtil()
        userOrderFirebaseUtil.openFbReference(userOrdersRef + "/" + userOrderProfile.id)

        userOrderFirebaseUtil.mDatabaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    var userOrders = snapshot.getValue(UserOrders::class.java)
                    d("AdminUserOrdersAdapter", "populate-${Gson().toJson(userOrders)}")
                    if (userOrders != null) {
                        userOrdersList.add(userOrders)
                        notifyItemInserted(userOrdersList.size - 1)
                    } else {
                        d("AdminUserOrdersAdapter", "populate-userOrders is null")
                    }
                } else {
                    d("AdminUserOrdersAdapter", "populate-snapshot does not exist")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })


    }

    class AdminUserOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var titleButton: Button =
            itemView.findViewById(R.id.activity_admin_user_orders_adapter_user_orders_row_button)
        var informationTextView: TextView =
            itemView.findViewById(R.id.activity_admin_user_orders_adapter_user_orders_row_textViewInformation)
        var showDetailsOnInformationTextView = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUserOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_admin_user_orders_adapter_user_orders_row, parent, false)
        return AdminUserOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminUserOrderViewHolder, position: Int) {
        holder.titleButton.text = userOrdersList[position].dateStamp
        holder.informationTextView.text =
            "Total Cost = \u20B9" + userOrdersList[position].totalCost + "\n" + "Delivery Status = " + userOrdersList[position].deliveryStatus
        titleButtonOnClickListener(holder, position)
        informationTextViewOnClickListener(holder, position)
    }

    private fun titleButtonOnClickListener(holder: AdminUserOrderViewHolder, position: Int) {

        holder.titleButton.setOnClickListener {
            d("AdminUserOrdersAdapter", "titleButtonOnClickListener - titlebutton clicked")
            var intent = Intent(adminUserOrdersActivity, AdminUserOrderDetailsActivity::class.java)
            intent.putExtra("userOrders", Gson().toJson(userOrdersList[position]))
            adminUserOrdersActivity.startActivity(intent)
        }
    }

    private fun informationTextViewOnClickListener(holder: AdminUserOrderViewHolder, position: Int) {

        holder.informationTextView.setOnClickListener {
            if (!holder.showDetailsOnInformationTextView) {
                holder.informationTextView.text =
                    "Total Cost = \u20B9" + userOrdersList[position].totalCost + "\n" + "Delivery Status = " + userOrdersList[position].deliveryStatus + "\n" + "Order Status = " + userOrdersList[position].orderStatus + "\n" + "Total Items = " + userOrdersList[position].orders.size
                holder.showDetailsOnInformationTextView = true
            } else {
                holder.informationTextView.text =
                    "Total Cost = \u20B9" + userOrdersList[position].totalCost + "\n" + "Delivery Status = " + userOrdersList[position].deliveryStatus
                holder.showDetailsOnInformationTextView = false
            }
        }
    }

    override fun getItemCount(): Int {
        return userOrdersList.size
    }
}