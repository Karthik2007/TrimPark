package com.karthik.trimpark.parkingentry.repository

import com.karthik.trimpark.base.db.TrimParkDatabase
import com.karthik.trimpark.util.TestCoroutineDispachterProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


/**
 * created by Karthik A on 2019-08-04
 */
class ParkingRepositoryTest {

    private lateinit var parkingRepository: ParkingRepository

    @Mock private lateinit var mockTrimParkDatabase: TrimParkDatabase


    @Before
    fun setUp()
    {
        MockitoAnnotations.initMocks(this)

        parkingRepository = ParkingRepository(mockTrimParkDatabase, TestCoroutineDispachterProvider())
    }


}