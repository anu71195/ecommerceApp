    package com.raunakgarments

    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
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
                .replace(R.id.activity_user_cart_frameLayout, UserCartActivityrvFragment(this, supportActionBar)).commit()
        }
    }