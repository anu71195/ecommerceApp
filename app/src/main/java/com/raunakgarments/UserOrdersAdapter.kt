package com.raunakgarments

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class UserOrdersAdapter: RecyclerView.Adapter<UserOrdersAdapter.UserOrderViewHolder>() {

    fun populate(userOrdersRef: String, userOrdersActivity: UserOrdersActivity) {
        var userOrderFirebaseUtil = FirebaseUtil()
        userOrderFirebaseUtil.openFbReference(userOrdersRef+"/"+FirebaseAuth.getInstance().uid)

        userOrderFirebaseUtil.mDatabaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                d("UserOrdersAdapter", "populate-${Gson().toJson(snapshot.key)}")
                d("UserOrdersAdapter", "populate-${Gson().toJson(snapshot.value)}")
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    class UserOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleButton: Button = itemView.findViewById(R.id.activity_user_orders_adapter_user_orders_row_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_user_orders_adapter_user_orders_row, parent, false)
        return UserOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserOrderViewHolder, position: Int) {
        holder.titleButton.text = "hello ${position}"
    }

    override fun getItemCount(): Int {
        return 100
    }
}