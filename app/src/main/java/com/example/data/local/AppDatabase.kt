package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.SearchHistoryItem
import com.example.data.model.SongthaewRoute
import com.example.data.model.TripBooking
import com.example.data.model.UserBooking

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Schema was identical between 1 and 2
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `trip_bookings` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `bookingCode` TEXT NOT NULL,
                `routeId` TEXT NOT NULL,
                `routeName` TEXT NOT NULL,
                `routeColor` TEXT NOT NULL,
                `pickupLocation` TEXT NOT NULL,
                `dropoffLocation` TEXT NOT NULL,
                `travelDate` TEXT NOT NULL,
                `travelTime` TEXT NOT NULL,
                `passengers` INTEGER NOT NULL,
                `bookingType` TEXT NOT NULL,
                `driverId` TEXT NOT NULL,
                `driverName` TEXT NOT NULL,
                `driverRating` REAL NOT NULL,
                `driverPhone` TEXT NOT NULL,
                `vehiclePlate` TEXT NOT NULL,
                `vehicleModel` TEXT NOT NULL,
                `vehicleConditionGrade` TEXT NOT NULL,
                `cleanlinessScore` REAL NOT NULL,
                `totalPrice` INTEGER NOT NULL,
                `note` TEXT NOT NULL,
                `status` TEXT NOT NULL,
                `createdAt` INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}

@Database(
    entities = [
        SongthaewRoute::class,
        UserBooking::class,
        SearchHistoryItem::class,
        TripBooking::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
    abstract fun bookingDao(): BookingDao
    abstract fun tripBookingDao(): TripBookingDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "songthaew_cm_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration(true)
                    .fallbackToDestructiveMigrationOnDowngrade(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
