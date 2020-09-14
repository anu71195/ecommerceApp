package com.raunakgarments

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class CartConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_confirmation)
        setSupportActionBar(findViewById(R.id.activity_cart_confirmation_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_cart_confirmation_frameLayout, CartConfirmationActivityrvFragment(this)).commit()
    }
}