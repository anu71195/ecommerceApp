package com.raunakgarments

import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.raunakgarments.model.Product

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, MainFragment()).commit()

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.actionHome -> d("abc", "Going Home")
                R.id.actionJeans -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, JeansFragment()).commit()
                    d("abc", "jeans was pressed")
                }
                R.id.actionJeans -> d("abc", "Shorts was pressed")
            }
            it.isChecked = true
            drawerLayout.closeDrawers()
            true
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        val products = arrayListOf<Product>()
        val imageUrl = "https://image.spreadshirtmedia.com/image-server/v1/products/T812A366PA3140PT17X50Y30D12906314FS9045CxFFFFFF/views/2,width=650,height=650,appearanceId=366,backgroundColor=f1f1f1/youve-got-the-keyboard-now-get-the-t-shirt-this-is-the-original-red-t-shirt-from-nord-keyboards-official-clothing-line.jpg"
        for(i in 0..100) {
            products.add(Product(title = "red colored cotton t-shirt #$i", photoUrl = imageUrl, price = 1.99))
        }

        recyler_view.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = ProductAdapter(products)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        drawerLayout.openDrawer(GravityCompat.START)
        return true
//        return super.onOptionsItemSelected(item)
    }
}
