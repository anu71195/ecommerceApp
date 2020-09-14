package com.raunakgarments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_cart_confirmation_activity_rv.*
import kotlinx.android.synthetic.main.fragment_user_cart_activity_rv.*
import kotlinx.android.synthetic.main.fragment_user_cart_activity_rv.fragment_user_cart_activity_rv

class CartConfirmationActivityrvFragment(context: Context) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart_confirmation_activity_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingUpRecyclerView(view)
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