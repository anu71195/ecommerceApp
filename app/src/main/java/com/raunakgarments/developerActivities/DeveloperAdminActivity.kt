package com.raunakgarments.developerActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        syncProductStockSyncButtonClickListener()
    }

    private fun syncProductStockSyncButtonClickListener() {
        activity_developer_admin_content_scrolling_edit_syncProductStockSync.setOnClickListener {

        }
    }

}