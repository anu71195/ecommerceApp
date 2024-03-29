package com.raunakgarments.NU//package com.raunakgarments
//
//import android.content.Intent
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.raunakgarments.ProductAdapter.ViewHolder
//import com.raunakgarments.model.Product
//import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.product_row.view.*
//
//import java.io.File
//
//class ProductAdapter(
//    private val products: List<Product>,
//    private val onClickProduct: (title: String, photoUrl: String, price: String, photoView: View) -> Unit
//) :
//    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val product = products[position]
//        Picasso.get().load(product.photoUrl).into(holder.image)
//        holder.title.text = product.title
//        holder.price.text = "\u20B9" + product.price.toString()
//        if (product.isOnSale) {
//            holder.saleImageView.visibility = View.VISIBLE
//        } else {
//            holder.saleImageView.visibility = View.GONE
//        }
//        holder.image.setOnClickListener {
//            onClickProduct.invoke(product.title, product.photoUrl, product.price.toString(), holder.image)
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_row, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun getItemCount() = products.size
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image: ImageView = itemView.findViewById(R.id.photo)
//        val title: TextView = itemView.findViewById(R.id.title)
//        val price: TextView = itemView.findViewById(R.id.price)
//        val saleImageView: ImageView = itemView.saleImageView
//    }
//}