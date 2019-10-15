package com.karthik.trimpark.parkingexit.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.karthik.trimpark.base.db.entity.ParkingReceipt
import com.karthik.trimpark.parkingentry.repository.ParkingRepository
import com.karthik.trimpark.util.TestCoroutineDispachterProvider
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations


/**
 * created by Karthik A on 2019-08-04
 */
class ParkingExitViewModelTest {

    @get: Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var parkingExitViewModel: ParkingExitViewModel

    @Mock private lateinit var mockParkingRepository: ParkingRepository

    @Mock private lateinit var mockparkedVehiclesObserver: Observer<List<String>>

    @Mock private lateinit var mockParkedDetailObserver: Observer<ParkingReceipt?>


    @Before
    fun setUp()
    {
        MockitoAnnotations.initMocks(this)

        parkingExitViewModel = ParkingExitViewModel(mockParkingRepository,TestCoroutineDispachterProvider())
    }


    @Test
    fun getParkedVehicles_success() = runBlocking {


        parkingExitViewModel.parkedVehicleResponse.observeForever(mockparkedVehiclesObserver)
        whenever(mockParkingRepository.getParkedVehicles("12")).thenReturn(emptyList())

        parkingExitViewModel.getParkedVehicles("15")

        mockparkedVehiclesObserver.onChanged(emptyList())



    }

    @Test
    fun getParkedVehicleReceipt_success() = runBlocking {
        parkingExitViewModel.parkingDetailResponse.observeForever(mockParkedDetailObserver)

        whenever(mockParkingRepository.getParkingDetail("123")).thenReturn(null)


        mockparkedVehiclesObserver.onChanged(null)
    }
}