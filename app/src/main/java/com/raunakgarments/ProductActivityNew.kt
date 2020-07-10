package com.raunakgarments

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        transaction.replace(R.id.product_main_fragment, FirstFragment()).commit()
//        var rvProducts = findViewById<RecyclerView>(R.id.rvProducts)
//        val adapter = ProductAdapterNew()
//        adapter.populate("products", this)
//        rvProducts.adapter = adapter
//        var dealsLayoutManager = GridLayoutManager(this, 2)
//        rvProducts.layoutManager = dealsLayoutManager






//        setSupportActionBar(findViewById(R.id.toolbar))
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}