package com.karthik.trimpark.base.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karthik.trimpark.base.db.entity.*


/**
 * created by Karthik A on 2019-08-01
 */
@Dao
interface ParkingDao {

    @Query("select count(*) from VehicleType")
    fun getVehicleTypeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllVehicleType(vehicles: List<VehicleType>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllParkingSlots(slots: List<ParkingSlot>)

    @Query("select * from VehicleType" )
    fun getVehicleTypes(): List<VehicleType>


    @Query("select count(*) from parkingslotentryaudit where vehicleId = :vehicleId and entryTime between :startTime and :endTime")
    fun getEntryCountperDay(vehicleId: String, startTime: Long, endTime: Long): Int

    @Query("select count(*) from parkingslot where vehicleTypeId > :vehicleTypeId and id not in (select parkingSlotId from ParkingSlotEntry)")
    fun getAvailableParkingSlotCount(vehicleTypeId: Int): Int

    @Query("select id from parkingslot where vehicleTypeId = :vehicleTypeId and id not in (select parkingSlotId from ParkingSlotEntry) order by vehicleTypeId asc limit 1 ")
    fun getAvailableExactParkingSlot(vehicleTypeId: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertParkingEntry(entry: ParkingSlotEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehicle(vehicle: Vehicle)

    @Query("select vehicleId from parkingslotentry where vehicleId like :searchVehicleId")
    fun searchParkedVehicleIds(searchVehicleId: String): List<String>

    @Query("select pse.id as id, pse.parkingSlotId as parkingSlotNo,pse.vehicleId as vehicleNo ,vt.type as vehicleType, pse.entryTime as entryTime, vt.rate1 as rate1, vt.rate2 as rate2 from ParkingSlotEntry as pse inner join Vehicle as v on v.id = pse.vehicleId inner join VehicleType as vt on vt.id = v.vehicleTypeId where v.id = :vehicleId")
    fun getParkingDetail(vehicleId: String): ParkingReceipt


    @Query("select slotId from (select pse.parkingSlotId as slotId, ps.vehicleTypeId as slotType, count(ps.vehicleTypeId) as vehicleCount from parkingslotentry as pse inner join parkingslot as ps on pse.parkingSlotId = ps.id inner join vehicle as v on v.id = pse.vehicleId where v.vehicleTypeId != ps.vehicleTypeId group by pse.parkingSlotId) where (slotType = 2 and vehicleCount < 2) or (slotType = 3 and vehicleCount < 3) or (slotType = 4 and vehicleCount < 4) limit 1")
    fun getHalfFilledParkingSlot(): List<String>

    @Query("select id from parkingslot where vehicleTypeId >= :vehicleTypeId and id not in (select parkingSlotId from ParkingSlotEntry) order by vehicleTypeId asc limit 1 ")
    fun getExactOrOtherParkingSlot(vehicleTypeId: Int): List<String>


    @Insert
    fun insertParkingEntryAudit(parkingaudit: ParkingSlotEntryAudit)

    @Query("delete from parkingslotentry where id = :entryId")
    fun deleteSlotEntry(entryId: Int)

}