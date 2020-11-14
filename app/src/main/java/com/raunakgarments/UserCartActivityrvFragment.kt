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
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.raunakgarments.global.UserCartSingletonClass
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.helper.ProductStockSyncHelper
import com.raunakgarments.model.*
import kotlinx.android.synthetic.main.fragment_user_cart_activity_rv.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UserCartActivityrvFragment() : Fragment() {

    var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var userId: String
    private lateinit var supportActionBar: ActionBar

    constructor(context: Context, supportActionBar: ActionBar?) : this() {
        if (supportActionBar != null) {
            this.supportActionBar = supportActionBar
        }
    }

    enum class ErrorType {
        emptyCart, email, phone, pincode, address, profile, ow
    }

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

        userCartActivityrvFragmentStartBookkeeping()
        val adapter = settingUpRecyclerView(view)
        checkOutButtonClickListener(adapter)
    }

    private fun userCartActivityrvFragmentStartBookkeeping() {
        fragment_user_cart_activity_progessBar.visibility = View.GONE
        fragment_user_cart_activity_checkoutButton.isEnabled = true
    }

    private fun settingUpRecyclerView(view: View): UserCartAdapter {
        val totalCostView = view.findViewById<TextView>(R.id.fragment_user_cart_activity_totalPrice)
        val adapter = UserCartAdapter()
        context?.let { adapter.populate("userCart/" + this.userId, it, totalCostView, this) }
        fragment_user_cart_activity_rv.adapter = adapter
        val productsLayoutManager = GridLayoutManager(context, 1)
        fragment_user_cart_activity_rv.layoutManager = productsLayoutManager
        return adapter
    }

    /*todo how to mitigate if user is spamming the products and trying to get locks again and again*/
    private fun checkOutButtonClickListener(adapter: UserCartAdapter) {
        fragment_user_cart_activity_checkoutButton.setOnClickListener {
            d("UserCartActivityrvFragment", "checkOutButtonClickListener - checkoutbutton clicked")
            fragment_user_cart_activity_checkoutButton.isEnabled = false
            adapter.notifyDataSetChanged()

            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
                setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
            }

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

            var userCartFirebaseUtil: FirebaseUtil = FirebaseUtil()
            userCartFirebaseUtil.openFbReference("userCart/" + FirebaseAuth.getInstance().uid.toString())

            userCartFirebaseUtil.mDatabaseReference.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.value != null) {
                        d("checkoutas", snapshot.value.toString())
                        var productStockSyncHashmap = snapshot.value as HashMap<String, Int>
                        d(
                            "usercartactivityrvfragment",
                            "checkconditionsforcheckout ${productStockSyncHashmap.size}, ${productStockSyncHashmap}"
                        )
                        if (productStockSyncHashmap.size > 0) {
                            checkAndValidateUserProfile(profile, emailVerified)

                        } else {
                            paymentErrorPopup(listOf(ErrorType.emptyCart))
                        }
                    } else {
                        paymentErrorPopup(listOf(ErrorType.emptyCart))
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })


        }
    }

    private fun checkAndValidateUserProfile(profile: Profile?, emailVerified: Boolean) {

//        emptyCart, email, phone, pincode, address, profile,  ow

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
            var errorList: MutableList<ErrorType> = ArrayList()

            if (profile != null) {
                if (!profile.deliverable) {
                    errorList.add(ErrorType.pincode)
                }

                if (!emailVerified) {
                    errorList.add(ErrorType.email)
                }

                if (profile.address.toString() == "" || profile.address == null) {
                    errorList.add(ErrorType.address)
                }

                if (FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() == "" || FirebaseAuth.getInstance().currentUser?.phoneNumber == null || FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() == null) {
                    errorList.add(ErrorType.phone)
                }

            } else {
                errorList.add(ErrorType.profile)
            }

            paymentErrorPopup(errorList)
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
        intent.putExtra("lockedProducts", lockedProducts)
        intent.putExtra("profile", Gson().toJson(profile))
        activity?.startActivity(intent)
    }

    private fun getLocks(profile: Profile, userID: String) {
        fragment_user_cart_activity_progessBar.visibility = View.VISIBLE
        var lockedProducts = HashMap<String, Int>()

        var userCartFirebaseUtil: FirebaseUtil = FirebaseUtil()
        userCartFirebaseUtil.openFbReference("userCart/" + FirebaseAuth.getInstance().uid.toString())

        var productStockSyncFirebaseUtil = FirebaseUtil()
        productStockSyncFirebaseUtil.openFbReference("productStockSync")

        var productStockSyncAdminFirebaseUtil = FirebaseUtil()
        productStockSyncAdminFirebaseUtil.openFbReference("productStockSyncAdminLock")

        userCartFirebaseUtil.mDatabaseReference.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.value != null) {
                    d("checkoutas", snapshot.value.toString())
                    var productStockSyncHashmap = snapshot.value as HashMap<String, Int>
                    if (productStockSyncHashmap.size > 0) {
                        for (productId in productStockSyncHashmap) {
                            productStockSyncFirebaseUtil.mDatabaseReference.child(productId.key)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            var productStockSync =
                                                snapshot.getValue(ProductStockSync::class.java)
                                            if (checkInStock(productStockSync, productId)) {
                                                if (productStockSync != null) {
                                                    if (checkConditionsForLock(productStockSync)
                                                    ) {
                                                        /* stock avaiilable*/

                                                        productStockSyncAdminFirebaseUtil.mDatabaseReference.child(
                                                            productId.key
                                                        ).addListenerForSingleValueEvent(object :
                                                            ValueEventListener {
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                if (snapshot.exists()) {

                                                                    var productStockSyncAdminLock =
                                                                        snapshot.getValue(
                                                                            ProductStockSyncAdminLock::class.java
                                                                        )
                                                                    d(
                                                                        "UserCartActivityrvFragment",
                                                                        "getLocks - ${productStockSyncAdminLock}"
                                                                    )
                                                                    d(
                                                                        "UserCartActivityrvFragment",
                                                                        "getLocks - ${
                                                                            Gson().toJson(
                                                                                productStockSyncAdminLock
                                                                            )
                                                                        }"
                                                                    )
                                                                    if (productStockSyncAdminLock != null && productStockSyncAdminLock.adminLock) {
                                                                        //user will not get the lock, also update productstock sync here if required
                                                                        adminLockIsTakenValueInsertion(
                                                                            snapshot,
                                                                            lockedProducts
                                                                        )
                                                                    } else {
                                                                        // user will get the lock as either productStockSyncAdminLock is not found or productStockSyncAdminLock.adminLock is false
                                                                        getLockAndPopulateProductStockSyncSnapshot(
                                                                            productStockSync,
                                                                            snapshot,
                                                                            lockedProducts
                                                                        )
                                                                    }
                                                                } else {
                                                                    d(
                                                                        "UserCartActivityrvFragment",
                                                                        "getLocks - snapshot does not exists"
                                                                    )
                                                                    //productstocksyncadminfirebaseutil snapshot does not exists mean no admin wants lock so user can get the lock
                                                                    getLockAndPopulateProductStockSyncSnapshot(
                                                                        productStockSync,
                                                                        snapshot,
                                                                        lockedProducts
                                                                    )
                                                                }
                                                                updateAcquiredTimeStampAndSetTimeDelayCheckLockedUser(
                                                                    productStockSyncHashmap,
                                                                    lockedProducts,
                                                                    profile,
                                                                    userID
                                                                )
                                                            }

                                                            override fun onCancelled(error: DatabaseError) {}
                                                        })

                                                    } else {
                                                        /*stock is locked*/
                                                        stockLockedValueInsertion(
                                                            snapshot,
                                                            lockedProducts
                                                        )
                                                    }
                                                }
                                            } else {
//                                            stock not available
                                                stockNotAvailableValueInsertion(
                                                    snapshot,
                                                    lockedProducts
                                                )
                                            }
                                        } else {
                                            /*product is not available*/
                                            productNotAvailableValueInsertion(
                                                productId,
                                                lockedProducts
                                            )
                                        }
                                        updateAcquiredTimeStampAndSetTimeDelayCheckLockedUser(
                                            productStockSyncHashmap,
                                            lockedProducts,
                                            profile,
                                            userID
                                        )
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                        }
                    } else {
                        //This situation should never come ideally
                        paymentErrorPopup(listOf(ErrorType.emptyCart))
                    }
                    d("checkout", lockedProducts.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun checkConditionsForLock(productStockSync: ProductStockSync): Boolean {
        return (((productStockSync.locked == "-1" || checkTimeStampStatus(
            productStockSync.timeStamp
        ) || productStockSync.locked == FirebaseAuth.getInstance().uid.toString())) && (!productStockSync.adminLock))
    }

    private fun getLockAndPopulateProductStockSyncSnapshot(
        productStockSync: ProductStockSync,
        snapshot: DataSnapshot,
        lockedProducts: HashMap<String, Int>
    ) {
        d("checkout", "entered")

        val totalBoughtItems = calculateTotalBoughtItems(productStockSync)
        val istTime = getIstTime()

        populateProductStockSyncSnapshot(productStockSync, totalBoughtItems, istTime, snapshot)

        stockAvailableValueInsertion(
            snapshot,
            lockedProducts
        )
    }

    private fun populateProductStockSyncSnapshot(
        productStockSync: ProductStockSync,
        totalBoughtItems: Int,
        istTime: SimpleDateFormat,
        snapshot: DataSnapshot
    ): ProductStockSync {
        productStockSync.locked = FirebaseAuth.getInstance().uid.toString()
        productStockSync.stock = productStockSync.stock - totalBoughtItems
        productStockSync.boughtTicket = HashMap<String, Int>()
        productStockSync.dateStamp = istTime.format(Date())
        productStockSync.timeStamp = ((Date().time) / 1000).toString()

        if (productStockSync != null) {
            ProductStockSyncHelper().setValueInChild(
                snapshot.key.toString(),
                productStockSync
            )
        }
        return productStockSync
    }

    private fun adminLockIsTakenValueInsertion(
        snapshot: DataSnapshot,
        lockedProducts: HashMap<String, Int>
    ) {
        lockedProducts[snapshot.key.toString()] = -5
    }

    private fun stockAvailableValueInsertion(
        snapshot: DataSnapshot,
        lockedProducts: HashMap<String, Int>
    ) {
        //locked product array
        lockedProducts[snapshot.key.toString()] = 1
    }

    private fun getIstTime(): SimpleDateFormat {
        var istTime = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        istTime.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return istTime
    }

    private fun calculateTotalBoughtItems(productStockSync: ProductStockSync): Int {
        var totalBoughtItems = 0
        if (productStockSync != null) {
            for (boughtItems in productStockSync.boughtTicket) {
                totalBoughtItems += boughtItems.value
            }
        }
        return totalBoughtItems
    }

    private fun stockLockedValueInsertion(
        snapshot: DataSnapshot,
        lockedProducts: HashMap<String, Int>
    ) {
        lockedProducts[snapshot.key.toString()] = -1
        d("checkout", "not entered")
        Toast.makeText(
            activity,
            " lock not available",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateAcquiredTimeStampAndSetTimeDelayCheckLockedUser(
        productStockSyncHashmap: HashMap<String, Int>,
        lockedProducts: HashMap<String, Int>,
        profile: Profile,
        userID: String
    ) {
        UserCartSingletonClass.productLockAcquiredTimeStamp =
            (((Date().time) / 1000) - productStockSyncHashmap.size)
        if (productStockSyncHashmap.size == lockedProducts.size) {
            Handler().postDelayed({
                checkforSpamAndLockUser(
                    lockedProducts, profile, userID
                )
            }, 5000)
        }
    }

    private fun stockNotAvailableValueInsertion(
        snapshot: DataSnapshot,
        lockedProducts: HashMap<String, Int>
    ): HashMap<String, Int> {
        lockedProducts[snapshot.key.toString()] = -2
        d(
            "UserCartActivityrvFragment",
            "stockNotAvailableValueInsertion-not entered${lockedProducts}"
        )
        Toast.makeText(
            activity,
            " lock not available",
            Toast.LENGTH_SHORT
        ).show()
        return lockedProducts
    }

    private fun productNotAvailableValueInsertion(
        productId: MutableMap.MutableEntry<String, Int>,
        lockedProducts: HashMap<String, Int>
    ) {
        lockedProducts[productId.key] = -4
    }

    private fun checkInStock(
        productStockSync: ProductStockSync?,
        productId: MutableMap.MutableEntry<String, Int>
    ): Boolean {
        var totalBoughtItems = 0
        if (productStockSync != null) {
            for (boughtItems in productStockSync.boughtTicket) {
                totalBoughtItems += boughtItems.value
            }
        }
        var totalAvailableStock = productStockSync?.stock?.minus(totalBoughtItems)

        return if (totalAvailableStock != null) {
            (totalAvailableStock >= productId.value)
        } else {
            (productStockSync?.stock!! >= productId.value)
        }
    }

    private fun checkforSpamAndLockUser(
        lockedProducts: HashMap<String, Int>,
        profile: Profile,
        userID: String
    ) {
        var checkoutCounterFirebaseUtil = FirebaseUtil()
        checkoutCounterFirebaseUtil.openFbReference("checkOutCounter")

        var productMap: HashMap<String, CheckoutCounter> = HashMap<String, CheckoutCounter>()

        checkoutCounterFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var userCheckoutCounter =
                                snapshot.getValue(UserCheckoutCounter::class.java)
                            d(
                                "UserCartActivityrvFragment",
                                "checkSpamAndValidateUserProfile - ${
                                    Gson().toJson(userCheckoutCounter)
                                }"
                            )

                            if (userCheckoutCounter != null) {
                                var todaysDate = CheckoutCounter().getTodayDate(0)
                                if(userCheckoutCounter.dateMap[todaysDate]?.productMap != null) {
                                    productMap =
                                        userCheckoutCounter.dateMap[todaysDate]?.productMap as HashMap<String, CheckoutCounter>
                                }
                                d(
                                    "UserCartActivityrvFragment",
                                    "checkSpamAndValidateUserProfile - ${Gson().toJson(productMap)}"
                                )

                                checkForLockUser(lockedProducts, profile, userID, productMap)
                            } else {
                                checkForLockUser(lockedProducts, profile, userID, productMap)
                            }
                        } else {
                            checkForLockUser(lockedProducts, profile, userID, productMap)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

    }

    //todo have to put spam check here
    private fun checkForLockUser(
        lockedProducts: HashMap<String, Int>,
        profile: Profile,
        userID: String,
        productMap: HashMap<String, CheckoutCounter>
    ) {
        d("checkout", lockedProducts.toString())
        var productCounter = 0
        for (productSync in lockedProducts) {
            var productId = productSync.key
            var productStockSyncFirebaseUtil = FirebaseUtil()
            productStockSyncFirebaseUtil.openFbReference("productStockSync")
            productStockSyncFirebaseUtil.mDatabaseReference.child(productId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (productSync.value == 1) {
                            if (snapshot.exists()) {
                                var productStockSync =
                                    snapshot.getValue(ProductStockSync::class.java)
                                if (productStockSync != null) {
                                    if (productStockSync.locked == FirebaseAuth.getInstance().uid.toString()) {
                                        //todo count as locks got here
                                        checkAndClearSpammingLimitAndTakeLocks(productId)
                                        //todo put spam counter check here
                                        d("UserCartActivityrvFragment","checkForLockUser - ${productId}    ${Gson().toJson(productMap)} ")
                                        if(productId in productMap) {
                                            d("UserCartActivityrvFragment","checkForLockUser - I'm in")
                                        } else {
                                            d("UserCartActivityrvFragment","checkForLockUser - I'm out")
                                        }
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
//
//    private fun checkUserCartForSpam(lockedProducts: HashMap<String, Int>, productId: String) {
//
//        var checkoutCounterFirebaseUtil = FirebaseUtil()
//        checkoutCounterFirebaseUtil.openFbReference("checkOutCounter")
//
//        checkoutCounterFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    var userCheckoutCounter = snapshot.getValue(UserCheckoutCounter::class.java)
//                    d("UserCartActivityrvFragment", "checkSpamAndValidateUserProfile - ${Gson().toJson(userCheckoutCounter)}")
//
//                    if(userCheckoutCounter != null) {
//                        var todaysDate = CheckoutCounter().getTodayDate(0)
//                        var productMap = userCheckoutCounter.dateMap[todaysDate]?.productMap as HashMap<String,CheckoutCounter>
//                        d("UserCartActivityrvFragment", "checkSpamAndValidateUserProfile - ${Gson().toJson(productMap)}")
//
//                        checkProductSpamFromCartAndValidateUserProfile(profile, emailVerified, productMap)
//
//                    } else {
//                        checkAndValidateUserProfile(profile, emailVerified)
//                    }
//                } else  {
//                    checkAndValidateUserProfile(profile, emailVerified)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//
//        })
//    }

    private fun checkAndClearSpammingLimitAndTakeLocks(productId: String) {
        var todaysDate = SimpleDateFormat("ddMMMMyyyy")
        todaysDate.timeZone =
            TimeZone.getTimeZone("Asia/Kolkata")

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -23)

        var checkoutCounterFirebaseUtil = FirebaseUtil()
        checkoutCounterFirebaseUtil.openFbReference("checkOutCounter")

        checkoutCounterFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
            .child("dateMap").child(CheckoutCounter().getTodayDate(0)).child("productMap")
            .child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var checkoutCounter = CheckoutCounter()

                    if (snapshot.exists()) {
                        checkoutCounter = snapshot.getValue(CheckoutCounter::class.java)!!
                        d(
                            "UserCartActivityrvFragment",
                            "checkAndClearSpammingLimitAndTakeLocks - ${checkoutCounter}"
                        )
                    } else {
                        d(
                            "UserCartActivityrvFragment",
                            "checkAndClearSpammingLimitAndTakeLocks - snapshot does not exist"
                        )
                    }

                    checkoutCounter.count += 1
                    checkoutCounterFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
                        .child("dateMap").child(
                            checkoutCounter.getTodayDate(
                                0
                            )
                        ).child("productMap").child(productId).setValue(checkoutCounter)
                }

                override fun onCancelled(error: DatabaseError) {}

            })

//todo give admin permission for number of days of deletion and number of times user can get locks
        d(
            "UserCartActivityrvFragment",
            "checkAndClearSpammingLimitAndTakeLocks - ${todaysDate.format(Date())}"
        )
        d(
            "UserCartActivityrvFragment",
            "checkAndClearSpammingLimitAndTakeLocks - ${todaysDate.format(calendar.time)}"
        )

    }

    // time stamp product stock sync delay = 600 seconds
    private fun checkTimeStampStatus(timeStamp: String): Boolean {
        return ((((Date().time) / 1000) - timeStamp.toLong()) > 600)
    }

    private fun paymentErrorPopupEmptyCart(builder: AlertDialog.Builder) {
        builder.setTitle("Can't Checkout?")
        builder.setMessage("Your cart is empty.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("OK") { dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun paymentErrorPopupProfileNullability(builder: AlertDialog.Builder) {
        builder.setTitle("Can't Checkout?")
        builder.setMessage("There seems to be issue with your profile settings. Check under 'profile' menu if it is up-to-date. \nIf issue still persists contact us via 'Contact Us' menu")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("OK") { dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun paymentErrorPopupProfile(
        builder: AlertDialog.Builder,
        errorTypeValueList: List<ErrorType>
    ) {
        //email, phone, pincode, address

        var profileIssuePopupMessage = createProfileIssuePopupMessage(errorTypeValueList)

        builder.setTitle("Can't Checkout?")
        builder.setMessage(profileIssuePopupMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("OK") { dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun createProfileIssuePopupMessage(errorTypeValueList: List<ErrorType>): String {
        var message = "You must be facing the following issues: "

        if (errorTypeValueList.contains(ErrorType.phone)) {
            message += "\n \u2022 Wrong, missing or unverified phone number"
        }

        if (errorTypeValueList.contains(ErrorType.email)) {
            message += "\n \u2022 Wrong, missing or unverified email address"
        }

        if (errorTypeValueList.contains(ErrorType.pincode)) {
            message += "\n \u2022 Missing pincode or non - deliverable area"
        }

        if (errorTypeValueList.contains(ErrorType.address)) {
            message += "\n \u2022 Missing address"
        }

        return message
    }

    private fun paymentErrorPopup(errorTypeValueList: List<ErrorType>) {
        //        emptyCart, email, phone, pincode, address, profile,  ow
        val builder = AlertDialog.Builder(requireContext())
        if (errorTypeValueList.contains(ErrorType.emptyCart)) {
            paymentErrorPopupEmptyCart(builder)
        } else if (errorTypeValueList.contains(ErrorType.profile)) {
            paymentErrorPopupProfileNullability(builder)
        } else {
            paymentErrorPopupProfile(builder, errorTypeValueList)
        }
    }

}

