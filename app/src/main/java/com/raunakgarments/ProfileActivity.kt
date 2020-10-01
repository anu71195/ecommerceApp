package com.raunakgarments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import com.google.firebase.ktx.Firebase
import com.raunakgarments.model.Profile
import kotlinx.android.synthetic.main.activity_profile_content_scrolling.*
import java.nio.channels.spi.AsynchronousChannelProvider.provider
import java.nio.channels.spi.SelectorProvider.provider
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
    var verificationID: String = ""
    private lateinit var alertDialogOTP: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.activity_profile_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        settingUpFirebaseVariables()
        unlinkAndSendOTPPhoneNumberDecision()
        unlinkPhoneWithEmailClickListener()
        populateTextFieldsProfileActivity()
        setTextForEmailAddressWarning()
        updateProfileClickListener()
        setEmailVerificationButtonVisibility()
        sendEmailVerificationClickListener()
        attachSendOTPButtonWithSendOTPCode()

    }

    private fun unlinkPhoneNumberWithEmailAddress() {
        var providerID = PhoneAuthProvider.PROVIDER_ID
        d("linked", "${FirebaseAuth.getInstance().currentUser?.phoneNumber}")
        d("linked", "${providerID}")
        if (providerID != null) {
            FirebaseAuth.getInstance().currentUser?.unlink(providerID)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        d("Unlinked", "Unlinked")
                        unlinkAndSendOTPPhoneNumberDecision()
                        // Auth provider unlinked from account
                        // ...
                    } else {
                        d("Unlinked", "Not Unlinked")
                        unlinkAndSendOTPPhoneNumberDecision()
                    }
                }
        }
    }

    private fun attachSendOTPButtonWithSendOTPCode() {
        activity_profile_content_scrolling_sendOTPNumber.setOnClickListener {
            verificationCallbacks()
            if(activity_profile_content_scrolling_phoneNumberCode.text.toString() == "") {
                activity_profile_content_scrolling_phoneNumberCode.setText("91")
            }
            var phoneNumber = activity_profile_content_scrolling_phoneNumberPlusSign.text.toString() + activity_profile_content_scrolling_phoneNumberCode.text.toString() + activity_profile_content_scrolling_phoneNumber.text.toString()
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                5,
                TimeUnit.SECONDS,
                this@ProfileActivity,
                mCallbacks
            )
        }
    }

    private fun verificationCallbacks() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(e: FirebaseException) {
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
                verificationID = p0
                showPinCodePopup()
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
            }
        }
    }

    fun showPinCodePopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("OTP")
        builder.setMessage("Please put your OTP received on y our phone below.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        val pinCodeInputEditText: EditText = EditText(this)
        builder.setView(pinCodeInputEditText)

        builder.setPositiveButton("Yes") { dialogInterface, which ->

            Toast.makeText(this, pinCodeInputEditText.text.toString(), Toast.LENGTH_LONG).show()
            if (pinCodeInputEditText.text.toString() != "") {
                var credential = PhoneAuthProvider.getCredential(
                    verificationID!!,
                    pinCodeInputEditText.text.toString()
                )
                signInWithPhoneAuthCredential(credential)
            }

        }
        builder.setNeutralButton("Cancel") { dialogInterface, which ->
            Toast.makeText(
                this,
                "clicked cancel\n operation cancel",
                Toast.LENGTH_LONG
            ).show()
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            Toast.makeText(this, "clicked No", Toast.LENGTH_LONG).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialogOTP = alertDialog
    }
    /*todo give errors for the fields like phone number is not present or something like that*/
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().currentUser?.linkWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("90", "signInWithCredential:success")
                    val user = task.result?.user
                    if(alertDialogOTP != null) {
                        alertDialogOTP.dismiss()
                    }
                    unlinkAndSendOTPPhoneNumberDecision()

                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("90", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
                unlinkAndSendOTPPhoneNumberDecision()
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

    private fun setTextForEmailAddressWarning() {
        if (!emailVerified) {
            activity_profile_content_scrolling_verification_warning.setTextColor(Color.parseColor("#FF0000"))
            activity_profile_content_scrolling_verification_warning.setText("Please Verify your email. To do that, please go to your email and verify the link.")
        } else {
            activity_profile_content_scrolling_verification_warning.setText("Email is verified.")
            activity_profile_content_scrolling_verification_warning.setTextColor(Color.parseColor("#00FF00"))
        }
    }

    private fun settingUpFirebaseVariables() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseAuth.currentUser?.reload()
        this.userEmailAddress = mFirebaseAuth.currentUser?.email.toString()
        this.emailVerified = mFirebaseAuth.currentUser?.isEmailVerified!!
        this.userId = mFirebaseAuth.uid.toString()
        firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("userProfile/")
        mDatabaseReference = firebaseUtil.mDatabaseReference
    }

    private fun populateTextFieldsProfileActivity() {
        mDatabaseReference.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    var profile = snapshot.getValue(Profile::class.java)

                    if (profile != null) {
                        orderNumber = profile.orderNumber
                        activity_profile_content_scrolling_name.setText(profile.userName)
                        activity_profile_content_scrolling_phoneNumber.setText(profile.number)
                        activity_profile_content_scrolling_phoneNumberCode.setText(profile.areaPhoneCode)
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
    }

    private fun sendEmailVerificationClickListener() {
        activity_profile_content_scrolling_sendEmailVerificationButton.setOnClickListener {
            mFirebaseAuth.currentUser?.sendEmailVerification()
        }
    }

    private fun setEmailVerificationButtonVisibility() {
        if (emailVerified) {
            activity_profile_content_scrolling_sendEmailVerificationButton.visibility = View.GONE
        }
    }

    private fun unlinkAndSendOTPPhoneNumberDecision() {
        FirebaseAuth.getInstance().currentUser?.reload()?.addOnCompleteListener(this) {
            if (FirebaseAuth.getInstance().currentUser?.phoneNumber == null || FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() == "" || FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() == null) {
                activity_profile_content_scrolling_sendOTPNumber.visibility = View.VISIBLE
                activity_profile_content_scrolling_unlinkPhoneWithEmail.visibility = View.GONE
                d(
                    "linkedphoneNumber1",
                    "${FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()}"
                )
                activity_profile_content_scrolling_phoneNumber.isEnabled = true
                activity_profile_content_scrolling_phoneNumberCode.isEnabled = true

            } else {
                activity_profile_content_scrolling_unlinkPhoneWithEmail.visibility = View.VISIBLE
                activity_profile_content_scrolling_sendOTPNumber.visibility = View.GONE
                d(
                    "linkedphoneNumber2",
                    "${FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()}"
                )
                activity_profile_content_scrolling_phoneNumber.isEnabled = false
                activity_profile_content_scrolling_phoneNumberCode.isEnabled = false
                var OTPPhoneNumberProfileFirebaseUtil: FirebaseUtil = FirebaseUtil()
                OTPPhoneNumberProfileFirebaseUtil.openFbReference("userProfile/")
                OTPPhoneNumberProfileFirebaseUtil.mDatabaseReference.child(userId)
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var profile = snapshot.getValue(Profile::class.java)
                            if (profile != null &&
                                (activity_profile_content_scrolling_phoneNumberPlusSign.text.toString() +
                                        activity_profile_content_scrolling_phoneNumberCode.text.toString() +
                                        activity_profile_content_scrolling_phoneNumber.text.toString()) == FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
                            ) {
                                profile.areaPhoneCode =
                                    activity_profile_content_scrolling_phoneNumberCode.text.toString()
                                profile.number =
                                    activity_profile_content_scrolling_phoneNumber.text.toString()
                                isAddressDeliverable(profile)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            }
        }

    }

    private fun updateProfileClickListener() {
        activity_profile_content_scrolling_updateButton.setOnClickListener {
            d("Update Button", "clicked")
            var name = activity_profile_content_scrolling_name.text.toString()
            var number: String = ""
            var email: String = activity_profile_content_scrolling_emailAddress.text.toString()
            var address: String = activity_profile_content_scrolling_address.text.toString()
            var pinCode: String = activity_profile_content_scrolling_pincode.text.toString()
            var profile = Profile(name, number, email, address, pinCode)
            d(
                (activity_profile_content_scrolling_phoneNumberPlusSign.text.toString() +
                        activity_profile_content_scrolling_phoneNumberCode.text.toString() +
                        activity_profile_content_scrolling_phoneNumber.text.toString()),
                FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() + "sometextprofile"
            )
            if (FirebaseAuth.getInstance().currentUser?.phoneNumber == null || FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() == "" || FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() == null) {
                profile.number = activity_profile_content_scrolling_phoneNumber.text.toString()
                profile.areaPhoneCode =
                    activity_profile_content_scrolling_phoneNumberCode.text.toString()
            } else {
                if ((activity_profile_content_scrolling_phoneNumberPlusSign.text.toString() +
                            activity_profile_content_scrolling_phoneNumberCode.text.toString() +
                            activity_profile_content_scrolling_phoneNumber.text.toString()) == FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
                ) {
                    profile.areaPhoneCode =
                        activity_profile_content_scrolling_phoneNumberCode.text.toString()
                    profile.number =
                        activity_profile_content_scrolling_phoneNumber.text.toString()
                }
            }
            profile.orderNumber = orderNumber
            isAddressDeliverable(profile)
        }
    }

    private fun unlinkPhoneWithEmailClickListener() {
        activity_profile_content_scrolling_unlinkPhoneWithEmail.setOnClickListener {
            unlinkPhoneNumberWithEmailAddress()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}