package com.raunakgarments.NU//package com.raunakgarments
//
//import android.content.Intent
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityOptionsCompat
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProviders
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.raunakgarments.model.Product
//import kotlinx.android.synthetic.main.fragment_main.*
//import kotlinx.android.synthetic.main.fragment_main.view.categoriesRecylerView
//
//class MainFragment : Fragment() {
//
//    lateinit var viewModel: MainFragmentViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val root = inflater.inflate(R.layout.fragment_main, container, false)
//
//        val categories = listOf(
//            "jeans",
//            "Socks",
//            "Suits",
//            "Skirts",
//            "Dresses",
//            "denims",
//            "pants",
//            "Jackets",
//            "shorts",
//            "payjamas"
//        )
//
//        root.categoriesRecylerView.apply {
//            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
//            adapter = CategoriesAdapter(categories)
//        }
//        return root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel = ViewModelProviders.of(requireActivity()).get(MainFragmentViewModel::class.java)
//
//        viewModel.products.observe(requireActivity(), Observer {
//            loadRecyclerView(it)
//        })
//
//        viewModel.setup()
//
//        searchButton.setOnClickListener {
//            viewModel.search(searchTerm.text.toString())
////            loadRecyclerView(ProductsRepository().searchForProducts(searchTerm.text.toString()))
//        }
//
//        searchTerm.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                viewModel.search(searchTerm.text.toString())
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//        })
////        val productsRepository = ProductsRepository().getAllProducts()
//
////
////        searchButton.setOnClickListener {
////            loadRecyclerView(ProductsRepository().searchForProducts(searchTerm.text.toString()))
////        }
//    }
//
//    fun loadRecyclerView(productsRepository: List<Product>) {
//
//        recyler_view.apply {
//            layoutManager = GridLayoutManager(activity, 2)
//            adapter = ProductAdapter(productsRepository) { extraTitle, extraImageUrl, extraPrice, photoView ->
//                val intent = Intent(activity, ProductDetails::class.java)
//                intent.putExtra("title", extraTitle)
//                intent.putExtra("price", extraPrice)
////                        intent.putExtra("imageURL", extraImageUrl)
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    activity as AppCompatActivity,
//                    photoView,
//                    "photoToAnimate"
//                )
//                startActivity(intent, options.toBundle())
//            }
//        }
//        progressBar.visibility = View.GONE
//    }
//}
//
