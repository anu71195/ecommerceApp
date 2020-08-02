package com.raunakgarments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
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

class ProfileActivity : AppCompatActivity() {
    private var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var firebaseUtil: FirebaseUtil
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var userId: String
    private var userEmailAddress: String = ""
    private var emailVerified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.activity_profile_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseAuth.currentUser?.reload()
        this.userEmailAddress = mFirebaseAuth.currentUser?.email.toString()
        this.emailVerified = mFirebaseAuth.currentUser?.isEmailVerified!!
        this.userId = mFirebaseAuth.uid.toString()

        firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("userProfile/")
        mDatabaseReference = firebaseUtil.mDatabaseReference

        mDatabaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                var profile = snapshot.getValue(Profile::class.java)

                if (profile != null) {
                    activity_profile_content_scrolling_name.setText(profile.userName)
                    activity_profile_content_scrolling_phoneNumber.setText(profile.number)
                    activity_profile_content_scrolling_emailAddress.setText(profile.email)
                    activity_profile_content_scrolling_address.setText(profile.address)
                    activity_profile_content_scrolling_pincode.setText(profile.pinCode)
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
            mDatabaseReference.child(userId).setValue(profile)
        }

        if(emailVerified){
            activity_profile_content_scrolling_sendEmailVerificationButton.visibility = View.GONE
        }

        activity_profile_content_scrolling_sendEmailVerificationButton.setOnClickListener {
            mFirebaseAuth.currentUser?.sendEmailVerification()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}