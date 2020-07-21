package com.raunakgarments

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raunakgarments.cart.CartActivity
import com.raunakgarments.model.Product
import kotlinx.android.synthetic.main.activity_product_new.*
import kotlinx.android.synthetic.main.activity_product_new_admin.*
import kotlinx.android.synthetic.main.main.*

class AdminProductActivityNew : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_new_admin)

        var manager = supportFragmentManager
        var transaction = manager.beginTransaction()
        transaction.replace(R.id.product_main_fragment_admin, AdminFragment(this)).commit()
        navigationViewNewAdmin.setNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.actionHome -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.product_main_fragment, ProductFragmentNew(this)).commit()
//                    d("Home", "Home was pressed")
//                }
//                R.id.actionProfile -> {
//                    d("Profile", "Profile was pressed")
//                }
//                R.id.actionSettings -> {
//                    d("Settings", "Settings was pressed")
//                }
//                R.id.actionAdmin -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.product_main_fragment, AdminFragment(this)).commit()
//                }
                R.id.menu_main_admin_actionHome -> {
                    var intent = Intent(this, ProductActivityNew::class.java)
                    this.startActivity(intent)
                }
                R.id.menu_main_admin_actionAdmin -> {
                    var intent = Intent(this, AdminProductActivityNew::class.java)
                    this.startActivity(intent)
//                    transaction.replace(R.id.product_main_fragment_admin, AdminFragment(this))
//                        .commit()
                }
                R.id.menu_main_admin_actionCloseNavigationDrawer -> {
                    activity_product_new_admin_drawerLayoutNew.closeDrawers()
                }
            }
            it.isChecked = true
            activity_product_new_admin_drawerLayoutNew.closeDrawers()
            true
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        activity_product_new_admin_drawerLayoutNew.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }
}