package com.raunakgarments

import android.content.Context
import android.content.Intent
import android.media.Image
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.raunakgarments.model.Product

class AdminPinCodeAdapter : RecyclerView.Adapter<AdminPinCodeAdapter.PinCodeViewHolder>() {

    var pinCodeArray: MutableList<String> = ArrayList()
    private lateinit var childEventListener: ChildEventListener
    lateinit var context: Context
    var firebaseUtil: FirebaseUtil = FirebaseUtil()

    fun populate(ref: String, context: Context) {
        firebaseUtil.openFbReference(ref)
        this.context = context
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
        var deletePinCodeButton: Button = itemView.findViewById(R.id.adapter_admin_pin_code_adapter_pin_code_row_deletePinCodeButton)
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
        holder.deletePinCodeButton.setOnClickListener {
            d("delete tag", "Deletetag")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Are you sure you want to remove pincode = ${pinCodeArray[position]} ?")
            builder.setMessage("")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                Toast.makeText(context, "clicked yes", Toast.LENGTH_LONG).show()
                firebaseUtil.mDatabaseReference.child(pinCodeArray[position]).removeValue()
                pinCodeArray.removeAt(position)
                notifyDataSetChanged()
            }
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                Toast.makeText(
                    context,
                    "clicked cancel\n operation cancel",
                    Toast.LENGTH_LONG
                ).show()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                Toast.makeText(context, "clicked No", Toast.LENGTH_LONG).show()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}