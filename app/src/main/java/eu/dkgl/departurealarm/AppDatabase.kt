package eu.dkgl.departurealarm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.dkgl.departurealarm.dao.PlannedDepartureDao
import eu.dkgl.departurealarm.entity.PlannedDeparture

@Database(entities = [PlannedDeparture::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plannedDepartureDao(): PlannedDepartureDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}