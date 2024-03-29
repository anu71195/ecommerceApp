package com.raunakgarments.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.R
import kotlinx.android.synthetic.main.fragment_products_new_admin.*
import kotlinx.android.synthetic.main.fragment_products_new_admin.searchButtonNewAdmin

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AdminProductFragmentNew(productActivityNew: AdminProductActivityNew) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products_new_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_products_new_admin_progressBar.visibility = View.VISIBLE
        val rvProductsAdmin = view.findViewById<RecyclerView>(R.id.rvProductsAdmin)
        val myContext = context
        val productsLayoutManager = GridLayoutManager(context, 2)

        rvProductsAdmin.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val totalItemCount = rvProductsAdmin!!.layoutManager?.itemCount
                val lastVisibleItemPosition = productsLayoutManager.findLastVisibleItemPosition()
                Log.d("MyTAG", "Load new list not entered")

                if (totalItemCount != lastVisibleItemPosition) {
                    Log.d("MyTAG", "Load new list")
                    if (fragment_products_new_admin_progressBar.visibility == View.GONE) {
                        fragment_products_new_admin_progressBar.visibility = View.VISIBLE
                        android.os.Handler().postDelayed({
                            fragment_products_new_admin_progressBar.visibility = View.GONE
                        }, 3000)
                    }
                }
            }
        })

        searchButtonNewAdmin.setOnClickListener {
            Log.d("Anuragadding", "addding")

            fragment_products_new_admin_progressBar.visibility = View.VISIBLE

            var searchTextReworked = searchTermNewAdmin.text.toString()
            var tagList = searchTextReworked.split(" ", ",")
            val re = Regex("[^A-Za-z0-9]")

            Log.d("searchText", "$searchTextReworked")
            var products: MutableList<String> = ArrayList()
            var searchList: MutableList<String> = ArrayList()

            for(tag in tagList) {
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
                                Log.d("productTags", snapshot.key)
                                Log.d("productTags", snapshot.value.toString())
                                if (snapshot.exists()) {
                                    Log.d("productTags", "snapshotExists")
                                    var productIds = snapshot.value
                                    for (productId in productIds as HashMap<String, Int>) {
                                        Log.d(
                                            "productTags",
                                            "${productId.key} and ${productId.value}"
                                        )
                                        products.add(productId.key)
                                        Log.d("producttagsList", products.toString())
                                    }
                                }

                                Log.d("producttagsList", products.toString())
                                Log.d("producttagsList", products.toString())
                                val searchAdapter = AdminProductSearchAdapterNew()
                                if (myContext != null) {
                                    searchAdapter.populate("products", products, myContext, fragment_products_new_admin_progressBar, rvProductsAdmin, productsLayoutManager)
                                }
                                rvProductsAdmin.adapter = searchAdapter
                                rvProductsAdmin.layoutManager = productsLayoutManager

                            }
                        })
                }
            }
            val adapter = AdminProductAdapterNew()
            if (myContext != null) {
                adapter.populate("products", myContext, fragment_products_new_admin_progressBar, rvProductsAdmin, productsLayoutManager)
            }
            rvProductsAdmin.adapter = adapter
            rvProductsAdmin.layoutManager = productsLayoutManager
        }

        val adapter = AdminProductAdapterNew()
        if (myContext != null) {
            adapter.populate(
                "products",
                myContext,
                fragment_products_new_admin_progressBar,
                rvProductsAdmin,
                productsLayoutManager
            )
        }
        rvProductsAdmin.adapter = adapter
        rvProductsAdmin.layoutManager = productsLayoutManager
    }
}