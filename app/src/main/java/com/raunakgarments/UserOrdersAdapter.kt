package com.raunakgarments

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
import com.google.gson.Gson
import com.raunakgarments.model.UserOrders

class UserOrdersAdapter : RecyclerView.Adapter<UserOrdersAdapter.UserOrderViewHolder>() {

    var userOrdersList: MutableList<UserOrders> = ArrayList()
    private lateinit var userOrdersActivity: Activity

    fun populate(userOrdersRef: String, userOrdersActivity: UserOrdersActivity) {
        this.userOrdersActivity = userOrdersActivity

        var userOrderFirebaseUtil = FirebaseUtil()
        userOrderFirebaseUtil.openFbReference(userOrdersRef + "/" + FirebaseAuth.getInstance().uid)

        userOrderFirebaseUtil.mDatabaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    var userOrders = snapshot.getValue(UserOrders::class.java)
                    d("UserOrdersAdapter", "populate-${Gson().toJson(userOrders)}")
                    if (userOrders != null) {
                        userOrdersList.add(userOrders)
                        notifyItemInserted(userOrdersList.size - 1)
                    } else {
                        d("UserOrdersAdapter", "populate-userOrders is null")
                    }
                } else {
                    d("UserOrdersAdapter", "populate-snapshot does not exist")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })
    }

    class UserOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleButton: Button =
            itemView.findViewById(R.id.activity_user_orders_adapter_user_orders_row_button)
        var informationTextView: TextView =
            itemView.findViewById(R.id.activity_user_orders_adapter_user_orders_row_textViewInformation)
        var showDetailsOnInformationTextView = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_user_orders_adapter_user_orders_row, parent, false)
        return UserOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserOrderViewHolder, position: Int) {
        holder.titleButton.text = userOrdersList[position].dateStamp
        holder.informationTextView.text =
            "total Cost = " + userOrdersList[position].totalCost + "\n" + "delivery status = " + userOrdersList[position].deliveryStatus
        titleButtonOnClickListener(holder, position)
        informationTextViewOnClickListener(holder, position)
    }

    private fun titleButtonOnClickListener(holder: UserOrderViewHolder, position: Int) {
        holder.titleButton.setOnClickListener {
            d("UserOrdersAdapter", "titleButtonOnClickListener - titlebutton clicked")
            var intent = Intent(userOrdersActivity, UserOrderDetailsActivity::class.java)
            intent.putExtra("userOrders", Gson().toJson(userOrdersList[position]))
            userOrdersActivity.startActivity(intent)
        }
    }

    private fun informationTextViewOnClickListener(holder: UserOrderViewHolder, position: Int) {

        holder.informationTextView.setOnClickListener {
            if (!holder.showDetailsOnInformationTextView) {
                holder.informationTextView.text =
                    "total Cost = " + userOrdersList[position].totalCost + "\n" + "delivery status = " + userOrdersList[position].deliveryStatus + "\n" + "orderStatus = " + userOrdersList[position].orderStatus + "\n" + "Total Items = " + userOrdersList[position].orders.size
                holder.showDetailsOnInformationTextView = true
            } else {
                holder.informationTextView.text =
                    "total Cost = " + userOrdersList[position].totalCost + "\n" + "delivery status = " + userOrdersList[position].deliveryStatus
                holder.showDetailsOnInformationTextView = false
            }
        }

    }

    override fun getItemCount(): Int {
        return userOrdersList.size
    }
}