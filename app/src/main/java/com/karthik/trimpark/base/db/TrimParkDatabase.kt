package com.karthik.trimpark.base.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.karthik.trimpark.base.db.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * created by Karthik A
 */
@Database(entities = [VehicleType::class,Location::class,
    ParkingSlot::class,Vehicle::class,
    ParkingSlotEntry::class,ParkingSlotEntryAudit::class], version = 1)
abstract class TrimParkDatabase: RoomDatabase() {

    abstract fun getParkingDao(): ParkingDao

    companion object {
        @Volatile
        private var INSTANCE: TrimParkDatabase? = null

        fun getDatabase(context: Context): TrimParkDatabase{

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrimParkDatabase::class.java,
                    "parking_database"
                ).build()
                INSTANCE = instance

                instance.populateInitialData()

                return instance
            }
        }

    }


    fun populateInitialData()
    {
        GlobalScope.launch {

            withContext(Dispatchers.IO){
                if(getParkingDao().getVehicleTypeCount() == 0)
                {
                    runInTransaction {

                        var vehicles = listOf(VehicleType(1,"MotorCycle",50,25),
                            VehicleType(2,"Small Car",100,45),
                            VehicleType(3,"Medium Car",150,75),
                            VehicleType(4,"Large Car",200,100)
                        )

                        getParkingDao().insertAllVehicleType(vehicles)


                        var slots: MutableList<ParkingSlot> = mutableListOf()

                        for(i in 1..5)
                        {
                            when {
                                i%4 == 0 -> slots.add(ParkingSlot("F0S$i",4))
                                i%2 == 0 -> slots.add(ParkingSlot("F0S$i",2))
                                i%3 == 0 -> slots.add(ParkingSlot("F0S$i",3))
                                else -> slots.add(ParkingSlot("F0S$i",1))
                            }

                        }

                        getParkingDao().insertAllParkingSlots(slots)

                    }
                }
            }

        }

    }
}