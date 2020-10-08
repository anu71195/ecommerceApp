package com.raunakgarments.developerActivities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.R
import com.raunakgarments.helper.ProductStockSyncHelper
import com.raunakgarments.model.ProductStockSync
import kotlinx.android.synthetic.main.activity_developer_admin_content_scrolling.*

class DeveloperAdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_admin)
        setSupportActionBar(findViewById(R.id.activity_developer_admin_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        developerFlowWarning()
        reUploadProductStockSyncButtonClickListener()
        syncProductStockSyncButtonClickListener()
    }

    private fun reUploadProductStockSyncButtonClickListener() {
        activity_developer_admin_content_scrolling_edit_download_upload_productStockSync.setOnClickListener {
            var productStockSyncFirebaseUtil = FirebaseUtil()
            productStockSyncFirebaseUtil.openFbReference("productStockSync")

            productStockSyncFirebaseUtil.mDatabaseReference.addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    var productStockSyncObject = snapshot.getValue(ProductStockSync::class.java)

                    snapshot.key?.let { it1 ->
                        if (productStockSyncObject != null) {
                            ProductStockSyncHelper().setValueInChild(it1, productStockSyncObject)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    // DECOMMISSIONED
    private fun syncProductStockSyncButtonClickListener() {
        activity_developer_admin_content_scrolling_edit_syncProductStockSync.setOnClickListener {
            /*
//            var productFirebaseUtil = FirebaseUtil()
//            productFirebaseUtil.openFbReference("products")
//            productFirebaseUtil.mDatabaseReference.addChildEventListener(
//                object : ChildEventListener {
//                    override fun onCancelled(error: DatabaseError) {}
//                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//                    override fun onChildRemoved(snapshot: DataSnapshot) {}
//                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                        var product = snapshot.getValue(Product::class.java)
//                        var productStockSync = ProductStockSync()
//                        if (product != null) {
//                            productStockSync.stock = product.stock
//                            ProductStockSyncHelper().setValueInChild(
//                                snapshot.key.toString(),
//                                productStockSync
//                            )
//
//                        }
//                    }
//
//                }
//            )

             */

        }
    }

    private fun developerFlowWarning() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Warning: Dangerous and Irreversible effects beyond")
        builder.setMessage("If you have access to this and don't know about its usage. Please contact the relevant person for that. Use or change of any thing beyond this point in this flow may result in severe and irreversible changes. Select Yes to go along with this flow or No to return to Admin screen.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            Toast.makeText(this, "You Pressed yes \n Please be careful", Toast.LENGTH_LONG).show()
        }
//        builder.setNeutralButton("Cancel") { dialogInterface, which ->
//            Toast.makeText(
//                this,
//                "clicked cancel\n operation cancel",
//                Toast.LENGTH_LONG
//            ).show()
//        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            Toast.makeText(this, "clicked No", Toast.LENGTH_LONG).show()
            finish()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}