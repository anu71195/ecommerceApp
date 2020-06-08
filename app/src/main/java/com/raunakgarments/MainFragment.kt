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
import com.google.gson.Gson
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
            val json = URL("https://finepointmobile.com/data/products.json").readText()

            uiThread {
                d("anurag","json: $json")
                val products = Gson().fromJson(json, Array<Product>::class.java).toList()

                root.recyler_view.apply {
                    layoutManager = GridLayoutManager(activity, 2)
                    adapter = ProductAdapter(products)
                    root.progressBar.visibility = View.GONE
                }
            }
        }

        val categories = listOf("jeans", "Socks", "Suits", "Skirts", "Dresses", "daniel")

        root.categoriesRecylerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = CategoriesAdapter(categories)
        }
        return root
    }


}

