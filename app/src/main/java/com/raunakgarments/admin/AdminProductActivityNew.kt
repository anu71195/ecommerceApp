package com.raunakgarments.admin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.raunakgarments.ProductActivityNew
import com.raunakgarments.R
import com.raunakgarments.developerActivities.DeveloperAdminActivity
import kotlinx.android.synthetic.main.activity_product_new_admin.*

class AdminProductActivityNew : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_new_admin)

        var manager = supportFragmentManager
        var transaction = manager.beginTransaction()
        var flow = intent.getStringExtra("flow")

        if (flow == "startFlow") {

            callAdminFragment(transaction)

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
                    var intent = Intent(this, AdminOrdersActivity::class.java)
                    this.startActivity(intent)
                }
                R.id.menu_main_admin_actionStats -> {

                }
                R.id.menu_main_admin_actionFunctions -> {
                    var intent = Intent(this, AdminFunctionsActivity::class.java)
                    this.startActivity(intent)
                }
                R.id.menu_main_admin_actionDeveloper -> {
                    var intent = Intent(this, DeveloperAdminActivity::class.java)
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

    private fun callAdminFragment(transaction: FragmentTransaction) {
        try {
            transaction.replace(R.id.product_main_fragment_admin, AdminFragment(this)).commit()
        } catch(e: Error) {
            Handler().postDelayed({callAdminFragment(transaction)},1000)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        activity_product_new_admin_drawerLayoutNew.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }
}