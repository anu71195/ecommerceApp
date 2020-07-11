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
    lateinit var link: String
    lateinit var description: String
    lateinit var ref: String
    lateinit var firebaseUtil: FirebaseUtil
    lateinit var mFirebaseDatebase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitButtonAdmin.setOnClickListener{
            this.title = productTitleAdmin.text.toString()
            this.price = productPriceAdmin.text.toString().toDouble()
            this.link = "https://www.amazon.in/images/I/51LQJZu0fYL._AC_UL320_.jpg"
            this.description = productDescriptionAdmin.text.toString()
            this.ref = "products"
            this.firebaseUtil = FirebaseUtil()
            firebaseUtil.openFbReference(ref)
            this.mFirebaseDatebase = firebaseUtil.mFirebaseDatabase
            this.mDatabaseReference = firebaseUtil.mDatabaseReference
            saveDeal()

            d("Anurag", "Button pressed: with text $title")
            d("Anurag", "Button pressed: with text $price")
            d("Anurag", "Button pressed: with text $link")
            d("Anurag", "Button pressed: with text $description")

//            doAsync {
//
//                val db = Room.databaseBuilder(
//                    requireActivity().applicationContext,
//                    AppDatabase::class.java, "productDatabase"
//                ).build()
//
//                db.productDao().InsertAll(DatabaseProduct(null,title.toString(), 12.99))
//
//                uiThread {
//                    d("Anurag", "added $title")
//                }
//            }
        }
    }

    private fun saveDeal() {
        var product = Product()
        product.populate(title, 23.0, link, description)
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