package com.karthik.trimpark.base.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Duration


/**
 * created by Karthik A on 2019-08-01
 */
@Entity
data class VehicleType(
    @PrimaryKey var id: Int,
    var type: String,
    var rate1: Int,
    var rate2: Int
){
    override fun toString(): String {
        return type
    }
}


@Entity
data class Location(
    @PrimaryKey var id: Int,
    var location: String
)


@Entity(foreignKeys = [ForeignKey(
    entity = VehicleType::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("vehicleTypeId"))] )
data class ParkingSlot(
    @PrimaryKey var id: String,
    var vehicleTypeId: Int
)


@Entity(foreignKeys = [ForeignKey(
    entity = VehicleType::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("vehicleTypeId"))] )
data class Vehicle(
    @PrimaryKey var id: String,
    var vehicleTypeId: Int,
    var ownerName: String?,
    var ownerPhoneNumber: String?
)


@Entity(foreignKeys = [
    ForeignKey(
    entity = Vehicle::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("vehicleId")),

    ForeignKey(
    entity = ParkingSlot::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("parkingSlotId"))] )
data class ParkingSlotEntry(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var parkingSlotId: String,
    var vehicleId: String,
    var entryTime: Long
)


@Entity(foreignKeys = [
    ForeignKey(
        entity = Vehicle::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("vehicleId")),

    ForeignKey(
        entity = ParkingSlot::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parkingSlotId"))] )
data class ParkingSlotEntryAudit(
    @PrimaryKey var id: Int,
    var parkingSlotId: String,
    var vehicleId: String,
    var entryTime: Long,
    var exitTime: Long,
    var amount: Int
)


data class ParkingReceipt(
    var id: Int,
    var parkingSlotNo: String,
    var vehicleNo: String,
    var rate1:Int,
    var rate2:Int,
    var entryTime: Long,
    var vehicleType: String,
    var exitTime: Long?,
    var amount: Int?,
    var duration: Int?,
    var discount: Int? = 0
)



