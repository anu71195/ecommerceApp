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
class AdminProductFragmentNew(productActivityNew: ProductActivityNew) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products_new_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvProductsAdmin = view.findViewById<RecyclerView>(R.id.rvProductsAdmin)
        val adapter = AdminProductAdapterNew()
        val myContext = context

        if (myContext != null) {
            adapter.populate("products", myContext)
        }
        rvProductsAdmin.adapter = adapter
        val productsLayoutManager = GridLayoutManager(context, 2)
        rvProductsAdmin.layoutManager = productsLayoutManager
    }
}