package com.raunakgarments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.raunakgarments.global.UserCartSingletonClass
import com.raunakgarments.model.Profile
import kotlinx.android.synthetic.main.fragment_cart_confirmation_activity_rv.*

class CartConfirmationActivityrvFragment(context: Context, intent: Intent) : Fragment() {
    var activityIntent: Intent = intent
    val adapter = CartConfirmationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart_confirmation_activity_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartConfirmationActivityrvFragmentStartBookkeeping()

        var profile =
            Gson().fromJson<Profile>(activityIntent.getStringExtra("profile"), Profile::class.java)
        var lockedProducts =
            activityIntent.getSerializableExtra("lockedProducts") as HashMap<String, Int>

        confirmOrderButtonClickListener()
        settingUpRecyclerView(view, profile, lockedProducts)
    }

    private fun cartConfirmationActivityrvFragmentStartBookkeeping() {
        fragment_cart_confirmation_activity_confirmButton.isEnabled = true
    }

    private fun confirmOrderButtonClickListener() {
        fragment_cart_confirmation_activity_confirmButton.setOnClickListener {
            d("CartConfirmationActivityrvFragment", "confirmOrderButtonClickListener - confirm button clicked")
            fragment_cart_confirmation_activity_confirmButton.isEnabled = false
            callCheckoutActivity(adapter.totalCartCost)
        }
    }

    private fun callCheckoutActivity(totalCartCost: Double) {
        var intent =
            Intent(activity, CheckoutActivity::class.java)
        UserCartSingletonClass.confirmationCartProductArray = adapter.confirmationCartProductArray
        intent.putExtra("userID", FirebaseAuth.getInstance().uid.toString())
        intent.putExtra("totalCartCost", totalCartCost)
        d("totalcartcost", totalCartCost.toString())
        activity?.startActivity(intent)
    }

    private fun settingUpRecyclerView(
        view: View,
        profile: Profile,
        lockedProducts: HashMap<String, Int>
    ) {
        val totalCostView =
            view.findViewById<TextView>(R.id.fragment_cart_confirmation_activity_totalPrice)
        adapter.populate(
            "userCart/" + FirebaseAuth.getInstance().uid.toString(),
            profile,
            lockedProducts,
            totalCostView
        )
        fragment_cart_confirmation_activity_rv.adapter = adapter
        val productsLayoutManager = GridLayoutManager(context, 1)
        fragment_cart_confirmation_activity_rv.layoutManager = productsLayoutManager
    }
}