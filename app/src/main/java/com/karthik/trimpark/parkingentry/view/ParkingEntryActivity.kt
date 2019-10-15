package com.karthik.trimpark.parkingentry.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.karthik.trimpark.R
import com.karthik.trimpark.base.db.entity.Vehicle
import com.karthik.trimpark.base.db.entity.VehicleType
import com.karthik.trimpark.parkingentry.viewmodel.ParkingEntryViewModel
import com.karthik.trimpark.parkingentry.viewmodel.ParkingEntryViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_parking_entry.*
import javax.inject.Inject

class ParkingEntryActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: ParkingEntryViewModel

    @Inject
    lateinit var entryViewModelFactory: ParkingEntryViewModelFactory

    lateinit var adapter: ArrayAdapter<VehicleType>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_entry)

        viewModel = ViewModelProviders.of(this, entryViewModelFactory).get(ParkingEntryViewModel::class.java)

        viewModel.fetchVehicleTypes()
        setUpObservers()
        setupActionListeners()
    }


    private fun setUpObservers() {
        viewModel.progressLoading.observe(this, Observer{
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.vehicleTypeResponse.observe(this, Observer {

            adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,it)

            vehicle_type_spinner.adapter = adapter
            vehicle_type_spinner.visibility = View.VISIBLE

        })

        viewModel.isSlotAvailableResponse.observe(this, Observer {

            if(!it)
            {
                Toast.makeText(this,"No Parking space available for ${viewModel.selectedVehicleType}",Toast.LENGTH_SHORT).show()
            }

        })

        viewModel.vehicleIdView.observe(this, Observer {
            vehicle_id_layout.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.assignSlotButton.observe(this, Observer {

            if(it){ assign_button.visibility = View.VISIBLE }
            assign_button.isEnabled = it
        })

        viewModel.assignSlotResponse.observe(this, Observer {

            if(it.isNotEmpty())
            {
                assign_button.visibility = View.GONE

                allotted_slot_view.text = it
                allotted_card_view.visibility = View.VISIBLE

                Toast.makeText(this,"Slot assigned for vehicle",Toast.LENGTH_SHORT).show()

            }else
            {
                Toast.makeText(this,"Error. Slot not assigned",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupActionListeners() {
        vehicle_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                viewModel.selectedVehicleType = viewModel.vehicleTypeResponse.value?.get(position)

                viewModel.vehicleTypeResponse.value?.get(position)?.id?.let {

                    allotted_card_view.visibility = View.GONE
                    vehicle_id_layout.visibility = View.GONE
                    viewModel.fetchFreeSlot(it)
                }
            }

        }

        assign_button.setOnClickListener {

            if(vehicle_id_view.text.toString().isNotEmpty())
            {
                viewModel.assignParkingSlot(Vehicle(vehicle_id_view.text.toString(),
                    viewModel.selectedVehicleType!!.id,null,null))
            }else{
                Toast.makeText(this,"Please enter the Vehicle No",Toast.LENGTH_SHORT).show()
            }


        }
    }

}
