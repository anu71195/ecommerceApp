package com.raunakgarments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.raunakgarments.helper.ProductStockSyncHelper
import com.raunakgarments.model.ProductStockSync
import com.raunakgarments.model.Profile
import kotlinx.android.synthetic.main.fragment_user_cart_activity_rv.*
import java.text.SimpleDateFormat
import java.util.*

class UserCartActivityrvFragment(context: Context) : Fragment() {

    var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAuth = FirebaseAuth.getInstance()
        this.userId = mFirebaseAuth.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_cart_activity_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_user_cart_activity_progessBar.visibility = View.GONE
        settingUpRecyclerView(view)
        checkOutButtonClickListener()

    }

    private fun settingUpRecyclerView(view: View) {
        val totalCostView = view.findViewById<TextView>(R.id.fragment_user_cart_activity_totalPrice)
        val adapter = UserCartAdapter()
        context?.let { adapter.populate("userCart/" + this.userId, it, totalCostView) }
        fragment_user_cart_activity_rv.adapter = adapter
        val productsLayoutManager = GridLayoutManager(context, 1)
        fragment_user_cart_activity_rv.layoutManager = productsLayoutManager
    }

    private fun checkOutButtonClickListener() {
        fragment_user_cart_activity_checkoutButton.setOnClickListener {
            var checkButtonClicked = true
            var firebaseUtil = FirebaseUtil()
            firebaseUtil.openFbReference("userProfile/")
            firebaseUtil.mDatabaseReference.child(userId)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (checkButtonClicked) {
                            d("deliverable log", "${snapshot.key} ${snapshot.value}")
                            var profile = snapshot.getValue(Profile::class.java)
                            checkConditionsForCheckout(profile)
                        }
                    }
                })
        }
    }

    fun checkConditionsForCheckout(profile: Profile?) {
        mFirebaseAuth.currentUser?.reload()
        var emailVerified = mFirebaseAuth.currentUser?.isEmailVerified!!
        d(
            "checkoutActivity",
            FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
        )
        FirebaseAuth.getInstance().currentUser?.reload()?.addOnCompleteListener {
            // todo also check if cart is not empty
            if (profile != null &&
                profile.deliverable &&
                emailVerified &&
                profile.address != "" &&
                FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() != "" &&
                FirebaseAuth.getInstance().currentUser?.phoneNumber != null &&
                FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() != null
            ) {
                processBeforeCallingCheckoutActivity(profile, userId)
            } else {
                paymentErrorPopup()
            }
        }
    }

    private fun processBeforeCallingCheckoutActivity(profile: Profile, userID: String) {
        getLocks(profile, userID)
    }

    private fun callCartConfirmationActivity(
        lockedProducts: HashMap<String, Int>,
        profile: Profile,
        userID: String
    ) {
        var intent = Intent(activity, CartConfirmationActivity::class.java)
        activity?.startActivity(intent)
//        var intent =
//            Intent(activity, CheckoutActivity::class.java)
//        intent.putExtra("userID", userID)
//        activity?.startActivity(intent)
    }

    private fun getLocks(profile: Profile, userID: String) {
        fragment_user_cart_activity_progessBar.visibility = View.VISIBLE
        var lockedProducts = HashMap<String, Int>()

        var userCartFirebaseUtil: FirebaseUtil = FirebaseUtil()
        userCartFirebaseUtil.openFbReference("userCart/" + FirebaseAuth.getInstance().uid.toString())

        var productStockSyncFirebaseUtil = FirebaseUtil()
        productStockSyncFirebaseUtil.openFbReference("productStockSync")

        userCartFirebaseUtil.mDatabaseReference.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.value != null) {
                    d("checkoutas", snapshot.value.toString())
                    var productStockSyncHashmap = snapshot.value as HashMap<String, Int>
                    /*todo if productstocksynchasmap;size is zero*/
                    for (productId in productStockSyncHashmap) {
                        productStockSyncFirebaseUtil.mDatabaseReference.child(productId.key)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        var productStockSync =
                                            snapshot.getValue(ProductStockSync::class.java)
                                        if (productStockSync?.stock!! >= productId.value) {
                                            if (productStockSync.locked == "-1" || checkTimeStampStatus(
                                                    productStockSync.timeStamp
                                                ) || productStockSync.locked == FirebaseAuth.getInstance().uid.toString()
                                            ) {
                                                /* locked product array*/
                                                d("checkout", "entered")
                                                productStockSync.locked =
                                                    FirebaseAuth.getInstance().uid.toString()


                                                val istTime =
                                                    SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
                                                istTime.timeZone =
                                                    TimeZone.getTimeZone("Asia/Kolkata")

                                                productStockSync.dateStamp = istTime.format(Date())
                                                productStockSync.timeStamp =
                                                    ((Date().time) / 1000).toString()

                                                ProductStockSyncHelper().setValueInChild(
                                                    snapshot.key.toString(),
                                                    productStockSync
                                                )

                                                lockedProducts[snapshot.key.toString()] = 1

                                            } else {
                                                /*stock is locked*/
                                                lockedProducts[snapshot.key.toString()] = -1
                                                d("checkout", "not entered")
                                                Toast.makeText(
                                                    activity,
                                                    " lock not available",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                        } else {
//                                            stock not available
                                            lockedProducts[snapshot.key.toString()] = -2
                                            d("checkout", "not entered")
                                            Toast.makeText(
                                                activity,
                                                " lock not available",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                    } else {
                                        /*product is not available*/
                                        lockedProducts[productId.key] = -4
                                    }

                                    if (productStockSyncHashmap.size == lockedProducts.size) {
                                        Handler().postDelayed({
                                            checkForLockUser(
                                                lockedProducts, profile, userID
                                            )
                                        }, 5000)
                                    }


                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                    d("checkout", lockedProducts.toString())

                }

            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun checkForLockUser(
        lockedProducts: HashMap<String, Int>,
        profile: Profile,
        userID: String
    ) {
        /*todo check locks again*/
        d("checkout", lockedProducts.toString())
        var productCounter = 0
        for (productSync in lockedProducts) {
            var productId = productSync.key
            var productStockSyncFirebaseUtil = FirebaseUtil()
            productStockSyncFirebaseUtil.openFbReference("productStockSync")
            productStockSyncFirebaseUtil.mDatabaseReference.child(productId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var productStockSync =
                                snapshot.getValue(ProductStockSync::class.java)
                            if (productStockSync != null) {
                                if (productStockSync.locked == FirebaseAuth.getInstance().uid.toString()) {
                                    lockedProducts[productId] = 1
                                } else {
                                    /*product lock is not available*/
                                    lockedProducts[productId] = -3
                                }
                            }
                        } else {
                            /*product is not available*/
                            lockedProducts[productId] = -4
                        }
                        productCounter += 1
                        d("checkoutoutside", "${productCounter}")

                        d("checkoutoutside", lockedProducts.toString())
                        if (productCounter == lockedProducts.size) {
                            d("checkout", "Checked all products")
                            d("checkoutinside", lockedProducts.toString())
                            fragment_user_cart_activity_progessBar.visibility = View.GONE
                            Handler().postDelayed({
                                callCartConfirmationActivity(
                                    lockedProducts,
                                    profile,
                                    userID
                                )
                            }, 1000)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    private fun checkTimeStampStatus(timeStamp: String): Boolean {
        return ((((Date().time) / 1000) - timeStamp.toLong()) > 600)
    }

    private fun paymentErrorPopup() {
        if (context != null) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Can't Checkout?")
            builder.setMessage("Please check if your email and phone Number is verified, pincode is deliverable and address is present from Profile section.")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("OK") { dialogInterface, which ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

}

