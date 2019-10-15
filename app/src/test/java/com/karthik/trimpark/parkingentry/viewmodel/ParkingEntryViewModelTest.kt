package com.karthik.trimpark.parkingentry.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.karthik.trimpark.base.db.entity.Vehicle
import com.karthik.trimpark.base.db.entity.VehicleType
import com.karthik.trimpark.parkingentry.repository.ParkingRepository
import com.karthik.trimpark.util.TestCoroutineDispachterProvider
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations


/**
 * created by Karthik A on 2019-08-04
 */
class ParkingEntryViewModelTest {


    @get: Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var parkingEntryViewModel: ParkingEntryViewModel


    @Mock private lateinit var mockParkingRepository: ParkingRepository
    @Mock private lateinit var mockAssignButtonObserver: Observer<Boolean>
    @Mock private lateinit var  mockVehicle: Vehicle

    @Mock private lateinit var mockVehicleIdViewObserver: Observer<Boolean>
    @Mock private lateinit var mockVehicleTypeObserver: Observer<List<VehicleType>>
    @Mock private lateinit var mockSlotResponseObserver: Observer<String>

    @Before
    fun setUp()
    {

        MockitoAnnotations.initMocks(this)

        parkingEntryViewModel = ParkingEntryViewModel(mockParkingRepository,TestCoroutineDispachterProvider())

    }


    @Test
    fun fetchVehicleTypes_success() = runBlocking {

        parkingEntryViewModel.vehicleTypeResponse.observeForever(mockVehicleTypeObserver)
        whenever(mockParkingRepository.getVehicleTypes()).thenReturn(emptyList())
        parkingEntryViewModel.fetchVehicleTypes()

        verify(mockVehicleTypeObserver).onChanged(emptyList())

    }

    @Test
    fun fetchFreeSlotCount_morethan0() = runBlocking {

        val testVehicleTypeId = 1
        parkingEntryViewModel.assignSlotButton.observeForever(mockAssignButtonObserver)
        whenever(mockParkingRepository.getFreeSlotsCount(testVehicleTypeId)).thenReturn(1)

        parkingEntryViewModel.fetchFreeSlotCount(testVehicleTypeId)

        verify(mockAssignButtonObserver).onChanged(true)
    }

    @Test
    fun fetchFreeSlotCount_0() = runBlocking {

        val testVehicleTypeId = 1
        parkingEntryViewModel.assignSlotButton.observeForever(mockAssignButtonObserver)
        whenever(mockParkingRepository.getFreeSlotsCount(testVehicleTypeId)).thenReturn(0)

        parkingEntryViewModel.fetchFreeSlotCount(testVehicleTypeId)

        verify(mockAssignButtonObserver).onChanged(false)
    }


   /* @Test
    fun assignSlot_success() = runBlocking {

        val testVehicleId = "test01"
        val testslotId = "slot1"

        parkingEntryViewModel.assignSlotResponse.observeForever(mockSlotResponseObserver)

        whenever(mockParkingRepository.getFreeSlot(1)).thenReturn("slot1")
        whenever(mockVehicle.id).thenReturn("3")
        //whenever(mockParkingRepository.registerParkingEntry())

    }*/

}