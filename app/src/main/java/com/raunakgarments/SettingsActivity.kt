package com.raunakgarments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log.d
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.UserSettings
import kotlinx.android.synthetic.main.activity_settings_content_scrolling.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.activity_settings_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        activity_settings_content_scrolling_UnavailableSwitch.layoutParams.width =
            getScreenWidth() / 3

        populateUserSettings()
        switchClickListenerActions()
    }

    private fun populateUserSettings() {
        var userSettingsFirebaseUtil = FirebaseUtil()
        userSettingsFirebaseUtil.openFbReference("userSettings")

        userSettingsFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        d("SettingsActivity", "populateUserSEttingsIfDoesNotExist :- snapshot exists")
                        var userSettings = snapshot.getValue(UserSettings::class.java)

                        if(userSettings != null) {
                            populateSwitchesWithExistingValues(userSettings)
                        } else {
                            d("SettingsActivity", "populateUserSEttingsIfDoesNotExist :- user setting is null")
                        }

                    } else  {
                        d("SettingsActivity", "populateUserSEttingsIfDoesNotExist :- snapshot does not exist")
                        populateUserSettings(userSettingsFirebaseUtil)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })

        //todo if settings snapshot does not exist consider it true
    }

    private fun populateSwitchesWithExistingValues(userSettings: UserSettings) {
        activity_settings_content_scrolling_UnavailableSwitch.isChecked = userSettings.showUnavailableProducts
        activity_settings_content_scrolling_ComingSoonSwitch.isChecked = userSettings.showComingSoonProducts
        activity_settings_content_scrolling_UnderMaintenanceSwitch.isChecked = userSettings.showUnderMaintenanceProducts
    }

    private fun populateUserSettings(userSettingsFirebaseUtil: FirebaseUtil) {
        var userSettings = UserSettings()

        activity_settings_content_scrolling_UnavailableSwitch.isChecked = userSettings.showUnavailableProducts
        activity_settings_content_scrolling_ComingSoonSwitch.isChecked = userSettings.showComingSoonProducts
        activity_settings_content_scrolling_UnderMaintenanceSwitch.isChecked = userSettings.showUnderMaintenanceProducts

        userSettingsFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString()).setValue(userSettings)
    }


    private fun switchClickListenerActions() {
        showUnAvailableProductsClickListener()
        showComingSoonProductsClickListener()
        showUnderMaintenaceProductsClickListener()
    }

    private fun showUnAvailableProductsClickListener() {
        activity_settings_content_scrolling_UnavailableSwitch.setOnCheckedChangeListener { _, isChecked ->

                var userSettingsFirebaseUtil = FirebaseUtil()
                userSettingsFirebaseUtil.openFbReference("userSettings")

                userSettingsFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {

                                var userSettings = snapshot.getValue(UserSettings::class.java)

                                if(userSettings != null) {
                                    userSettings.showUnavailableProducts = isChecked
                                    userSettingsFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString()).setValue(userSettings)
                                } else {
                                    d(
                                        "SettingsActivity",
                                        "showUnAvailableProductsClickListener - userSettings is null"
                                    )
                                }

                            } else {
                                d(
                                    "SettingsActivity",
                                    "showUnAvailableProductsClickListener - snapshot does not exist"
                                )
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}

                    })
        }
    }

    private fun showComingSoonProductsClickListener() {
        activity_settings_content_scrolling_ComingSoonSwitch.setOnCheckedChangeListener { _, isChecked ->

            var userSettingsFirebaseUtil = FirebaseUtil()
            userSettingsFirebaseUtil.openFbReference("userSettings")

            userSettingsFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {

                            var userSettings = snapshot.getValue(UserSettings::class.java)

                            if(userSettings != null) {
                                userSettings.showComingSoonProducts = isChecked
                                userSettingsFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString()).setValue(userSettings)
                            } else {
                                d(
                                    "SettingsActivity",
                                    "showUnAvailableProductsClickListener - userSettings is null"
                                )
                            }

                        } else {
                            d(
                                "SettingsActivity",
                                "showUnAvailableProductsClickListener - snapshot does not exist"
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}

                })
        }
    }

    private fun showUnderMaintenaceProductsClickListener() {
        activity_settings_content_scrolling_UnderMaintenanceSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

            var userSettingsFirebaseUtil = FirebaseUtil()
            userSettingsFirebaseUtil.openFbReference("userSettings")

            userSettingsFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {

                            var userSettings = snapshot.getValue(UserSettings::class.java)

                            if(userSettings != null) {
                                userSettings.showUnderMaintenanceProducts = isChecked
                                userSettingsFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString()).setValue(userSettings)
                            } else {
                                d(
                                    "SettingsActivity",
                                    "showUnAvailableProductsClickListener - userSettings is null"
                                )
                            }

                        } else {
                            d(
                                "SettingsActivity",
                                "showUnAvailableProductsClickListener - snapshot does not exist"
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}

                })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }
}