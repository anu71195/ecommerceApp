package com.raunakgarments

import android.os.Bundle
import android.util.Log.d
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.raunakgarments.model.UserOrders

class UserOrderDetailsActivity : AppCompatActivity() {
    private lateinit var userOrders: UserOrders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_order_details)
        setSupportActionBar(findViewById(R.id.activity_user_order_details_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        userOrders = Gson().fromJson<UserOrders>(intent.getStringExtra("userOrders"), UserOrders::class.java)
        d("UserOrderDetailsActivity", "onCreate-${Gson().toJson(userOrders)}")
        d("UserOrderDetailsActivity", "onCreate-${Gson().toJson(userOrders.orders)}")
    }
}