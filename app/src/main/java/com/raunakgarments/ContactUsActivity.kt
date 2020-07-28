package com.raunakgarments

import android.os.Bundle
import android.renderscript.Sampler
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.raunakgarments.model.ContactUs
import com.raunakgarments.model.Profile
import kotlinx.android.synthetic.main.activity_contact_us_content_scrolling.*

class ContactUsActivity : AppCompatActivity() {

    lateinit var mFirebaseDatebase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        setSupportActionBar(findViewById(R.id.activity_contact_us_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        var firebaseUtil: FirebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("contactUs")
        this.mFirebaseDatebase = firebaseUtil.mFirebaseDatabase
        this.mDatabaseReference = firebaseUtil.mDatabaseReference

        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                var contactUs = snapshot.getValue(ContactUs::class.java)

                if(contactUs != null) {
                    activity_contact_us_content_scrolling_phone_Value_textView.text = contactUs.phoneNumber
                    activity_contact_us_content_scrolling_Email_value_textView.text = contactUs.emailAddress
                    checkEmptyStringAndReplace(activity_contact_us_content_scrolling_phone_Value_textView,"Not Available")
                    checkEmptyStringAndReplace(activity_contact_us_content_scrolling_Email_value_textView,"Not Available")
                }
            }
        })


    }

    fun checkEmptyStringAndReplace(textView: TextView, replaceString: String) {
        if(textView.text == "") {
            textView.text = replaceString
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}