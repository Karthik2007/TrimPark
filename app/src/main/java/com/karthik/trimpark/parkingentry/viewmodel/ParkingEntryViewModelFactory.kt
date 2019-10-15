package com.karthik.trimpark.parkingentry.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karthik.trimpark.base.util.CoroutineDispatcherProvider
import com.karthik.trimpark.parkingentry.repository.ParkingRepository
import javax.inject.Inject


/**
 * created by Karthik A on 2019-08-01
 */
class ParkingEntryViewModelFactory
@Inject constructor(
    private val deliveryRepository: ParkingRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ParkingEntryViewModel(deliveryRepository,coroutineDispatcherProvider) as T
    }

}