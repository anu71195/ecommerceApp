package com.raunakgarments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.Gson
import com.raunakgarments.database.AppDatabase
import com.raunakgarments.database.DatabaseProduct
import com.raunakgarments.model.Product
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_main.view.categoriesRecylerView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        doAsync {

            val db = Room.databaseBuilder(
                requireActivity().applicationContext,
                AppDatabase::class.java, "productDatabase"
            ).build()
            val productsFromDatabase = db.productDao().getAll()

            val products = productsFromDatabase.map {
                Product(
                    it.title,
                    "https://5.imimg.com/data5/RL/WH/OR/SELLER-51723387/blank-tshirt-500x500.jpg",
                    it.price,
                    true
                )
            }
            uiThread {

                root.recyler_view.apply {
                    layoutManager = GridLayoutManager(activity, 2)
                    adapter = ProductAdapter(products)
                    root.progressBar.visibility = View.GONE
                }
            }
        }

//        root.progressBar.visibility = View.GONE

        val categories = listOf("jeans", "Socks", "Suits", "Skirts", "Dresses", "denims", "pants", "Jackets", "shorts", "payjamas")

        root.categoriesRecylerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = CategoriesAdapter(categories)
        }
        return root
    }

}

