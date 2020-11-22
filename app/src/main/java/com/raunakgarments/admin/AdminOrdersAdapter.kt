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
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.global.AdminOrderSingletonClass
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.Profile
import com.raunakgarments.model.UserOrderProfile
import com.raunakgarments.model.UserOrders

class AdminOrdersAdapter : RecyclerView.Adapter<AdminOrdersAdapter.AdminOrderViewHolder>() {

    var userOrderProfileList: MutableList<UserOrderProfile> = ArrayList()
    private lateinit var adminOrdersActivity: Activity

    fun populate(userOrdersRef: String, adminOrdersActivity: AdminOrdersActivity) {
        this.adminOrdersActivity = adminOrdersActivity

        var adminOrderFirebaseUtil = FirebaseUtil()
        adminOrderFirebaseUtil.openFbReference(userOrdersRef)

        adminOrderFirebaseUtil.mDatabaseReference.addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    d("AdminOrdersAdapter", "populate-${snapshot.key}")
                    d("AdminOrdersAdapter", "populate-${snapshot.value}")
                    var userOrderProfile = UserOrderProfile()
                    userOrderProfile.id = snapshot.key.toString()
                    userOrderProfile.userOrderList = snapshot.value as HashMap<String, UserOrders>
                    d("AdminOrdersAdapter", "populate-userOrderProfile - ${Gson().toJson(userOrderProfile)}")

                    var userProfileFirebaseUtil = FirebaseUtil()
                    userProfileFirebaseUtil.openFbReference("userProfile")

                    snapshot.key?.let {
                        userProfileFirebaseUtil.mDatabaseReference.child(it).addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()) {
                                    var userProfile = snapshot.getValue(Profile::class.java)
                                    if(userProfile != null) {
                                        userOrderProfile.userCurrentProfile = userProfile
                                    }
                                } else {
                                    d("AdminOrdersAdapter", "populate - snapshot does not exist")
                                }
                                d("AdminOrdersAdapter", "populate-userOrderProfile - ${Gson().toJson(userOrderProfile)}")
                                userOrderProfileList.add(userOrderProfile)
                                notifyItemInserted(userOrderProfileList.size - 1)
                            }

                            override fun onCancelled(error: DatabaseError) {}

                        })
                    }




                } else {
                    d("AdminOrdersAdapter", "populate-snapshot does not exist")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })

    }

    class AdminOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleButton: Button =
            itemView.findViewById(R.id.activity_admin_orders_adapter_admin_orders_row_Button)
        var informationTextView: TextView =
            itemView.findViewById(R.id.activity_admin_orders_adapter_admin_orders_row_textViewInformation)
        var showDetailsOnInformationTextView = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_admin_orders_adapter_admin_orders_row, parent, false)
        return AdminOrderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        d("AdminOrdersAdapter", "getItemCount-${userOrderProfileList.size}")
        return userOrderProfileList.size
    }

    override fun onBindViewHolder(holder: AdminOrderViewHolder, position: Int) {
        holder.titleButton.text = userOrderProfileList[position].userCurrentProfile.userName + " -> " +userOrderProfileList[position].id
        holder.informationTextView.text = getUserProfileShortText(userOrderProfileList, position)

        informationTextViewOnClickListener(holder, position)
        titleButtonOnClickListener(holder, position)
    }

    private fun informationTextViewOnClickListener(holder: AdminOrderViewHolder, position: Int) {
        holder.informationTextView.setOnClickListener {
            if (!holder.showDetailsOnInformationTextView) {
                holder.informationTextView.text = getUserProfileText(userOrderProfileList,position)
                holder.showDetailsOnInformationTextView = true
            } else {
                holder.informationTextView.text = getUserProfileShortText(userOrderProfileList, position)
                holder.showDetailsOnInformationTextView = false
            }
        }
    }

    private fun getUserProfileText(
        userOrderProfileList: MutableList<UserOrderProfile>,
        position: Int
    ): String {
        var detailedText = ""
        detailedText += "PROFILE\n"
        detailedText += "Id = ${userOrderProfileList[position].id} \n\n"
        detailedText += "Name = ${userOrderProfileList[position].userCurrentProfile.userName} \n\n"
        detailedText += "Address = ${userOrderProfileList[position].userCurrentProfile.address} \n\n"
        detailedText += "Email = ${userOrderProfileList[position].userCurrentProfile.email} \n\n"
        detailedText += "Number = +${userOrderProfileList[position].userCurrentProfile.areaPhoneCode + " " + userOrderProfileList[position].userCurrentProfile.number } \n\n"
        detailedText += "Pincode = ${userOrderProfileList[position].userCurrentProfile.pinCode}"

        return detailedText
    }

    private fun getUserProfileShortText(
        userOrderProfileList: MutableList<UserOrderProfile>,
        position: Int
    ): String {
        var shortText = ""

        shortText += "Email = ${userOrderProfileList[position].userCurrentProfile.email} \n\n"
        shortText += "Number = +${userOrderProfileList[position].userCurrentProfile.areaPhoneCode + " " + userOrderProfileList[position].userCurrentProfile.number } \n\n"
        shortText += "Pincode = ${userOrderProfileList[position].userCurrentProfile.pinCode}"

        return shortText
    }

    private fun titleButtonOnClickListener(holder: AdminOrderViewHolder, position: Int) {
        holder.titleButton.setOnClickListener {
            d("AdminOrdersAdapter", "titleButtonOnClickListener - titlebutton clicked")
            var intent = Intent(adminOrdersActivity, AdminUserOrdersActivity::class.java)
            AdminOrderSingletonClass.userOrderProfile = userOrderProfileList[position]
//            intent.putExtra("userOrderProfile", Gson().toJson(userOrderProfileList[position]))
            adminOrdersActivity.startActivity(intent)
        }
    }
}