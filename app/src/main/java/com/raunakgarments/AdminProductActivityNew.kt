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
        var flow = intent.getStringExtra("flow")

        if (flow == "startFlow") {
            transaction.replace(R.id.product_main_fragment_admin, AdminFragment(this)).commit()
        } else if (flow == "deleteFlow") {
            transaction.replace(R.id.product_main_fragment_admin, AdminProductFragmentNew(this))
                .commit()
        } else if (flow == "updateFlow") {
            transaction.replace(R.id.product_main_fragment_admin, AdminProductFragmentNew(this))
                .commit()
        } else {
            transaction.replace(R.id.product_main_fragment_admin, AdminFragment(this)).commit()
        }
        navigationViewNewAdmin.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_main_admin_actionAllProducts -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.product_main_fragment_admin,
                            AdminProductFragmentNew(this)
                        )
                        .commit()
                }
                R.id.menu_main_admin_actionHome -> {
                    var intent = Intent(this, ProductActivityNew::class.java)
                    this.startActivity(intent)
                }
                R.id.menu_main_admin_actionAdmin -> {
                    var intent = Intent(this, AdminProductActivityNew::class.java)
                    this.startActivity(intent)
                }
                R.id.menu_main_admin_actionOrders -> {

                }
                R.id.menu_main_admin_actionStats -> {

                }
                R.id.menu_main_admin_actionFunctions -> {
                    var intent = Intent(this, AdminFunctionsActivity::class.java)
                    this.startActivity(intent)
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