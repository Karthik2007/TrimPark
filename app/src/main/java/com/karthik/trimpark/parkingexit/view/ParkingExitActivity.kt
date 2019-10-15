package com.karthik.trimpark.parkingexit.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.karthik.trimpark.R
import com.karthik.trimpark.base.db.entity.ParkingReceipt
import com.karthik.trimpark.base.util.DateUtil
import com.karthik.trimpark.parkingexit.viewmodel.ParkingExitViewModel
import com.karthik.trimpark.parkingexit.viewmodel.ParkingExitViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_parking_exit.*
import javax.inject.Inject



class ParkingExitActivity : DaggerAppCompatActivity() {


    private lateinit var viewModel: ParkingExitViewModel
    @Inject lateinit var factory: ParkingExitViewModelFactory

    private lateinit var searchAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_exit)

        viewModel = ViewModelProviders.of(this, factory).get(ParkingExitViewModel::class.java)

        searchAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item)

        setUpViews()
        setUpObserver()


    }

    private fun setUpViews() {

        (vehicle_search_view.editText as AutoCompleteTextView).setAdapter(searchAdapter)

        (vehicle_search_view.editText as AutoCompleteTextView).threshold = 1
        (vehicle_search_view.editText as AutoCompleteTextView).addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //var searchString = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(searchString: CharSequence?, start: Int, before: Int, count: Int) {
                if(searchString != null && searchString.isNotEmpty())
                {
                    viewModel.getParkedVehicles(searchString.toString())
                }
            }

        })

        (vehicle_search_view.editText as AutoCompleteTextView).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                var selectedVehicleId = (vehicle_search_view.editText as AutoCompleteTextView).text.toString()
                hideKeyboard()
                hideResponseViews()
                viewModel.getParkedVehicleReceipt(selectedVehicleId)
            }

        calculate_fee_button.setOnClickListener {
            viewModel.calculateFee()
        }

        checkout_button.setOnClickListener {
            viewModel.userWantsToCheckout()
        }

    }

    private fun hideResponseViews() {
        checkout_button.visibility = View.GONE
        calculate_fee_button.visibility = View.GONE
        fee_card_view.visibility = View.GONE
    }

    private fun setUpObserver() {

        viewModel.parkedVehicleResponse.observe(this, Observer {
            searchAdapter.clear()
            searchAdapter.addAll(it)
            searchAdapter.notifyDataSetChanged()
        })

        viewModel.parkingDetailResponse.observe(this, Observer {

            val detail = "${it.id} ${it.parkingSlotNo} ${it.entryTime} ${it.vehicleNo} ${it.vehicleType}"


            setParkingReceipt(it)
            calculate_fee_button.visibility = View.VISIBLE
        })

        viewModel.parkingReceiptResponse.observe(this, Observer {

            setupFeeCard(it)
            calculate_fee_button.visibility = View.GONE
            checkout_button.visibility = View.VISIBLE

        })

        viewModel.checkoutResponse.observe(this, Observer {

            if(it){
                Toast.makeText(this, "Successfully Checked Out",Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Failed to Checkout. Try again",Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setupFeeCard(receipt: ParkingReceipt?) {

        receipt?.let {

            duration_view.text = "${receipt.duration} hr(s)"
            parking_fee_view.text = "${receipt.amount} \u20b9"
            discount_view.text = if(receipt.discount!! > 0) "${receipt.discount} ₹" else "--"
            total_amount_view.text = "${receipt.amount!!.minus(receipt.discount!!)}"

            fee_card_view.visibility = View.VISIBLE
        }

    }

    private fun setParkingReceipt(parkingReceipt: ParkingReceipt?) {

        parkingReceipt?.let {
            vehicle_no_view.text = it.vehicleNo
            vehicle_type_view.text = it.vehicleType
            parking_lot_number_view.text = it.parkingSlotNo
            entry_time_view.text = DateUtil.getFormattedDate(it.entryTime, "dd/MM/yy hh:mm")

            receipt_card_view.visibility = View.VISIBLE
        }

    }


    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(vehicle_search_view.editText!!.windowToken, 0)
    }
}
