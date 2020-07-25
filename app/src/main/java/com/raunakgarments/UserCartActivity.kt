package com.raunakgarments

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.replace

class UserCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_cart)
        setSupportActionBar(findViewById(R.id.activity_user_cart_toolbar))
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_user_cart_frameLayout, UserCartActivityrvFragment()).commit()
//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }

}