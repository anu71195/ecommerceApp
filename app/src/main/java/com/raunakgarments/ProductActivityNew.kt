package com.raunakgarments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raunakgarments.model.Product

class ProductActivityNew : AppCompatActivity() {
    lateinit var deals: Array<Product>
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mChildListener: ChildEventListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_new)

        var manager = supportFragmentManager
        var transaction = manager.beginTransaction()
        transaction.replace(R.id.product_main_fragment, ProductFragmentNew()).commit()

    }
}