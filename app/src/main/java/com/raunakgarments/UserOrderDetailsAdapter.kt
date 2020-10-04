package com.raunakgarments

import android.content.Intent
import android.util.DisplayMetrics
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.raunakgarments.model.ConfirmationCartProduct
import com.raunakgarments.model.UserOrders
import com.squareup.picasso.Picasso

class UserOrderDetailsAdapter :
    RecyclerView.Adapter<UserOrderDetailsAdapter.UserOrderDetailsViewHolder>() {

    private lateinit var activityIntent: Intent
    private lateinit var userOrders: UserOrders
    var productList: MutableList<ConfirmationCartProduct> = ArrayList()
    private lateinit var userOrderDetailsActivity: UserOrderDetailsActivity

    fun populate(intent: Intent, userOrderDetailsActivity: UserOrderDetailsActivity) {
        this.activityIntent = intent
        this.userOrderDetailsActivity = userOrderDetailsActivity

        this.userOrders =
            Gson().fromJson<UserOrders>(intent.getStringExtra("userOrders"), UserOrders::class.java)
        for (orderedProduct in userOrders.orders) {
            productList.add(orderedProduct.value)
        }
        Log.d(
            "UserOrderDetailsAdapter",
            "populate-${Gson().toJson(userOrders)}"
        )
        Log.d(
            "UserOrderDetailsAdapter",
            "populate-${Gson().toJson(productList)}"
        )

    }

    class UserOrderDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var productTitle: TextView = itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_productTitleTextView)
        var productImage: ImageView = itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_productImage)
        var totalPriceTv: TextView = itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_productTotalPriceTextView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserOrderDetailsViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_user_order_details_adapter_user_orders_row, parent, false)
        return UserOrderDetailsViewHolder(itemView)
    }


    override fun onBindViewHolder(
        holder: UserOrderDetailsViewHolder,
        position: Int
    ) {
        holder.productTitle.text = productList[position].title
        Picasso.get().load(productList[position].photoUrl).into(holder.productImage)
        holder.productImage.layoutParams.width = getScreenWidth()/3
        holder.totalPriceTv.text = productList[position].price.toString() + " X " + productList[position].quantity + " = " + productList[position].totalPrice
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        userOrderDetailsActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}