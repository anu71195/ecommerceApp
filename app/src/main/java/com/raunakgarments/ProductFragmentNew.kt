package com.raunakgarments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductFragmentNew : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvProducts = view.findViewById<RecyclerView>(R.id.rvProducts)
        val adapter = ProductAdapterNew()
        val myContext = context

        if (myContext != null) {
            adapter.populate("products", myContext)
        }
        rvProducts.adapter = adapter
        val dealsLayoutManager = GridLayoutManager(context, 2)
        rvProducts.layoutManager = dealsLayoutManager
    }
}