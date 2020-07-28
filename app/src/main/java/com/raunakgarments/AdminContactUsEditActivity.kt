package com.raunakgarments

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.raunakgarments.model.ContactUs
import kotlinx.android.synthetic.main.activity_admin_contact_us_edit_content_scrolling.*
import kotlinx.android.synthetic.main.activity_admin_functions_content_scrolling.*
import kotlinx.android.synthetic.main.activity_contact_us_content_scrolling.*

class AdminContactUsEditActivity : AppCompatActivity() {

    lateinit var mFirebaseDatebase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_contact_us_edit)
        setSupportActionBar(findViewById(R.id.activity_admin_contact_us_edit_toolbar))
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
                    activity_admin_contact_us_edit_content_scrolling_phoneEditText.setText(contactUs.phoneNumber)
                    activity_admin_contact_us_edit_content_scrolling_emailEditTex.setText(contactUs.emailAddress)
                }
            }
        })

        activity_admin_contact_us_edit_content_scrolling_UpdateContactDetailsButton.setOnClickListener {
            var phoneNumber = activity_admin_contact_us_edit_content_scrolling_phoneEditText.text.toString()
            var emailAddress = activity_admin_contact_us_edit_content_scrolling_emailEditTex.text.toString()
            var contactUs = ContactUs(phoneNumber, emailAddress)
            mDatabaseReference.setValue(contactUs)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}