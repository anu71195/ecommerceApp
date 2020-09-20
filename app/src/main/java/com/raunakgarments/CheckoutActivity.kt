package com.raunakgarments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.MenuItem
import android.view.View
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
import java.lang.Long.min
import java.util.*

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {

    var razorPayButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(activity_checkout_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        Checkout.preload(applicationContext)
        activity_checkout_progressBar.visibility = View.GONE
        startRazorPayButtonTimer()
        d("checkoutactivitypre", "${ Gson().toJson(UserCartSingletonClass.confirmationCartProductArray)}")
        activity_checkout_content_scrolling_payButton.setOnClickListener {
            razorPayButtonClicked = true
            val userID = intent.getStringExtra("userID")
            getProfileAndStartPayment(userID)
        }
    }

    //razorpaybutton valid button = 300 seconds
    // one should be able to press razorpay within 7 mins of pressing checkout or 5 mins within the checkout activity started whichever is minimum
    private fun startRazorPayButtonTimer() {
        var timeRemaining = minOf(420 - (Date().time/1000) + UserCartSingletonClass.productLockAcquiredTimeStamp,300)
        d("checkoutactivitype", "${timeRemaining}")
        Handler().postDelayed({setConditionsForRazorPayButtonTimeOut()}, timeRemaining*1000)
    }

    private fun setConditionsForRazorPayButtonTimeOut() {
        if(!razorPayButtonClicked){
            Toast.makeText(this, "Timeout", Toast.LENGTH_LONG).show()
            finish()
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
    /*todo record the product while tking locks as well*/
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
    /*todo release lock if timeout is not done*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Error: Payment Unuccessful", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onPaymentSuccess(p0: String?) {
        activity_checkout_progressBar.visibility = View.VISIBLE
        Toast.makeText(this, "If progress bar is running. \nPlease wait", Toast.LENGTH_LONG).show()
        var userOrderFirebaseUtil = FirebaseUtil()
        var productStockSyncFirebaseUtil = FirebaseUtil()

        userOrderFirebaseUtil.openFbReference("userOrders/"+FirebaseAuth.getInstance().uid)

        val userOrderPushReferenceKey = userOrderFirebaseUtil.mDatabaseReference.push().key

        if (userOrderPushReferenceKey != null) {
            for(userOrderedProduct in UserCartSingletonClass.confirmationCartProductArray) {
                userOrderFirebaseUtil.mDatabaseReference.child(userOrderPushReferenceKey).child(userOrderedProduct.id)
                    .setValue(userOrderedProduct)

                productStockSyncFirebaseUtil.openFbReference("productStockSync/"+userOrderedProduct.id+"/boughtTicket")
                var productStockSyncReferenceKey = productStockSyncFirebaseUtil.mDatabaseReference.push().key
                if (productStockSyncReferenceKey != null) {
                    productStockSyncFirebaseUtil.mDatabaseReference.child(productStockSyncReferenceKey).setValue(userOrderedProduct.quantity)
                }

            }
            userOrderFirebaseUtil.mDatabaseReference.child(userOrderPushReferenceKey).child("orderStatus")
                .setValue("Payment Done")
        }

        var userCartFirebaseUtil = FirebaseUtil()
        userCartFirebaseUtil.openFbReference("userCart/"+FirebaseAuth.getInstance().uid)
        userCartFirebaseUtil.mDatabaseReference.removeValue()
        activity_checkout_progressBar.visibility = View.GONE
        finish()
    }
}