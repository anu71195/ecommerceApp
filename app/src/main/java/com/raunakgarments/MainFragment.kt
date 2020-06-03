package com.raunakgarments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.raunakgarments.model.Product
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val products = arrayListOf<Product>()
        val imageUrl = "https://image.spreadshirtmedia.com/image-server/v1/products/T812A366PA3140PT17X50Y30D12906314FS9045CxFFFFFF/views/2,width=650,height=650,appearanceId=366,backgroundColor=f1f1f1/youve-got-the-keyboard-now-get-the-t-shirt-this-is-the-original-red-t-shirt-from-nord-keyboards-official-clothing-line.jpg"
        for(i in 0..100) {
            products.add(Product(title = "red colored cotton t-shirt #$i", photoUrl = imageUrl, price = 1.99))
        }

        root.recyler_view.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = ProductAdapter(products)
        }
        return root
    }


}

