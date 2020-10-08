package com.raunakgarments.admin

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.raunakgarments.FirebaseUtil
import com.raunakgarments.R
import kotlinx.android.synthetic.main.activity_admin_pin_code_edit_content_scrolling.*

class AdminPinCodeEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_pin_code_edit)
        setSupportActionBar(findViewById(R.id.activity_admin_pin_code_edit_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        var firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference(getString(R.string.database_pincode))

        activity_admin_pin_code_edit_content_scrolling_pinCodeAddButton.setOnClickListener {
            var pinCodeText = activity_admin_pin_code_edit_content_scrolling_pincodeEditView.text.toString()
            if(pinCodeText != "") {
                firebaseUtil.mDatabaseReference.child(pinCodeText).setValue(1)
            }
        }

        val pinCodeAdapter = AdminPinCodeAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
        pinCodeAdapter.populate(getString(R.string.database_pincode), this)
        activity_admin_pin_code_edit_content_scrolling_pincodeRecyclerView.adapter = pinCodeAdapter
        activity_admin_pin_code_edit_content_scrolling_pincodeRecyclerView.layoutManager = productsLayoutManager
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}