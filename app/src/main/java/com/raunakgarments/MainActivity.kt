package com.raunakgarments

import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.raunakgarments.database.AppDatabase
import com.raunakgarments.database.DatabaseProduct
import com.raunakgarments.model.Product

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        doAsync {

            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "productDatabase"
            ).build()

            db.productDao().InsertAll(DatabaseProduct(null,"Socks - one dozen", 1.99))
            val products = db.productDao().getAll()

            uiThread {
                d("Anurag", "products size? ${products.size} ${products[0].title}")
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, MainFragment()).commit()
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.actionHome -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, MainFragment()).commit()
                }
                R.id.actionJeans -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, JeansFragment()).commit()
                }
                R.id.actionShorts -> d("abc", "Shorts was pressed")
            }
            it.isChecked = true
            drawerLayout.closeDrawers()
            true
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        drawerLayout.openDrawer(GravityCompat.START)
        return true
//        return super.onOptionsItemSelected(item)
    }
}
