package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SearchHistoryItem
import com.example.data.model.SongthaewRoute
import com.example.data.model.TripBooking
import com.example.data.model.UserBooking
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Query("SELECT * FROM routes")
    fun getAllRoutes(): Flow<List<SongthaewRoute>>

    @Query("SELECT * FROM routes WHERE id = :id")
    suspend fun getRouteById(id: String): SongthaewRoute?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: SongthaewRoute)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routes: List<SongthaewRoute>)

    @Update
    suspend fun updateRoute(route: SongthaewRoute)

    @Query("UPDATE routes SET isFavorite = NOT isFavorite WHERE id = :routeId")
    suspend fun toggleFavorite(routeId: String)

    @Query("DELETE FROM routes WHERE id = :routeId")
    suspend fun deleteRoute(routeId: String)

    @Query("SELECT COUNT(*) FROM routes")
    suspend fun getRoutesCount(): Int
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<UserBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: UserBooking): Long
}

@Dao
interface TripBookingDao {
    @Query("SELECT * FROM trip_bookings ORDER BY createdAt DESC")
    fun getAllTripBookings(): Flow<List<TripBooking>>

    @Query("SELECT * FROM trip_bookings WHERE id = :id")
    suspend fun getTripBookingById(id: Long): TripBooking?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTripBooking(booking: TripBooking): Long

    @Query("UPDATE trip_bookings SET status = :newStatus WHERE id = :id")
    suspend fun updateStatus(id: Long, newStatus: String)

    @Query("DELETE FROM trip_bookings WHERE id = :id")
    suspend fun deleteTripBooking(id: Long)

    @Query("SELECT COUNT(*) FROM trip_bookings")
    suspend fun getCount(): Int
}

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
    fun getAllSearchHistory(): Flow<List<SearchHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(item: SearchHistoryItem)

    @Query("DELETE FROM search_history WHERE query = :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM search_history")
    suspend fun clearAll()
}
