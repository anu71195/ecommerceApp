package com.raunakgarments

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.raunakgarments.model.UserOrders

class UserOrdersAdapter: RecyclerView.Adapter<UserOrdersAdapter.UserOrderViewHolder>() {

    var userOrdersList: MutableList<UserOrders> = ArrayList()

    fun populate(userOrdersRef: String, userOrdersActivity: UserOrdersActivity) {
        var userOrderFirebaseUtil = FirebaseUtil()
        userOrderFirebaseUtil.openFbReference(userOrdersRef+"/"+FirebaseAuth.getInstance().uid)

        userOrderFirebaseUtil.mDatabaseReference.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists()) {
                    var userOrders = snapshot.getValue(UserOrders::class.java)
                    d("UserOrdersAdapter", "populate-${Gson().toJson(userOrders)}")
                    if (userOrders != null) {
                        userOrdersList.add(userOrders)
                        notifyItemInserted(userOrdersList.size-1)
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
//        userOrderFirebaseUtil.mDatabaseReference.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                d("UserOrdersAdapter", "populate-${Gson().toJson(snapshot.key)}")
//                d("UserOrdersAdapter", "populate-${Gson().toJson(snapshot.value)}")
//                if(snapshot.exists()) {
//                    var userOrders = snapshot.getValue(UserOrders::class.java)
//                    d("UserOrdersAdapter", "populate-${Gson().toJson(userOrders)}")
//                } else {
//                    d("UserOrdersAdapter", "populate-snapshot does not exist")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//
//        })
    }

    class UserOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleButton: Button = itemView.findViewById(R.id.activity_user_orders_adapter_user_orders_row_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_user_orders_adapter_user_orders_row, parent, false)
        return UserOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserOrderViewHolder, position: Int) {
        holder.titleButton.text = userOrdersList[position].orders.size.toString()
    }

    override fun getItemCount(): Int {
        return userOrdersList.size
    }
}