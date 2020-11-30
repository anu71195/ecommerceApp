package com.raunakgarments.admin

import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.raunakgarments.R
import com.raunakgarments.global.AdminOrderSingletonClass
import com.raunakgarments.global.OrderStatusObject
import kotlinx.android.synthetic.main.activity_admin_orders_content_scrolling.*
import kotlinx.android.synthetic.main.activity_settings_content_scrolling.*

class AdminOrdersActivity : AppCompatActivity() {

    var autoLoadingCheck = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_orders)
        setSupportActionBar(findViewById(R.id.activity_admin_orders_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        setSpinnerDropDown()

    }

    private fun setSpinnerDropDown() {
        val spinner =
            findViewById<Spinner>(R.id.activity_admin_orders_content_scrolling_OrderListTypeSpinner)
        val items = AdminOrderSingletonClass.getItemList()
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            items
        )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                arg2: Int,
                arg3: Long
            ) {
                // Do what you want
                activity_admin_orders_content_scrolling_StatusFilterOptions.visibility = View.GONE
                when(spinner.selectedItem.toString()) {
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.title) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.customer) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                        initializeUserOrdersRecyclerViewByCustomerAdapter()
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.time) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                        initializeUserOrdersrecyclerViewByDatesAdapter()
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.statusFilter) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                        initializeUserOrdersrecyclerViewStatusFilterAdapter()
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.clean) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                        cleanAdapter()
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.error) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                    }
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun cleanAdapter() {
        val adminOrdersAdapter = AdminOrdersAdapter()
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
    }

    private fun initializeUserOrdersrecyclerViewStatusFilterAdapter() {
        activity_admin_orders_content_scrolling_StatusFilterOptions.visibility = View.VISIBLE
        populateCheckboxText()
        filterButtonClickListener()
        checkBoxClickListeners()
        checkAutoPopulateAndPopulateIfNeeded()
        initializeUserOrdersrecyclerViewFilterAdapter()
    }

    private fun filterButtonClickListener() {
        activity_admin_orders_content_scrolling_FilterButton.setOnClickListener {
            initializeUserOrdersrecyclerViewFilterAdapter()
        }
    }

    private fun initializeUserOrdersrecyclerViewFilterAdapter() {
        cleanAdapter()
        val adminOrdersAdapter = AdminOrdersAdapterFilter()
        val productsLayoutManager = GridLayoutManager(this, 1)
        productsLayoutManager.reverseLayout = true
        adminOrdersAdapter.populate(getString(R.string.database_userOrders), this)
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
        activity_admin_orders_content_scrolling_OrdersRecyclerView.layoutManager =
            productsLayoutManager
    }

    private fun checkBoxClickListeners() {

        handleOrderStatusCheckboxCheckAtInitialization(activity_admin_orders_content_scrolling_OrderStatusFilterPaymentDone, OrderStatusObject.orderStatus.paymentDone)
        handleOrderStatusCheckboxCheckAtInitialization(activity_admin_orders_content_scrolling_OrderStatusFilterRefunded, OrderStatusObject.orderStatus.refunded)
        handleOrderStatusCheckboxCheckAtInitialization(activity_admin_orders_content_scrolling_OrderStatusFilterPaymentPending, OrderStatusObject.orderStatus.paymentPending)
        handleOrderStatusCheckboxCheckAtInitialization(activity_admin_orders_content_scrolling_OrderStatusFilterError, OrderStatusObject.orderStatus.error)

        handleOrderStatusCheckBoxSelection(activity_admin_orders_content_scrolling_OrderStatusFilterPaymentDone, OrderStatusObject.orderStatus.paymentDone)
        handleOrderStatusCheckBoxSelection(
            activity_admin_orders_content_scrolling_OrderStatusFilterRefunded,
            OrderStatusObject.orderStatus.refunded
        )
        handleOrderStatusCheckBoxSelection(
            activity_admin_orders_content_scrolling_OrderStatusFilterPaymentPending,
            OrderStatusObject.orderStatus.paymentPending
        )
        handleOrderStatusCheckBoxSelection(
            activity_admin_orders_content_scrolling_OrderStatusFilterError,
            OrderStatusObject.orderStatus.error
        )

        handleDeliveryStatusCheckboxCheckAtInitialization(activity_admin_orders_content_scrolling_DeliveryStatusFilterPaymentDone, OrderStatusObject.deliveryStatus.paymentDone)
        handleDeliveryStatusCheckboxCheckAtInitialization(activity_admin_orders_content_scrolling_DeliveryStatusFilterDelivered, OrderStatusObject.deliveryStatus.delivered)
        handleDeliveryStatusCheckboxCheckAtInitialization(activity_admin_orders_content_scrolling_DeliveryStatusFilterReturned, OrderStatusObject.deliveryStatus.returned)
        handleDeliveryStatusCheckboxCheckAtInitialization(activity_admin_orders_content_scrolling_DeliveryStatusFilterCancelled, OrderStatusObject.deliveryStatus.cancelled)
        handleDeliveryStatusCheckboxCheckAtInitialization(activity_admin_orders_content_scrolling_DeliveryStatusFilterError, OrderStatusObject.deliveryStatus.error)

        handleDeliveryStatusCheckBoxSelection(activity_admin_orders_content_scrolling_DeliveryStatusFilterPaymentDone, OrderStatusObject.deliveryStatus.paymentDone)
        handleDeliveryStatusCheckBoxSelection(
            activity_admin_orders_content_scrolling_DeliveryStatusFilterDelivered,
            OrderStatusObject.deliveryStatus.delivered
        )
        handleDeliveryStatusCheckBoxSelection(
            activity_admin_orders_content_scrolling_DeliveryStatusFilterReturned,
            OrderStatusObject.deliveryStatus.returned
        )
        handleDeliveryStatusCheckBoxSelection(
            activity_admin_orders_content_scrolling_DeliveryStatusFilterCancelled,
            OrderStatusObject.deliveryStatus.cancelled
        )
        handleDeliveryStatusCheckBoxSelection(
            activity_admin_orders_content_scrolling_DeliveryStatusFilterError,
            OrderStatusObject.deliveryStatus.error
        )
    }

    private fun handleDeliveryStatusCheckboxCheckAtInitialization(
        checkBoxView: CheckBox,
        deliveryStatus: OrderStatusObject.deliveryStatus
    ) {
        if(deliveryStatus in AdminOrderSingletonClass.deliveryStatusCheckboxSelection) {
            checkBoxView.isChecked = true
        }
    }

    private fun handleDeliveryStatusCheckBoxSelection(
        checkBoxView: CheckBox,
        deliveryStatus: OrderStatusObject.deliveryStatus) {
        checkBoxView.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                AdminOrderSingletonClass.deliveryStatusCheckboxSelection.add(deliveryStatus)
            } else {
                AdminOrderSingletonClass.deliveryStatusCheckboxSelection.remove(deliveryStatus)
            }
            d("AdminOrdersActivity", "handleOrderStatusCheckBoxSelection -> ${AdminOrderSingletonClass.deliveryStatusCheckboxSelection}")
            if(autoLoadingCheck) {
                initializeUserOrdersrecyclerViewFilterAdapter()
                setCheckBoxesClickEnability(false)
                Handler().postDelayed({setCheckBoxesClickEnability(true)}, 100)
            }
        }
    }

    private fun handleOrderStatusCheckboxCheckAtInitialization(
        checkBoxView: CheckBox,
        orderStatus: OrderStatusObject.orderStatus
    ) {
        if(orderStatus in AdminOrderSingletonClass.orderStatusCheckboxSelection) {
            checkBoxView.isChecked = true
        }
    }

    private fun handleOrderStatusCheckBoxSelection(
        checkBoxView: CheckBox,
        orderStatus: OrderStatusObject.orderStatus) {
        checkBoxView.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                AdminOrderSingletonClass.orderStatusCheckboxSelection.add(orderStatus)
            } else {
                AdminOrderSingletonClass.orderStatusCheckboxSelection.remove(orderStatus)
            }
            d("AdminOrdersActivity", "handleOrderStatusCheckBoxSelection -> ${AdminOrderSingletonClass.orderStatusCheckboxSelection}")
            if(autoLoadingCheck) {
                initializeUserOrdersrecyclerViewFilterAdapter()
                setCheckBoxesClickEnability(false)
                Handler().postDelayed({setCheckBoxesClickEnability(true)}, 1000)
            }
        }
    }

    private fun setCheckBoxesClickEnability(clickable: Boolean) {

        activity_admin_orders_content_scrolling_OrderStatusFilterPaymentDone.isClickable = clickable
        activity_admin_orders_content_scrolling_OrderStatusFilterRefunded.isClickable = clickable
        activity_admin_orders_content_scrolling_OrderStatusFilterPaymentPending.isClickable = clickable
        activity_admin_orders_content_scrolling_OrderStatusFilterError.isClickable = clickable

        activity_admin_orders_content_scrolling_DeliveryStatusFilterPaymentDone.isClickable = clickable
        activity_admin_orders_content_scrolling_DeliveryStatusFilterDelivered.isClickable = clickable
        activity_admin_orders_content_scrolling_DeliveryStatusFilterReturned.isClickable = clickable
        activity_admin_orders_content_scrolling_DeliveryStatusFilterCancelled.isClickable = clickable
        activity_admin_orders_content_scrolling_DeliveryStatusFilterError.isClickable = clickable

    }

    private fun checkAutoPopulateAndPopulateIfNeeded() {
        activity_admin_orders_content_scrolling_AutoLoading.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                autoLoadingCheck = true
                initializeUserOrdersrecyclerViewFilterAdapter()
            } else {
                autoLoadingCheck = false
            }
        }
    }

    private fun populateCheckboxText() {
        activity_admin_orders_content_scrolling_OrderStatusFilterPaymentDone.text = OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentDone)
        activity_admin_orders_content_scrolling_OrderStatusFilterRefunded.text = OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.refunded)
        activity_admin_orders_content_scrolling_OrderStatusFilterPaymentPending.text = OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentPending)
        activity_admin_orders_content_scrolling_OrderStatusFilterError.text = OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.error)

        activity_admin_orders_content_scrolling_DeliveryStatusFilterPaymentDone.text = OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.paymentDone)
        activity_admin_orders_content_scrolling_DeliveryStatusFilterDelivered.text = OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.delivered)
        activity_admin_orders_content_scrolling_DeliveryStatusFilterReturned.text = OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.returned)
        activity_admin_orders_content_scrolling_DeliveryStatusFilterCancelled.text = OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.cancelled)
        activity_admin_orders_content_scrolling_DeliveryStatusFilterError.text = OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.error)
    }

    private fun initializeUserOrdersrecyclerViewByDatesAdapter() {
        cleanAdapter()
        val adminOrdersAdapter = AdminOrdersAdapterByDates()
        val productsLayoutManager = GridLayoutManager(this, 1)
        productsLayoutManager.reverseLayout = true
        adminOrdersAdapter.populate(getString(R.string.database_userOrders), this)
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
        activity_admin_orders_content_scrolling_OrdersRecyclerView.layoutManager =
            productsLayoutManager
    }

    private fun initializeUserOrdersRecyclerViewByCustomerAdapter() {
        cleanAdapter()
        val adminOrdersAdapter = AdminOrdersAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
        adminOrdersAdapter.populate(getString(R.string.database_userOrders), this)
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
        activity_admin_orders_content_scrolling_OrdersRecyclerView.layoutManager =
            productsLayoutManager
    }
}