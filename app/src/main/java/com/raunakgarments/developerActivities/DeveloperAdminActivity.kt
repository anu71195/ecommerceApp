package com.raunakgarments.developerActivities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.PhoneAuthProvider
import com.raunakgarments.AdminProductActivityNew
import com.raunakgarments.R
import kotlinx.android.synthetic.main.activity_developer_admin_content_scrolling.*

class DeveloperAdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_admin)
        setSupportActionBar(findViewById(R.id.activity_developer_admin_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        developerFlowWarning()
        syncProductStockSyncButtonClickListener()
    }

    private fun syncProductStockSyncButtonClickListener() {
        activity_developer_admin_content_scrolling_edit_syncProductStockSync.setOnClickListener {

        }
    }

    private fun developerFlowWarning() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Warning: Dangerous and Irreversible effects beyond")
        builder.setMessage("If you have access to this and don't know about its usage. Please contact the relevant person for that. Use or change of any thing beyond this point in this flow may result in severe and irreversible changes. Select Yes to go along with this flow or No to return to Admin screen.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            Toast.makeText(this, "You Pressed yes \n Please be careful", Toast.LENGTH_LONG).show()
        }
//        builder.setNeutralButton("Cancel") { dialogInterface, which ->
//            Toast.makeText(
//                this,
//                "clicked cancel\n operation cancel",
//                Toast.LENGTH_LONG
//            ).show()
//        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            Toast.makeText(this, "clicked No", Toast.LENGTH_LONG).show()
            finish()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}