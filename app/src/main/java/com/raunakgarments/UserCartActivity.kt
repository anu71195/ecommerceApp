package com.raunakgarments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.replace
import kotlinx.android.synthetic.main.activity_user_cart.*

class UserCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_cart)
        setSupportActionBar(activity_user_cart_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_user_cart_frameLayout, UserCartActivityrvFragment(this)).commit()
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