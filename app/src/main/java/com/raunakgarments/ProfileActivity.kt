package com.raunakgarments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_content_scrolling.*
import org.jetbrains.anko.email
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
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

        userEmailAddress = mFirebaseAuth.currentUser?.email.toString()
        emailVerified = mFirebaseAuth.currentUser?.isEmailVerified!!

        activity_profile_content_scrolling_emailAddress.setText(userEmailAddress)
        d("Email Verification", "$emailVerified")
        if(!emailVerified) {
            activity_profile_content_scrolling_verification_warning.setTextColor(Color.parseColor("#FF0000"))
            activity_profile_content_scrolling_verification_warning.setText("Please Verify your email")
        } else {
            activity_profile_content_scrolling_verification_warning.setText("Email iis verified")
            activity_profile_content_scrolling_verification_warning.setTextColor(Color.parseColor("#00FF00"))
        }

//        ic_baseline_check_24_green

//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}