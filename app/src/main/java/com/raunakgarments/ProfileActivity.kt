package com.raunakgarments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.raunakgarments.model.Product
import com.raunakgarments.model.Profile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_content_scrolling.*
import org.jetbrains.anko.email
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor
import java.io.File
import java.util.concurrent.TimeUnit

class ProfileActivity : AppCompatActivity() {
    private var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var firebaseUtil: FirebaseUtil
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var userId: String
    private var userEmailAddress: String = ""
    private var emailVerified = false
    private var orderNumber: Int = 1
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var credential: PhoneAuthCredential

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.activity_profile_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)

            verificationCallbacks()
            d("9085811917", "9085811917")
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+919085811917",
                5,
                TimeUnit.SECONDS,
                this@ProfileActivity,
                mCallbacks
            )
        }

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseAuth.currentUser?.reload()
        this.userEmailAddress = mFirebaseAuth.currentUser?.email.toString()
        this.emailVerified = mFirebaseAuth.currentUser?.isEmailVerified!!
        this.userId = mFirebaseAuth.uid.toString()

        firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("userProfile/")
        mDatabaseReference = firebaseUtil.mDatabaseReference

        mDatabaseReference.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    var profile = snapshot.getValue(Profile::class.java)

                    if (profile != null) {
                        orderNumber = profile.orderNumber
                        activity_profile_content_scrolling_name.setText(profile.userName)
                        activity_profile_content_scrolling_phoneNumber.setText(profile.number)
                        activity_profile_content_scrolling_emailAddress.setText(profile.email)
                        activity_profile_content_scrolling_address.setText(profile.address)
                        activity_profile_content_scrolling_pincode.setText(profile.pinCode)
                        setTextForDeliverableWarning(profile.deliverable)
                    }
                    d("userProfile", snapshot.key)
                    d("userProfile", snapshot.value.toString())
                }
            })

        activity_profile_content_scrolling_emailAddress.setText(userEmailAddress)
        d("Email Verification", "$emailVerified")
        if (!emailVerified) {
            activity_profile_content_scrolling_verification_warning.setTextColor(Color.parseColor("#FF0000"))
            activity_profile_content_scrolling_verification_warning.setText("Please Verify your email. To do that, please go to your email and verify the link.")
        } else {
            activity_profile_content_scrolling_verification_warning.setText("Email is verified.")
            activity_profile_content_scrolling_verification_warning.setTextColor(Color.parseColor("#00FF00"))
        }

        activity_profile_content_scrolling_updateButton.setOnClickListener {
            d("Update Button", "clicked")
            var name = activity_profile_content_scrolling_name.text.toString()
            var number: String = activity_profile_content_scrolling_phoneNumber.text.toString()
            var email: String = activity_profile_content_scrolling_emailAddress.text.toString()
            var address: String = activity_profile_content_scrolling_address.text.toString()
            var pinCode: String = activity_profile_content_scrolling_pincode.text.toString()
            var profile = Profile(name, number, email, address, pinCode)
            profile.orderNumber = orderNumber
            isAddressDeliverable(profile)
        }

        if (emailVerified) {
            activity_profile_content_scrolling_sendEmailVerificationButton.visibility = View.GONE
        }

        activity_profile_content_scrolling_sendEmailVerificationButton.setOnClickListener {
            mFirebaseAuth.currentUser?.sendEmailVerification()
        }


    }

    private fun verificationCallbacks() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                d("9085811917", "Code sent success")
                Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
                signInWithPhoneAuthCredential(credential)

            }

            override fun onVerificationFailed(e: FirebaseException) {
                d("+919085811917", "Code sent failure")
                Log.w(e.toString(), "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                d("9085811917", "Code sent")
                d("9085811917", "${p0} ${p1}")
                Log.d("9085811917", "onCodeSent:$p0")
                credential = PhoneAuthProvider.getCredential(p0!!, p1.toString())
                Log.d("9085811917", "onCodeSent:$credential")
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                d("9085811917", "${p0}")
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("9085811917", "signInWithCredential:success")

                    val user = task.result?.user
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("9085811917", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    private fun isAddressDeliverable(profile: Profile) {
        var pinCodeFirebaseUtil = FirebaseUtil()
        pinCodeFirebaseUtil.openFbReference(getString(R.string.database_pincode))
        pinCodeFirebaseUtil.mDatabaseReference.child(profile.pinCode)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (profile.pinCode != "") {
                        profile.deliverable = snapshot.exists()
                    } else {
                        profile.deliverable = false
                    }
                    mDatabaseReference.child(userId).setValue(profile)
                    setTextForDeliverableWarning(profile.deliverable)
                }
            })
    }

    fun setTextForDeliverableWarning(isDeliverable: Boolean) {
        if (isDeliverable) {
            activity_profile_content_scrolling_deliverable_warning.setText("Pin Code is Deliverable")
            activity_profile_content_scrolling_deliverable_warning.setTextColor(Color.parseColor("#00FF00"))
        } else {
            activity_profile_content_scrolling_deliverable_warning.setTextColor(Color.parseColor("#FF0000"))
            activity_profile_content_scrolling_deliverable_warning.setText("We do not serve on below pincode.")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}