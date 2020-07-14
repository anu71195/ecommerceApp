package com.raunakgarments

import android.accounts.AuthenticatorDescription
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raunakgarments.database.AppDatabase
import com.raunakgarments.database.DatabaseProduct
import com.raunakgarments.model.Product
import kotlinx.android.synthetic.main.fragment_admin.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AdminFragment : Fragment() {

    lateinit var title: String
    var price: Double = 0.0
    var link = "https://visualsound.com/wp-content/uploads/2019/05/unavailable-image.jpg"
    var description: String = ""
    lateinit var ref: String
    lateinit var firebaseUtil: FirebaseUtil
    lateinit var mFirebaseDatebase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_admin, container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitButtonAdmin.setOnClickListener {
            if (productTitleAdmin.text.toString() == "" || productPriceAdmin.text.toString() == "") {
                clean()
            } else {
                this.title = productTitleAdmin.text.toString()
                this.price = productPriceAdmin.text.toString().toDouble()
                if (productDescriptionAdmin.text.toString() != "") {
                    this.description = productDescriptionAdmin.text.toString()
                }
                if (productImageLinkAdmin.text.toString() != "") {
                    this.link = productImageLinkAdmin.text.toString()
                }

                this.ref = "products"
                this.firebaseUtil = FirebaseUtil()
                firebaseUtil.openFbReference(ref)
                this.mFirebaseDatebase = firebaseUtil.mFirebaseDatabase
                this.mDatabaseReference = firebaseUtil.mDatabaseReference
                saveDeal()
            }
        }
    }

    private fun saveDeal() {
        var product = Product()
        product.populate(title, price, link, description)
        mDatabaseReference.push().setValue(product)
        clean()
    }

    private fun clean() {
        productTitleAdmin.setText("")
        productPriceAdmin.setText("")
        productDescriptionAdmin.setText("")
        productImageLinkAdmin.setText("")
        productTitleAdmin.requestFocus()
    }
}