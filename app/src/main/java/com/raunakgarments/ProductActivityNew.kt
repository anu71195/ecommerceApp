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
import kotlinx.android.synthetic.main.main.*

class ProductActivityNew : AppCompatActivity() {
    lateinit var deals: Array<Product>
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mChildListener: ChildEventListener
    private lateinit var logIn: Authentication
    private lateinit var navView: NavigationView
    private lateinit var navMenu: Menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_new)
        setSupportActionBar(toolbarNew)
        this.navView = findViewById(R.id.navigationViewNew)
        this.navMenu = this.navView.menu
        this.logIn = Authentication(this, findViewById<NavigationView>(R.id.navigationViewNew))

        var manager = supportFragmentManager
        var transaction = manager.beginTransaction()
        transaction.replace(R.id.product_main_fragment, ProductFragmentNew(this)).commit()
        navigationViewNew.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_main_actionHome -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.product_main_fragment, ProductFragmentNew(this)).commit()
                    d("Home", "Home was pressed")
                }
                R.id.menu_main_actionProfile -> {
                    d("Profile", "Profile was pressed")
                    var intent = Intent(this, ProfileActivity::class.java)
                    this.startActivity(intent)
                }
                R.id.menu_main_actionSettings -> {
                    d("Settings", "Settings was pressed")
                    var intent = Intent(this, SettingsActivity::class.java)
                    this.startActivity(intent)
                }
                R.id.menu_main_actionAdmin -> {
                    var intent = Intent(this ,AdminProductActivityNew::class.java)
                    intent.putExtra("flow", "startFlow")
                    this.startActivity(intent)
                }
                R.id.menu_main_actioncontactUs -> {

                }
                R.id.menu_main_actionCloseNavigationDrawer -> {
                    drawerLayoutNew.closeDrawers()
                }
            }
            it.isChecked = true
            drawerLayoutNew.closeDrawers()
            true
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionCart) {
            d("anurag", "going to cart")
            startActivity(Intent(this, UserCartActivity::class.java))
            return true
        } else if (item.itemId == R.id.actionLogOut) {
            d("Logout", "User logged out")
            this.logIn.signOut()
            return true
        }
        drawerLayoutNew.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onPause() {
        super.onPause()
        this.logIn.detachListener()
    }

    override fun onResume() {
        super.onResume()
        this.logIn.attachListener()
    }
}