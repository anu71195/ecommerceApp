package com.raunakgarments.admin

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.global.AdminOrderSingletonClass
import com.raunakgarments.global.OrderStatusObject
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.UserOrderProduct
import com.raunakgarments.model.UserOrders
import kotlinx.android.synthetic.main.activity_admin_user_order_details_content_scrolling.*

class AdminUserOrderDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_order_details)
        setSupportActionBar(findViewById(R.id.activity_admin_user_order_details_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        orderOrderStatusButtonClickListener()
        orderDeliveryStatusButtonClickListener()
        updateButtonClickListener()


        //todo get enum from them and rest of theplaces find them
        //todo not changing if error

        AdminOrderSingletonClass.userOrders =
            Gson().fromJson<UserOrders>(intent.getStringExtra("userOrders"), UserOrders::class.java)

        loadRefreshData(AdminOrderSingletonClass.userOrders)

        initializeAdminUserOrdersRecyclerViewAdapter()
    }

    fun loadRefreshData(userOrders: UserOrders) {

        activity_admin_user_order_details_content_scrolling_OrdersTotalCost.text = "Total Cost = ₹" + userOrders.totalCost
        activity_admin_user_order_details_content_scrolling_OrdersTotalItems.text = "Total Items = ${userOrders.orders.size}"
        activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text = "Delivery Status = ${OrderStatusObject.getDeliveryStringFromString(userOrders.deliveryStatus)}"
        activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text = "Order Status = ${OrderStatusObject.getOrderStringFromString(userOrders.orderStatus)}"

        activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.setBackgroundColor(OrderStatusObject.getDeliveryColorFromString(userOrders.deliveryStatus))
        activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.setBackgroundColor(OrderStatusObject.getOrderColorFromString(userOrders.orderStatus))

        activity_admin_user_order_details_content_scrolling_OrdersUserName.text = "Name = ${userOrders.userOrderProfile.userName}\n"
        activity_admin_user_order_details_content_scrolling_OrdersUserAddress.text = "Address = ${userOrders.userOrderProfile.address}\n"
        activity_admin_user_order_details_content_scrolling_OrdersUserEmail.text = "Email = ${userOrders.userOrderProfile.email}\n"
        activity_admin_user_order_details_content_scrolling_OrdersUserPhoneNumber.text = "Number = +${userOrders.userOrderProfile.areaPhoneCode + " " + userOrders.userOrderProfile.number}\n"
        activity_admin_user_order_details_content_scrolling_OrdersUserPincode.text = "Pincode = ${userOrders.userOrderProfile.pinCode}"

    }

    //todo synchronise it with adapter and vice versa
    private fun updateButtonClickListener() {
        activity_admin_user_order_details_content_scrolling_updateButton.setOnClickListener {

            AdminOrderSingletonClass.userOrders.orders = getUpdatedUserOrders(AdminOrderSingletonClass.userOrders.orders)

            AdminOrderSingletonClass.userOrders.deliveryStatus = getDeliveryStatusString()
            AdminOrderSingletonClass.userOrders.orderStatus = getOrderStatusString()

            var userOrderFirebaseUtil = FirebaseUtil()
            userOrderFirebaseUtil.openFbReference("userOrders")

            userOrderFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString()).child(AdminOrderSingletonClass.userOrders.id).setValue(AdminOrderSingletonClass.userOrders)
        }
    }

    private fun getUpdatedUserOrders(orders: HashMap<String, UserOrderProduct>): HashMap<String, UserOrderProduct> {
        for (orderedProduct in orders) {
            d("AdminUserOrderDetailsActivity", "getUpdatedUserOrders - ${orderedProduct.key}")
            d("AdminUserOrderDetailsActivity", "getUpdatedUserOrders - ${Gson().toJson(orderedProduct.value)}")

            if(orders[orderedProduct.key]!!.deliveryStatus == AdminOrderSingletonClass.userOrders.deliveryStatus) {
                orders[orderedProduct.key]!!.deliveryStatus = getDeliveryStatusString()
            }

            if(orders[orderedProduct.key]!!.orderStatus == AdminOrderSingletonClass.userOrders.orderStatus) {
                orders[orderedProduct.key]!!.orderStatus = getOrderStatusString()
            }
        }


        return orders
    }

    private fun getOrderStatusString(): String {
        if(activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentDone)}") {
           return OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentDone)
        } else if (activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.refunded)}") {
           return OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.refunded)
        } else if(activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentPending)}") {
           return OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentPending)
        }
        return OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.error)
    }

    private fun getDeliveryStatusString(): String {
        if(activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.paymentDone)}") {
                return OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.paymentDone)
        } else if (activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.delivered)}") {
            return OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.delivered)
        } else if(activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.cancelled)}") {
            return OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.cancelled)
        } else if(activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.returned)}") {
            return OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.returned)
        }
        return OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.error)

    }

    private fun orderOrderStatusButtonClickListener() {
        activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.setOnClickListener {
            d("AdminUserOrderDetailsActivity", "orderOrderStatusButtonClickListener :- order status button clicked")
            if(activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentDone)}") {
                d("AdminUserOrderDetailsActivity", "orderOrderStatusButtonClickListener :- order status button clicked payment done")
                activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text = "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.refunded)}"
                activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.setBackgroundColor(OrderStatusObject.getOrderColor(OrderStatusObject.orderStatus.refunded))
            } else if (activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.refunded)}") {
                activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text = "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentPending)}"
                activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.setBackgroundColor(OrderStatusObject.getOrderColor(OrderStatusObject.orderStatus.paymentPending))
            } else if(activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentPending)}") {
                activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text = "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentDone)}"
                activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.setBackgroundColor(OrderStatusObject.getOrderColor(OrderStatusObject.orderStatus.paymentDone))
            } else if(activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.error)}") {
                activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text = "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentDone)}"
                activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.setBackgroundColor(OrderStatusObject.getOrderColor(OrderStatusObject.orderStatus.paymentDone))
            }
        }
        d("AdminUserOrderDetailsActivity", "orderOrderStatusButtonClickListener :- order status button clicked end")
    }

    private fun orderDeliveryStatusButtonClickListener() {
        //todo change in this.userOrders both adapter and on this screen
        //todo change in userorderprofilesingletonclass
        // todo change in adminordersingletonclass
        //todo update in database and datasetchanged function call
        activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.setOnClickListener {
            d("AdminUserOrderDetailsActivity", "orderOrderStatusButtonClickListener :- order status button clicked")
            if(activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.paymentDone)}") {
                d("AdminUserOrderDetailsActivity", "orderOrderStatusButtonClickListener :- order status button clicked payment done")
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.delivered)}"
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.setBackgroundColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.delivered))
            } else if (activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.delivered)}") {
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.cancelled)}"
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.setBackgroundColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.cancelled))
            } else if(activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.cancelled)}") {
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.returned)}"
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.setBackgroundColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.returned))
            } else if(activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.returned)}") {
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.paymentDone)}"
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.setBackgroundColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.paymentDone))
            } else if(activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.error)}") {
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.paymentDone)}"
                activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.setBackgroundColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.paymentDone))
            }
        }
    }

    private fun initializeAdminUserOrdersRecyclerViewAdapter() {
        val adminUserOrderDetailsAdapter =
            AdminUserOrderDetailsAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
//        productsLayoutManager.reverseLayout = true
        adminUserOrderDetailsAdapter.populate(intent, this)
        activity_admin_user_order_details_content_scrolling_OrdersRecyclerView.adapter = adminUserOrderDetailsAdapter
        activity_admin_user_order_details_content_scrolling_OrdersRecyclerView.layoutManager = productsLayoutManager
    }
}