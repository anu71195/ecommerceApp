package com.raunakgarments

import android.content.Context
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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

        searchButtonClickListener(myContext, productsLayoutManager)
        val adapter = ProductAdapterNew()
        if (myContext != null) {
            adapter.populate("products", myContext)
        }
        rvProducts.adapter = adapter
        rvProducts.layoutManager = productsLayoutManager
    }

    private fun searchButtonClickListener(
        myContext: Context?,
        productsLayoutManager: GridLayoutManager
    ) {
        searchButtonNew.setOnClickListener {
            d("Anuragadding", "addding")
            var searchTextReworked = searchTermNew.text.toString()
            var tagList = searchTextReworked.split(" ", ",")
            val re = Regex("[^A-Za-z0-9]")

            d("searchText", "$searchTextReworked")
            var products: MutableList<String> = ArrayList()
            var searchList: MutableList<String> = ArrayList()

            for (tag in tagList) {
                searchList.add(re.replace(tag.toLowerCase(), ""))
            }

            for (searchText in searchList) {
                if (searchText != "") {
                    var tagFirebaseUtil = FirebaseUtil()
                    tagFirebaseUtil.openFbReference("tags")

                    tagFirebaseUtil.mDatabaseReference.child(searchText)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {}

                            override fun onDataChange(snapshot: DataSnapshot) {
                                d("productTags", snapshot.key)
                                d("productTags", snapshot.value.toString())
                                if (snapshot.exists()) {
                                    d("productTags", "snapshotExists")
                                    var productIds = snapshot.value
                                    for (productId in productIds as HashMap<String, Int>) {
                                        d("productTags", "${productId.key} and ${productId.value}")
                                        products.add(productId.key)
                                        d("producttagsList", products.toString())
                                    }
                                }

                                d("producttagsList", products.toString())
                                d("producttagsList", products.toString())
                                val searchAdapter = ProductSearchAdapterNew()
                                if (myContext != null) {
                                    searchAdapter.populate("products", products, myContext)
                                }
                                rvProducts.adapter = searchAdapter
                                rvProducts.layoutManager = productsLayoutManager

                            }
                        })

                }
            }
            val adapter = ProductAdapterNew()
            if (myContext != null) {
                adapter.populate("products", myContext)
            }
            rvProducts.adapter = adapter
            rvProducts.layoutManager = productsLayoutManager
        }
    }

}