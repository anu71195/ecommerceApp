package com.raunakgarments

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raunakgarments.cart.CartActivity
import com.raunakgarments.model.Product
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_product_new.*
import kotlinx.android.synthetic.main.main.*

class ProductActivityNew : AppCompatActivity() {
    lateinit var deals: Array<Product>
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mChildListener: ChildEventListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_new)
        setSupportActionBar(toolbarNew)

        var manager = supportFragmentManager
        var transaction = manager.beginTransaction()
        transaction.replace(R.id.product_main_fragment, ProductFragmentNew()).commit()
//        drawerLayoutNew.closeDrawers()
//        drawerLayoutNew.closeDrawer(Gravity.LEFT)
//        navigationViewNew.setNavigationItemSelectedListener {
//            when(it.itemId) {
//                R.id.actionHome -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.product_main_fragment, ProductFragmentNew()).commit()
//                    d("abc", "Home was pressed")
//                }
//                R.id.actionJeans -> {
////                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, JeansFragment()).commit()
//                    d("abc", "Shorts was pressed")
//                }
//                R.id.actionShorts -> d("abc", "Shorts was pressed")
//
//                R.id.actionAdmin -> {
////                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AdminFragment()).commit()
//                    d("abc", "Shorts was pressed")
//                }
//            }
//            it.isChecked = true
//            drawerLayoutNew.closeDrawers()
//            true
//        }

//        supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
//        }

    }
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        if(item.itemId == R.id.actionCart) {
////            d("anurag", "going to cart")
////            startActivity(Intent(this, CartActivity::class.java))
////            return true
////        }
//        drawerLayout.openDrawer(GravityCompat.START)
//        return true
//        return super.onOptionsItemSelected(item)
//    }
////
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }
//
}