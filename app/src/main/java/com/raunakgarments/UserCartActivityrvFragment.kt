package com.raunakgarments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_user_cart_activity_rv.*

class UserCartActivityrvFragment(context: Context) : Fragment() {

    var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAuth = FirebaseAuth.getInstance()
        this.userId = mFirebaseAuth.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_cart_activity_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvProductsAdmin = view.findViewById<RecyclerView>(R.id.fragment_user_cart_activity_rv)
        val totalCostView = view.findViewById<TextView>(R.id.fragment_user_cart_activity_totalPrice)
        val adapter = UserCartAdapter()
        context?.let { adapter.populate("userCart/" + this.userId, it, totalCostView) }
        fragment_user_cart_activity_rv.adapter = adapter
        val productsLayoutManager = GridLayoutManager(context, 1)
        fragment_user_cart_activity_rv.layoutManager = productsLayoutManager

        fragment_user_cart_activity_checkoutButton.setOnClickListener {
            var firebaseUtil = FirebaseUtil()
            firebaseUtil.openFbReference("userProfile/")
            firebaseUtil.mDatabaseReference.child(userId).child("deliverable")
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        d("deliverable log", "${snapshot.key} ${snapshot.value}")
                        if (snapshot.exists() && snapshot.value == true) {
                            var intent = Intent(activity, CheckoutActivity::class.java)
                            activity?.startActivity(intent)
                        } else {
                            if (context != null) {
                                val builder = AlertDialog.Builder(context!!)
                                builder.setTitle("Are you sure?")
                                builder.setMessage("")
                                builder.setIcon(android.R.drawable.ic_dialog_alert)
                                builder.setPositiveButton("Yes") { dialogInterface, which ->
                                    Toast.makeText(
                                        context,
                                        "clicked yes\n operation conducted",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNeutralButton("Cancel") { dialogInterface, which ->
                                    Toast.makeText(
                                        context,
                                        "clicked cancel\n operation cancel",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton("No") { dialogInterface, which ->
                                    Toast.makeText(
                                        context,
                                        "clicked No",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                            }
                        }
                    }
                })

        }
    }
}
