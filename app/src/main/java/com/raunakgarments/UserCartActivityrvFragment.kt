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
import com.google.gson.Gson
import com.raunakgarments.model.Product
import com.raunakgarments.model.Profile
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
            firebaseUtil.mDatabaseReference.child(userId)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        d("deliverable log", "${snapshot.key} ${snapshot.value}")
                        var profile = snapshot.getValue(Profile::class.java)
                        mFirebaseAuth.currentUser?.reload()
                        var emailVerified = mFirebaseAuth.currentUser?.isEmailVerified!!
                        d(
                            "checkoutActivity",
                            "${FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()}"
                        )
                        FirebaseAuth.getInstance().currentUser?.reload()?.addOnCompleteListener {
                            if (profile != null &&
                                profile.deliverable &&
                                emailVerified &&
                                profile.address != "" &&
                                FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() != "" &&
                                FirebaseAuth.getInstance().currentUser?.phoneNumber != null &&
                                FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() != null
                            ) {
                                callCheckoutActivity(profile, userId)
                            } else {
                                paymentErrorPopup()
                            }
                        }
                    }
                })
        }
    }

    fun callCheckoutActivity(profile: Profile, userID: String) {
        var intent =
            Intent(activity, CheckoutActivity::class.java)
        intent.putExtra("userID", userID)
        activity?.startActivity(intent)
    }

    fun paymentErrorPopup() {
        if (context != null) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Can't Checkout?")
            builder.setMessage("Please check if your email and phone Number is verified, pincode is deliverable and address is present from Profile section.")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("OK") { dialogInterface, which ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

}

