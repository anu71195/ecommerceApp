package com.raunakgarments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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

        fragment_user_cart_activity_progessBar.visibility = View.GONE
        settingUpRecyclerView(view)
        checkOutButtonClickListener()

    }

    private fun settingUpRecyclerView(view: View) {
        val totalCostView = view.findViewById<TextView>(R.id.fragment_user_cart_activity_totalPrice)
        val adapter = UserCartAdapter()
        context?.let { adapter.populate("userCart/" + this.userId, it, totalCostView) }
        fragment_user_cart_activity_rv.adapter = adapter
        val productsLayoutManager = GridLayoutManager(context, 1)
        fragment_user_cart_activity_rv.layoutManager = productsLayoutManager
    }

    private fun checkOutButtonClickListener() {
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
                        checkConditionsForCheckout(profile)
                    }
                })
        }
    }

    fun checkConditionsForCheckout(profile: Profile?) {
        mFirebaseAuth.currentUser?.reload()
        var emailVerified = mFirebaseAuth.currentUser?.isEmailVerified!!
        d(
            "checkoutActivity",
            FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
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
                processBeforeCallingCheckoutActivity(profile, userId)
            } else {
                paymentErrorPopup()
            }
        }
    }

    private fun processBeforeCallingCheckoutActivity(profile: Profile, userID: String) {
        getLocks(profile, userID)
    }

private fun callCheckoutActivity(profile: Profile, userID: String) {
    var intent =
        Intent(activity, CheckoutActivity::class.java)
    intent.putExtra("userID", userID)
    activity?.startActivity(intent)
}
    private fun getLocks(profile: Profile, userID: String) {
        fragment_user_cart_activity_progessBar.visibility = View.VISIBLE

/*todo*/


//        var userCartFirebaseUtil: FirebaseUtil = FirebaseUtil()
//        userCartFirebaseUtil.openFbReference("userCart/"+FirebaseAuth.getInstance().uid.toString())
//        userCartFirebaseUtil.mDatabaseReference.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                d("checkout", "${snapshot.toString()}")
//            }
//            override fun onCancelled(error: DatabaseError) {}
//
//        })

//        var productSyncFirebaseUtil = FirebaseUtil()
//        productSyncFirebaseUtil.openFbReference("productStockSync")
//        productSyncFirebaseUtil.mDatabaseReference.addValueEventListener(object: ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//
//        })



        Handler().postDelayed({callCheckoutActivity(profile, userID)}, 5000)
    }

    private fun paymentErrorPopup() {
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

