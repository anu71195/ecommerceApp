package com.raunakgarments

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.Gson
import com.raunakgarments.database.AppDatabase
import com.raunakgarments.database.DatabaseProduct
import com.raunakgarments.model.Product
import com.raunakgarments.repos.ProductsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_main.view.categoriesRecylerView
import kotlinx.android.synthetic.main.product_row.view.*
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

        val categories = listOf(
            "jeans",
            "Socks",
            "Suits",
            "Skirts",
            "Dresses",
            "denims",
            "pants",
            "Jackets",
            "shorts",
            "payjamas"
        )

        root.categoriesRecylerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = CategoriesAdapter(categories)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productsRepository = ProductsRepository().getAllProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                d("Anurag", "success")
                recyler_view.apply {
                    layoutManager = GridLayoutManager(activity, 2)
                    adapter = ProductAdapter(it) { extraTitle, extraImageUrl, extraPrice, photoView ->
                        val intent = Intent(activity, ProductDetails::class.java)
                        intent.putExtra("title", extraTitle)
                        intent.putExtra("price", extraPrice)
                        intent.putExtra("imageURL", extraImageUrl)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity as AppCompatActivity, photoView, "photoToAnimate")
                        startActivity(intent, options.toBundle())
                    }
                }
                progressBar.visibility = View.GONE
            }, {
                d("anurag", " error :( ${it.message}")
            })
    }
}

