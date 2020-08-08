package com.raunakgarments

import android.os.Bundle
import android.util.Log.d
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_admin_pin_code_edit_content_scrolling.*

class AdminPinCodeEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_pin_code_edit)
        setSupportActionBar(findViewById(R.id.activity_admin_pin_code_edit_toolbar))

        var firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("pincode")

        activity_admin_pin_code_edit_content_scrolling_pinCodeAddButton.setOnClickListener {
            var pinCodeText = activity_admin_pin_code_edit_content_scrolling_pincodeEditView.text.toString()
            if(pinCodeText != "") {
                firebaseUtil.mDatabaseReference.child(pinCodeText).setValue(1)
            }
        }
    }
}