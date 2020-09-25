package com.raunakgarments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.raunakgarments.global.UserCartSingletonClass
import com.raunakgarments.helper.CostFormatterHelper
import com.raunakgarments.helper.ProductStockSyncHelper
import com.raunakgarments.model.ProductStockSync
import com.raunakgarments.model.Profile
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout_content_scrolling.*
import org.json.JSONObject
import java.util.*

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {

    var razorPayButtonClicked = false
    var isRazorPayOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(activity_checkout_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        activityStartBookkeeping()
        startRazorPayButtonTimer()
        razorPayButtonClickListener()
    }

    private fun activityStartBookkeeping() {
        Checkout.preload(applicationContext)
        activity_checkout_progressBar.visibility = View.GONE
        activity_checkout_progressBarText.visibility = View.GONE
        activity_checkout_content_scrolling_payButton.isEnabled = true
    }

    private fun razorPayButtonClickListener() {
        activity_checkout_content_scrolling_payButton.setOnClickListener {
            activity_checkout_content_scrolling_payButton.isEnabled = false
            razorPayButtonClicked = true
            isRazorPayOpen = true

            //todo set timer over loop while razorpay is open
            updateUserProductLockTimeoutRecurrently(3000)

            val userID = intent.getStringExtra("userID")
            getProfileAndStartPayment(userID)
        }
    }

    private fun updateUserProductLockTimeoutRecurrently(timer: Int) {
        Handler().postDelayed({increaseTimeout(timer)}, timer.toLong())
    }
    private fun increaseTimeout(timer: Int) {
        d("CheckoutActivity", "increaseTimeout - timer - ${timer}")
        if (isRazorPayOpen) {
            updateUserProductLockTimeoutRecurrently(timer)
        }
    }

    //razorpaybutton valid button = 300 seconds
    // one should be able to press razorpay within 7 mins of pressing checkout or 5 mins within the checkout activity started whichever is minimum
    private fun startRazorPayButtonTimer() {
        var timeRemaining = minOf(
            420 - (Date().time / 1000) + UserCartSingletonClass.productLockAcquiredTimeStamp,
            300
        )
        d("checkoutactivitype", "${timeRemaining}")
        Handler().postDelayed({ checkConditionsForRazorPayButtonTimeOut() }, timeRemaining * 1000)
    }

    private fun checkConditionsForRazorPayButtonTimeOut() {
        if (!razorPayButtonClicked) {
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

    /*todo record the product while tking locks as well along with time*/
    /*todo release lock when */
    fun startRazorpayPayment(co: Checkout, profile: Profile, userID: String) {
        val activity: Activity = this
        var totalCartCost = intent.getDoubleExtra("totalCartCost", 0.0)
        if (totalCartCost > 0.0) {
            totalCartCost = CostFormatterHelper().formatCost(totalCartCost * 100)
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
                options.put(
                    "amount",
                    totalCartCost.toInt().toString()
                )//pass amount in currency subunits

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
    /*todo add time when razorpay button is clicked*/
    /*todo add timeout in the background while razorpay is up and increment user product lock timeout accordingly*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        isRazorPayOpen = false
        activity_checkout_progressBar.visibility = View.VISIBLE
        activity_checkout_progressBarText.visibility = View.VISIBLE
        Toast.makeText(this, "Error: Payment Unuccessful", Toast.LENGTH_LONG).show()
        waitBeforeReleasingLock()
        Handler().postDelayed({ waitAndFinishActivity() }, 3 * 1000)

    }

    override fun onPaymentSuccess(p0: String?) {
        isRazorPayOpen = false
        activity_checkout_progressBar.visibility = View.VISIBLE
        activity_checkout_progressBarText.visibility = View.VISIBLE
        var userOrderFirebaseUtil = FirebaseUtil()
        var productStockSyncFirebaseUtil = FirebaseUtil()

        userOrderFirebaseUtil.openFbReference("userOrders/" + FirebaseAuth.getInstance().uid)

        val userOrderPushReferenceKey = userOrderFirebaseUtil.mDatabaseReference.push().key

        if (userOrderPushReferenceKey != null) {
            for (userOrderedProduct in UserCartSingletonClass.confirmationCartProductArray) {
                if (userOrderedProduct.productStatus == 1) {
                    userOrderFirebaseUtil.mDatabaseReference.child(userOrderPushReferenceKey)
                        .child(userOrderedProduct.id)
                        .setValue(userOrderedProduct)

                    productStockSyncFirebaseUtil.openFbReference("productStockSync/" + userOrderedProduct.id + "/boughtTicket")
                    var productStockSyncReferenceKey =
                        productStockSyncFirebaseUtil.mDatabaseReference.push().key
                    if (productStockSyncReferenceKey != null) {
                        productStockSyncFirebaseUtil.mDatabaseReference.child(
                            productStockSyncReferenceKey
                        ).setValue(userOrderedProduct.quantity)
                    }

                }
            }
            userOrderFirebaseUtil.mDatabaseReference.child(userOrderPushReferenceKey)
                .child("orderStatus")
                .setValue("Payment Done")
        }

        waitBeforeReleasingLock()
        var userCartFirebaseUtil = FirebaseUtil()
        userCartFirebaseUtil.openFbReference("userCart/" + FirebaseAuth.getInstance().uid)
        userCartFirebaseUtil.mDatabaseReference.removeValue()
        Handler().postDelayed({ waitAndFinishActivity() }, 3 * 1000)


    }

    private fun waitAndFinishActivity() {

        activity_checkout_progressBar.visibility = View.GONE
        activity_checkout_progressBarText.visibility = View.GONE
        finish()
    }

    private fun waitBeforeReleasingLock() {
        Handler().postDelayed({ releaseLockIfTimeIsLeft() }, 1 * 1000)
    }

    private fun releaseLockIfTimeIsLeft() {
        var productStockSyncFirebaseUtil = FirebaseUtil()
        for (userOrderedProduct in UserCartSingletonClass.confirmationCartProductArray) {
            //if more than 2 minute is left for the lock timeout then release lock
            var timeLeft =
                480 - (Date().time / 1000) + UserCartSingletonClass.productLockAcquiredTimeStamp
            d("checkoutactivity", "${timeLeft}")
            if (timeLeft > 0 && userOrderedProduct.productStatus == 1) {
                productStockSyncFirebaseUtil.openFbReference("productStockSync/" + userOrderedProduct.id)
                productStockSyncFirebaseUtil.mDatabaseReference.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                var productStockSync =
                                    snapshot.getValue(ProductStockSync::class.java)
                                if (productStockSync != null) {
                                    if (productStockSync.locked ==
                                        FirebaseAuth.getInstance().uid.toString()
                                    ) {
                                        d(
                                            "checkoutactivity",
                                            " releaseLockIfTimeIsLeft :- ${productStockSync.toString()}"
                                        )

                                        productStockSync.locked = "-1"

                                        var totalBoughtItems = 0

                                        for (boughtItems in productStockSync.boughtTicket) {
                                            totalBoughtItems += boughtItems.value
                                        }
                                        productStockSync.stock =
                                            productStockSync.stock - totalBoughtItems
                                        productStockSync.boughtTicket = HashMap<String, Int>()

                                        ProductStockSyncHelper().setValueInChild(
                                            snapshot.key.toString(),
                                            productStockSync)

                                    }
                                }
                            } else {
                                d(
                                    "checkoutactivity",
                                    " releaseLockIfTimeIsLeft :- Snapshot does not exist"
                                )
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}

                    })
            }
        }
    }

}