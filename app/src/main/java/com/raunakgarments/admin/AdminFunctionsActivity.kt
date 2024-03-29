package com.raunakgarments.admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.raunakgarments.R
import kotlinx.android.synthetic.main.activity_admin_functions_content_scrolling.*

class AdminFunctionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_functions)
        setSupportActionBar(findViewById(R.id.activity_admin_functions_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        Activity_admin_functions_content_scrolling_edit_contactUsButton.setOnClickListener {
            var intent = Intent(this, AdminContactUsEditActivity::class.java)
            this.startActivity(intent)
        }

        Activity_admin_functions_content_scrolling_edit_insertPinCodeButton.setOnClickListener {
            var intent = Intent(this, AdminPinCodeEditActivity::class.java)
            this.startActivity(intent)
        }

        Activity_admin_functions_content_scrolling_edit_SpamCounterSettingsButton.setOnClickListener {
            var intent = Intent(this, AdminSpamSettingsActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}