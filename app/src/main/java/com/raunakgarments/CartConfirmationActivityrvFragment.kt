package com.raunakgarments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.raunakgarments.model.Profile
import kotlinx.android.synthetic.main.fragment_cart_confirmation_activity_rv.*
import java.util.HashMap

class CartConfirmationActivityrvFragment(context: Context, intent: Intent) : Fragment() {
    var activityIntent: Intent = intent
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart_confirmation_activity_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingUpRecyclerView(view)
        var profile = Gson().fromJson<Profile>(activityIntent.getStringExtra("profile"), Profile::class.java)
        var lockedProducts = activityIntent.getSerializableExtra("lockedProducts")

        d("cartconfirmation", "${lockedProducts}")
        d("cartconfirmation", "${(activityIntent.getStringExtra("profile"))}")
    }

    private fun settingUpRecyclerView(view: View) {
//        val totalCostView = view.findViewById<TextView>(R.id.fragment_user_cart_activity_totalPrice)
        val adapter = CartConfirmationAdapter()
//        context?.let { adapter.populate("userCart/" + this.userId, it, totalCostView) }
        fragment_cart_confirmation_activity_rv.adapter = adapter
        val productsLayoutManager = GridLayoutManager(context, 1)
        fragment_cart_confirmation_activity_rv.layoutManager = productsLayoutManager
    }
}