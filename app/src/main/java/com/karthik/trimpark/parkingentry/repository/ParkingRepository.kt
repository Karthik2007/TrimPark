package com.karthik.trimpark.parkingentry.repository

import com.karthik.trimpark.base.db.TrimParkDatabase
import com.karthik.trimpark.base.db.entity.*
import com.karthik.trimpark.base.util.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import java.util.*


/**
 * created by Karthik A on 2019-08-01
 */
class ParkingRepository(
    private val parkingDatabase: TrimParkDatabase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {

    suspend fun getVehicleTypes(): List<VehicleType> = withContext(coroutineDispatcherProvider.IO){

        return@withContext parkingDatabase.getParkingDao().getVehicleTypes()
    }

    suspend fun getFreeSlotsCount(vehicleTypeId: Int): Int = withContext(coroutineDispatcherProvider.IO){

        return@withContext parkingDatabase.getParkingDao().getAvailableParkingSlotCount(vehicleTypeId)
    }

    suspend fun getExactFreeSlot(vehicleTypeId: Int): List<String> = withContext(coroutineDispatcherProvider.IO){

        return@withContext parkingDatabase.getParkingDao().getAvailableExactParkingSlot(vehicleTypeId)
    }

    suspend fun getExactOrOtherFreeSlot(vehicleTypeId: Int): List<String> = withContext(coroutineDispatcherProvider.IO){

        return@withContext parkingDatabase.getParkingDao().getExactOrOtherParkingSlot(vehicleTypeId)
    }

    suspend fun getHalfFreeSlot(vehicleTypeId: Int): List<String> = withContext(coroutineDispatcherProvider.IO){

        return@withContext parkingDatabase.getParkingDao().getHalfFilledParkingSlot()
    }


    suspend fun registerParkingEntry(entry: ParkingSlotEntry): Boolean = withContext(coroutineDispatcherProvider.IO){

        return@withContext parkingDatabase.getParkingDao().insertParkingEntry(entry) > 0
    }

    suspend fun addVehicle(vehicle: Vehicle) = withContext(coroutineDispatcherProvider.IO){

        parkingDatabase.getParkingDao().insertVehicle(vehicle = vehicle)
    }


    suspend fun getParkedVehicles(vehicleId: String):List<String> = withContext(coroutineDispatcherProvider.IO){

        return@withContext parkingDatabase.getParkingDao().searchParkedVehicleIds(vehicleId)
    }

    suspend fun getParkingDetail(vehicleId: String): ParkingReceipt? = withContext(coroutineDispatcherProvider.IO){

        return@withContext parkingDatabase.getParkingDao().getParkingDetail(vehicleId)

    }


    suspend fun checkoutParking(parkingReciept: ParkingReceipt): Boolean = withContext(coroutineDispatcherProvider.IO) {

        var isSuccess = false
        parkingDatabase.runInTransaction {

            parkingDatabase.getParkingDao().insertParkingEntryAudit(ParkingSlotEntryAudit(parkingReciept.id,
                parkingSlotId = parkingReciept.parkingSlotNo,vehicleId = parkingReciept.vehicleNo,
                entryTime = parkingReciept.entryTime,exitTime = parkingReciept.exitTime!!,amount = parkingReciept.amount!!))
            parkingDatabase.getParkingDao().deleteSlotEntry(parkingReciept.id)

            isSuccess = true
        }

        return@withContext isSuccess
    }


    suspend fun isDiscountEligible(vehicleId: String): Boolean = withContext(coroutineDispatcherProvider.IO){

        var calendar = Calendar.getInstance()
        for(i in 1..5)
        {
            calendar.add(Calendar.DAY_OF_MONTH,- 1)
            calendar.set(Calendar.HOUR_OF_DAY,0)
            calendar.set(Calendar.MINUTE,0)
            calendar.set(Calendar.MILLISECOND,0)

            var startTime = calendar.timeInMillis
            calendar.set(Calendar.HOUR_OF_DAY,23)
            calendar.set(Calendar.MINUTE,59)
            calendar.set(Calendar.MILLISECOND,999)
            print(calendar.get(Calendar.DAY_OF_MONTH))
            var endTime = calendar.timeInMillis

            if(parkingDatabase.getParkingDao().getEntryCountperDay(vehicleId,startTime,endTime = endTime) == 0)
            {
                return@withContext false
            }
        }

        return@withContext true
    }


}