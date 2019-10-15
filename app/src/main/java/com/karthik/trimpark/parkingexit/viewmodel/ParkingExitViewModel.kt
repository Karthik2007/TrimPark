package com.karthik.trimpark.parkingexit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karthik.trimpark.base.db.entity.ParkingReceipt
import com.karthik.trimpark.base.util.CoroutineDispatcherProvider
import com.karthik.trimpark.parkingentry.repository.ParkingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * created by Karthik A on 2019-08-02
 */
class ParkingExitViewModel(
    private val parkingRepository: ParkingRepository,
    dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {


    private val viewModelJob = Job()
    private var uiScope: CoroutineScope

    private val _parkedVehicleResponse = MutableLiveData<List<String>>()
    val parkedVehicleResponse: LiveData<List<String>>
        get() = _parkedVehicleResponse

    private val _parkingDetailResponse = MutableLiveData<ParkingReceipt>()
    val parkingDetailResponse: LiveData<ParkingReceipt>
        get() = _parkingDetailResponse

    private val _parkingReceiptResponse = MutableLiveData<ParkingReceipt>()
    val parkingReceiptResponse: LiveData<ParkingReceipt>
        get() = _parkingReceiptResponse

    private val _checkoutResponse = MutableLiveData<Boolean>()
    val checkoutResponse: LiveData<Boolean>
        get() = _checkoutResponse


    init {
        uiScope = CoroutineScope(dispatcherProvider.Main + viewModelJob)
    }


    fun getParkedVehicles(searchString: String) {
        uiScope.launch {
            var result = parkingRepository.getParkedVehicles("$searchString%")

            _parkedVehicleResponse.value = result
        }
    }

    fun getParkedVehicleReceipt(vehicleId: String) {
        uiScope.launch {
            var result = parkingRepository.getParkingDetail(vehicleId)

            _parkingDetailResponse.value = result

        }
    }


    fun calculateFee() {
        val parkingDetail = _parkingDetailResponse.value

        parkingDetail?.let {

            val exitTime = System.currentTimeMillis()

            val entryTime = parkingDetail.entryTime

            val durationInMill = exitTime - entryTime

            val durationInHours = Math.ceil(durationInMill.toDouble() / (1000 * 60 * 60))

            val amount = (durationInHours - 1) * parkingDetail.rate2 + parkingDetail.rate1

            parkingDetail.duration = durationInHours.toInt()
            parkingDetail.amount = amount.toInt()
            parkingDetail.exitTime = exitTime

            uiScope.launch {

                var isDiscountEligible = parkingRepository.isDiscountEligible(vehicleId = parkingDetail.vehicleNo)

                parkingDetail.discount = (parkingDetail.amount!!.times(0.25)).toInt()


                parkingReceipt = parkingDetail
                _parkingReceiptResponse.value = parkingReceipt
            }

        }
    }


    fun userWantsToCheckout() {
        uiScope.launch {
            parkingReceipt?.let {
                _checkoutResponse.value = parkingRepository.checkoutParking(it)
            }

        }
    }

    var parkingReceipt: ParkingReceipt? = null
        private set
}