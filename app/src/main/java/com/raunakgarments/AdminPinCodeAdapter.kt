package com.raunakgarments

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.raunakgarments.model.Product

class AdminPinCodeAdapter : RecyclerView.Adapter<AdminPinCodeAdapter.PinCodeViewHolder>() {

    var pinCodeArray: MutableList<String> = ArrayList()
    private lateinit var childEventListener: ChildEventListener

    fun populate(ref: String) {
        var firebaseUtil: FirebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference(ref)
        d("pinCode", ref)
        childEventListener = object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                d("pinCode", snapshot.key.toString())
                d("pinCode", snapshot.value.toString())
                d("pinCode", "pincode")
                pinCodeArray.add(snapshot.key.toString())
                notifyItemInserted(pinCodeArray.size-1)
            }
        }
        firebaseUtil.mDatabaseReference.addChildEventListener(childEventListener)
    }

    class PinCodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.adapter_admin_pin_code_adapter_pin_code_row_textView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PinCodeViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_admin_pin_code_adapter_pin_code_row, parent, false)
        return PinCodeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pinCodeArray.count()
    }


    override fun onBindViewHolder(holder: PinCodeViewHolder, position: Int) {
        holder.title.text = pinCodeArray[position]
    }
}