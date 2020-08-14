package com.raunakgarments

import android.app.Activity
import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout_content_scrolling.*
import kotlinx.android.synthetic.main.activity_user_cart.*
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
        activity_checkout_content_scrolling_payButton.setOnClickListener {
            startPayment()
        }
    }

    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        var firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("paymentGateway")
        firebaseUtil.mDatabaseReference.child("razorPay").child("keyID")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    d("razorPay", "${snapshot.toString()}")
                    co.setKeyID(snapshot.value.toString())
                    try {
                        val options = JSONObject()
                        options.put("name", "Razorpay Corp")
                        options.put("description", "Demoing Charges")
                        //You can omit the image option to fetch the image from dashboard
                        options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
                        options.put("theme.color", "#3399cc");
                        options.put("currency", "INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
                        options.put("amount", "50000")//pass amount in currency subunits

                        val prefill = JSONObject()
                        prefill.put("email", "gaurav.kumar@example.com")
                        prefill.put("contact", "9876543210")

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
            })


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Error: Payment Unuccessful", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful $p0", Toast.LENGTH_LONG).show()
    }
}