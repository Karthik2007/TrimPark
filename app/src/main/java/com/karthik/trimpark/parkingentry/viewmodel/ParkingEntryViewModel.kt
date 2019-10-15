package com.karthik.trimpark.parkingentry.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karthik.trimpark.base.db.entity.ParkingSlotEntry
import com.karthik.trimpark.base.db.entity.Vehicle
import com.karthik.trimpark.base.db.entity.VehicleType
import com.karthik.trimpark.base.util.CoroutineDispatcherProvider
import com.karthik.trimpark.parkingentry.repository.ParkingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * created by Karthik A on 2019-08-01
 */
class ParkingEntryViewModel(
    private val parkingRepository: ParkingRepository,
    dispatcherProvider: CoroutineDispatcherProvider
): ViewModel() {


    private val viewModelJob = Job()
    private var uiScope: CoroutineScope

    private val _progressLoading = MutableLiveData<Boolean>()
    val progressLoading: LiveData<Boolean>
        get() = _progressLoading

    private val _vehicleIdView = MutableLiveData<Boolean>()
    val vehicleIdView: LiveData<Boolean>
        get() = _vehicleIdView

    private val _assignSlotButton = MutableLiveData<Boolean>()
    val assignSlotButton: LiveData<Boolean>
        get() = _assignSlotButton


    private val _assignSlotResponse = MutableLiveData<String>()
    val assignSlotResponse: LiveData<String>
        get() = _assignSlotResponse

    private val _vehicleTypeResponse = MutableLiveData<List<VehicleType>>()
    val vehicleTypeResponse: LiveData<List<VehicleType>>
        get() = _vehicleTypeResponse


    private val _isSlotAvailableResponse = MutableLiveData<Boolean>()
    val isSlotAvailableResponse: LiveData<Boolean>
        get() = _isSlotAvailableResponse

    var selectedVehicleType: VehicleType? = null

    private var availableSlotId: String? = null

    init {
        uiScope = CoroutineScope(dispatcherProvider.Main + viewModelJob)
    }


    fun fetchVehicleTypes()
    {
        uiScope.launch {
            _progressLoading.value = true
            val vehicleTypes = parkingRepository.getVehicleTypes()
            _vehicleTypeResponse.value = vehicleTypes
            _progressLoading.value = false
        }
    }

    fun fetchFreeSlot(vehicleTypeId: Int)
    {
        uiScope.launch {
            _progressLoading.value = true

            var slotIds: List<String> = parkingRepository.getExactFreeSlot(vehicleTypeId)

            if(slotIds.isEmpty() && vehicleTypeId == 1) {
                slotIds = parkingRepository.getHalfFreeSlot(vehicleTypeId)
            }

            if(slotIds.isEmpty()){
                slotIds = parkingRepository.getExactOrOtherFreeSlot(vehicleTypeId)
            }

            _isSlotAvailableResponse.value = slotIds.isNotEmpty()

            if(slotIds.isNotEmpty()){
                availableSlotId = slotIds[0]
                _vehicleIdView.value = true
                _assignSlotButton.value = true
            }else
            {
                _vehicleIdView.value = false
                _assignSlotButton.value = false
            }
            _progressLoading.value = false
        }
    }

    fun assignParkingSlot(vehicle: Vehicle)
    {
        uiScope.launch {

            val slotId = availableSlotId

            parkingRepository.addVehicle(vehicle)
            val entry = ParkingSlotEntry(parkingSlotId = slotId!!,vehicleId = vehicle.id,entryTime = System.currentTimeMillis())

            val result = parkingRepository.registerParkingEntry(entry)

            if(result)
                _assignSlotResponse.value = slotId
            else
                _assignSlotResponse.value = ""


        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}