package com.raunakgarments

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raunakgarments.model.Product
import kotlinx.android.synthetic.main.fragment_products_new.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductFragmentNew() : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvProducts = view.findViewById<RecyclerView>(R.id.rvProducts)
        val myContext = context
        val productsLayoutManager = GridLayoutManager(context, 2)


        searchButtonNew.setOnClickListener {
            d("Anuragadding", "addding")
            val searchAdapter = ProductSearchAdapterNew()
            var products: MutableList<String> = ArrayList()
            products.add("-MBzSyExe8TNRe5t4qBS")


            if (myContext != null) {
                searchAdapter.populate("products", products, myContext)
            }
            rvProducts.adapter = searchAdapter
            rvProducts.layoutManager = productsLayoutManager
        }



        val adapter = ProductAdapterNew()
        if (myContext != null) {
            adapter.populate("products", myContext)
        }
        rvProducts.adapter = adapter
        rvProducts.layoutManager = productsLayoutManager



    }

}