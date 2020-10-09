package com.raunakgarments.admin

import android.app.Activity
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.raunakgarments.R
import com.raunakgarments.helper.FirebaseUtil

class AdminOrdersAdapter : RecyclerView.Adapter<AdminOrdersAdapter.AdminOrderViewHolder>()  {

    private lateinit var adminOrdersActivity: Activity

    fun populate(userOrdersRef: String, adminOrdersActivity: AdminOrdersActivity) {
        this.adminOrdersActivity = adminOrdersActivity

        var adminOrderFirebaseUtil = FirebaseUtil()
        adminOrderFirebaseUtil.openFbReference(userOrdersRef)

        adminOrderFirebaseUtil.mDatabaseReference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists()) {
                    d("AdminOrdersAdapter", "populate-${snapshot.key}")
                    d("AdminOrdersAdapter", "populate-${snapshot.value}")
                } else {
                    d("AdminOrdersAdapter", "populate-snapshot does not exist")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) { }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })

    }

    class AdminOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var titleButton: TextView =
            itemView.findViewById(R.id.activity_admin_orders_adapter_admin_orders_row_TextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_admin_orders_adapter_admin_orders_row, parent, false)
        return AdminOrderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: AdminOrderViewHolder, position: Int) {
        holder.titleButton.text = "Hello World ${position}"
    }
}