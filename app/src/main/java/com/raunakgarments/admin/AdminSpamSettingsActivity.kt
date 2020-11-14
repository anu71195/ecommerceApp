package com.raunakgarments.admin

import android.os.Bundle
import android.util.Log.d
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.raunakgarments.R
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.SpamSettings
import kotlinx.android.synthetic.main.activity_admin_spam_settings_content_scrolling.*

class AdminSpamSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_spam_settings)
        setSupportActionBar(findViewById(R.id.activity_admin_spam_settings_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        populateSpamSettingsData()
        updateSpamSettingsClickListener()
    }

    private fun populateSpamSettingsData() {
        var spamSettingsFirebaseUtil = FirebaseUtil()
        spamSettingsFirebaseUtil.openFbReference("spamSettings")

        spamSettingsFirebaseUtil.mDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var spamSettings = SpamSettings()
                if(snapshot.exists()) {
                    spamSettings = snapshot.getValue(SpamSettings::class.java)!!
                } else  {
                    d("AdminSpamSettingsActivity", "populateSPamSEttingsData - snapshot does not exist")
                }
                activity_admin_spam_settings_content_scrolling_attemptNumbersText.setText(spamSettings.lockLimit.toString())
                activity_admin_spam_settings_content_scrolling_deletionDaysText.setText(spamSettings.daysDataAvailability.toString())
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun updateSpamSettingsClickListener() {
        activity_admin_spam_settings_content_scrolling_updateSpamSettingsButton.setOnClickListener {

            var spamSettingsFirebaseUtil = FirebaseUtil()
            spamSettingsFirebaseUtil.openFbReference("spamSettings")

            var spamSettings = SpamSettings()

            spamSettings.lockLimit = activity_admin_spam_settings_content_scrolling_attemptNumbersText.text.toString().toInt()
            spamSettings.daysDataAvailability = activity_admin_spam_settings_content_scrolling_deletionDaysText.text.toString().toInt()

            spamSettingsFirebaseUtil.mDatabaseReference.setValue(spamSettings)

        }
    }
}