package com.raunakgarments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.raunakgarments.database.AppDatabase
import com.raunakgarments.database.DatabaseProduct
import kotlinx.android.synthetic.main.fragment_admin.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AdminFragment : Fragment() {
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
            val title = productTitleAdmin.text.toString()
            val price = productPriceAdmin.text.toString().toDouble()
            val link = productImageLinkAdmin.text.toString()
            val description = productDescriptionAdmin.text.toString()
            d("Anurag", "Button pressed: with text $title")
            d("Anurag", "Button pressed: with text $price")
            d("Anurag", "Button pressed: with text $link")
            d("Anurag", "Button pressed: with text $description")
            print(title)
            print(price)
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
}