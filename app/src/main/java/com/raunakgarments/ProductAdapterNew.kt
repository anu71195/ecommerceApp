package com.raunakgarments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.raunakgarments.model.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_row.view.*

class ProductAdapterNew : RecyclerView.Adapter<ProductAdapterNew.DealViewHolder>() {

    var products: MutableList<Product> = ArrayList()
    private lateinit var mFirebaseDatebase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener
    private lateinit var listener: (Product) -> Unit
    private lateinit var context: Context

    fun populate(ref: String, context: Context) {
        var firebaseUtil: FirebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference(ref)
        mFirebaseDatebase = firebaseUtil.mFirebaseDatabase
        mDatabaseReference = firebaseUtil.mDatabaseReference
        childEventListener = object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var td = snapshot.getValue(Product::class.java)
                if (td != null) {
                    td.id = snapshot.key.toString()
                    d(td.price.toString(),"lksd")
                    products.add(td)
                    notifyItemInserted(products.size-1)
                }
            }
        }
        mDatabaseReference.addChildEventListener(childEventListener)
        this.context = context
    }

    public class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.title)
        var image: ImageView = itemView.findViewById(R.id.photo)
        val title: TextView = itemView.findViewById(R.id.title)
        var price: TextView = itemView.findViewById(R.id.price)
    }

    private fun rvItemSegue(title: String, price: Double, imageUrl: String, photoView: View) {
        var intent = Intent(context ,ProductDetails::class.java)
        intent.putExtra("title", title)
        intent.putExtra("price", price)
        intent.putExtra("imageUrl", imageUrl)
        context.startActivity(intent)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        var context = parent.context
        var itemView = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false)
        return DealViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        var product = products[position]
        holder.tvTitle.setText(product.title)
        holder.price.text = "\u20b9" + product.price
        Picasso.get().load(product.photoUrl).into(holder.image)
        holder.itemView.setOnClickListener { rvItemSegue(product.title, product.price, product.photoUrl, holder.image) }
    }
}