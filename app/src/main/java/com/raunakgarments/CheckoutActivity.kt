package com.raunakgarments

import android.app.Activity
import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.raunakgarments.global.UserCartSingletonClass
import com.raunakgarments.helper.CostFormatterHelper
import com.raunakgarments.model.ConfirmationCartProduct
import com.raunakgarments.model.Profile
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout_content_scrolling.*
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(activity_checkout_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        Checkout.preload(applicationContext)
        d("checkoutactivitypre", "${ Gson().toJson(UserCartSingletonClass.confirmationCartProductArray)}")
        activity_checkout_content_scrolling_payButton.setOnClickListener {
            val userID = intent.getStringExtra("userID")
            getProfileAndStartPayment(userID)
        }
    }

    private fun getProfileAndStartPayment(userID: String) {
        var firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("userProfile/")
        firebaseUtil.mDatabaseReference.child(userID)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    var profile = snapshot.getValue(Profile::class.java)
                    if (profile != null) {
                        profile.orderNumber = profile.orderNumber + 1
                        firebaseUtil.mDatabaseReference.child(userID).setValue(profile)
                        startPayment(profile, userID)
                    }
                }
            })
    }


    private fun startPayment(profile: Profile, userID: String) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val co = Checkout()

        var firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("paymentGateway")
        firebaseUtil.mDatabaseReference.child("razorPay").child("keyID")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    d("razorPay", "${snapshot.toString()}")
                    co.setKeyID(snapshot.value.toString())
                    startRazorpayPayment(co, profile, userID)
                }
            })
    }
    /*todo 10 min if not paid then pay with razor pay button get disabled*/
    /*todo release lock when */
    fun startRazorpayPayment(co: Checkout, profile: Profile, userID: String) {
        val activity: Activity = this
        var totalCartCost = intent.getDoubleExtra("totalCartCost", 0.0)
        if (totalCartCost > 0.0) {
            totalCartCost = CostFormatterHelper().formatCost(totalCartCost*100)
            try {
                val options = JSONObject()
                val orderID = "ERUS" + userID + "EMTI" + System.currentTimeMillis()
                    .toString() + "RDEOR" + profile.orderNumber.toString()
                d("orderID", orderID)
                options.put("name", "Raunak Garments")
                options.put("description", "Net order Charges")
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
                options.put("theme.color", "#3399cc")
                options.put("currency", "INR")
//                        options.put("order_id", orderID)
                options.put("amount", totalCartCost.toInt().toString())//pass amount in currency subunits

                val prefill = JSONObject()
                prefill.put("email", profile.email)
                prefill.put("contact", profile.number)

                options.put("prefill", prefill)
                co.open(activity, options)
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    "Error in payment: " + e.message,
                    Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
            }
        }
    }
/*todo decrement when product payment is done*/
/*todo on failure and success payment*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Error: Payment Unuccessful", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful $p0", Toast.LENGTH_LONG).show()
        var userOrderFirebaseUtil = FirebaseUtil()
        userOrderFirebaseUtil.openFbReference("userOrders/"+FirebaseAuth.getInstance().uid)
        val userOrderPushReferenceKey = userOrderFirebaseUtil.mDatabaseReference.push().key

        if (userOrderPushReferenceKey != null) {
            for(userOrderedProduct in UserCartSingletonClass.confirmationCartProductArray) {
                userOrderFirebaseUtil.mDatabaseReference.child(userOrderPushReferenceKey).child(userOrderedProduct.id)
                    .setValue(userOrderedProduct)
            }
            userOrderFirebaseUtil.mDatabaseReference.child(userOrderPushReferenceKey).child("orderStatus")
                .setValue("Payment Done")
        }
        /*todo empty cart*/
        finish()
    }
}