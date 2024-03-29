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
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.Profile
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
                    if (snapshot.key.toString() != "id" && snapshot.key.toString() != "userOrderProfile") {
                        d("UserOrdersAdapter", "my snapshot-${snapshot}")
                        var userOrders = snapshot.getValue(UserOrders::class.java)
                        d("UserOrdersAdapter", "populate-${Gson().toJson(userOrders)}")
                        if (userOrders != null) {

                            if (userOrders.id == "") {
                                userOrders.id = snapshot.key!!
                                userOrderFirebaseUtil.mDatabaseReference.child(userOrders.id)
                                    .child("id").setValue(userOrders.id)
                            }

                            if (userOrders.userOrderProfile.pinCode == "") {
                                addOrderToListAndNotify(userOrders)
                                getAndUpdateUserProfile(
                                    userOrders,
                                    userOrdersList.size,
                                    snapshot.key!!
                                )
                            } else {
                                addOrderToListAndNotify(userOrders)
                            }
                        } else {
                            d("UserOrdersAdapter", "populate-userOrders is null")
                        }
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

    private fun getAndUpdateUserProfile(
        userOrders: UserOrders,
        userOrdersListIndex: Int,
        orderId: String
    ) {
        var userOrderFirebaseUtil = FirebaseUtil()
        userOrderFirebaseUtil.openFbReference("userOrders" + "/" + FirebaseAuth.getInstance().uid)

        var userProfileFirebaseUtil = FirebaseUtil()
        userProfileFirebaseUtil.openFbReference("userProfile")
        userProfileFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        d("UserOrdersAdapter", "getAndUpdateUserProfile - snapshot exists")
                        var userProfile = snapshot.getValue(Profile::class.java)
                        if (userProfile != null) {
                            userOrders.userOrderProfile = userProfile
                            userOrderFirebaseUtil.mDatabaseReference.child(orderId)
                                .setValue(userOrders)
                            editOrderToListAndNotify(userOrders, userOrdersListIndex)
                        } else {
                            d("UserOrdersAdapter", "getAndUpdateUserProfile - userProfile is null")
                        }
                    } else {
                        d("UserOrdersAdapter", "getAndUpdateUserProfile - snapshot does not exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    private fun editOrderToListAndNotify(userOrders: UserOrders, userOrdersListIndex: Int) {
        userOrdersList[userOrdersListIndex] = userOrders
        notifyDataSetChanged()
    }

    private fun addOrderToListAndNotify(userOrders: UserOrders) {
        userOrdersList.add(userOrders)
        notifyItemInserted(userOrdersList.size - 1)
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
            "Total Cost = \u20B9" + userOrdersList[position].totalCost + "\n" + "Delivery Status = " + userOrdersList[position].deliveryStatus + "\n" + "Order Status = " + userOrdersList[position].orderStatus + "\n" + "Total Items = " + userOrdersList[position].orders.size
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
                holder.informationTextView.text = getDetailedText(userOrdersList, position)
                holder.showDetailsOnInformationTextView = true
            } else {
                holder.informationTextView.text =
                    "Total Cost = \u20B9" + userOrdersList[position].totalCost + "\n" + "Delivery Status = " + userOrdersList[position].deliveryStatus + "\n" + "Order Status = " + userOrdersList[position].orderStatus + "\n" + "Total Items = " + userOrdersList[position].orders.size
                holder.showDetailsOnInformationTextView = false
            }
        }
    }

    private fun getDetailedText(
        userOrdersList: MutableList<UserOrders>,
        position: Int
    ): CharSequence? {
        var detailedText =
            "SUMMARY \nTotal Cost = \u20B9" + userOrdersList[position].totalCost + "\n" + "Delivery Status = " + userOrdersList[position].deliveryStatus + "\n" + "Order Status = " + userOrdersList[position].orderStatus + "\n" + "Total Items = " + userOrdersList[position].orders.size + "\n\n\n"
        detailedText += "ORDERS\n"
        detailedText += getOrderText(userOrdersList, position)
        detailedText += "PROFILE\n"
        detailedText += "Name = ${userOrdersList[position].userOrderProfile.userName} \n\n"
        detailedText += "Address = ${userOrdersList[position].userOrderProfile.address} \n\n"
        detailedText += "Email = ${userOrdersList[position].userOrderProfile.email} \n\n"
        detailedText += "Number = +${userOrdersList[position].userOrderProfile.areaPhoneCode + " " + userOrdersList[position].userOrderProfile.number} \n\n"
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
            if (orderedProduct.value.deliveryStatus == "") {
                orderedProduct.value.deliveryStatus = userOrdersList[position].deliveryStatus
                userOrderFirebaseUtil.mDatabaseReference.child(userOrdersList[position].id)
                    .child("orders").child(orderedProduct.value.id).child("deliveryStatus")
                    .setValue(userOrdersList[position].deliveryStatus)
            }

            if (orderedProduct.value.orderStatus == "") {
                orderedProduct.value.orderStatus = userOrdersList[position].orderStatus
                userOrderFirebaseUtil.mDatabaseReference.child(userOrdersList[position].id)
                    .child("orders").child(orderedProduct.value.id).child("orderStatus")
                    .setValue(userOrdersList[position].orderStatus)
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