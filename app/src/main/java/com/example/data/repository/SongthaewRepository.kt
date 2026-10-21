package com.example.data.repository

import android.util.Log
import com.example.data.local.AppDatabase
import com.example.data.local.InitialData
import com.example.data.model.SearchHistoryItem
import com.example.data.model.SongthaewRoute
import com.example.data.model.TripBooking
import com.example.data.model.UserBooking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlin.random.Random

class SongthaewRepository(private val database: AppDatabase) {

    val allRoutes: Flow<List<SongthaewRoute>> = database.routeDao().getAllRoutes()
        .catch { e ->
            Log.e("SongthaewRepo", "Error fetching routes from Room", e)
            emit(InitialData.initialRoutes)
        }
        .flowOn(Dispatchers.IO)

    val allBookings: Flow<List<UserBooking>> = database.bookingDao().getAllBookings()
        .catch { e ->
            Log.e("SongthaewRepo", "Error fetching bookings from Room", e)
            emit(emptyList())
        }
        .flowOn(Dispatchers.IO)

    val allTripBookings: Flow<List<TripBooking>> = database.tripBookingDao().getAllTripBookings()
        .catch { e ->
            Log.e("SongthaewRepo", "Error fetching trip bookings from Room", e)
            emit(emptyList())
        }
        .flowOn(Dispatchers.IO)

    val allSearchHistory: Flow<List<SearchHistoryItem>> = database.searchHistoryDao().getAllSearchHistory()
        .catch { e ->
            Log.e("SongthaewRepo", "Error fetching search history from Room", e)
            emit(emptyList())
        }
        .flowOn(Dispatchers.IO)

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        try {
            InitialData.initialRoutes.forEach { route ->
                val existing = database.routeDao().getRouteById(route.id)
                if (existing == null) {
                    database.routeDao().insertRoute(route)
                } else {
                    database.routeDao().insertRoute(route.copy(isFavorite = existing.isFavorite))
                }
            }
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error seeding initial data", e)
        }
    }

    suspend fun toggleFavorite(routeId: String) = withContext(Dispatchers.IO) {
        try {
            database.routeDao().toggleFavorite(routeId)
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error toggling favorite", e)
        }
    }

    suspend fun insertRoute(route: SongthaewRoute) = withContext(Dispatchers.IO) {
        try {
            database.routeDao().insertRoute(route)
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error inserting route", e)
        }
    }

    suspend fun updateRoute(route: SongthaewRoute) = withContext(Dispatchers.IO) {
        try {
            database.routeDao().updateRoute(route)
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error updating route", e)
        }
    }

    suspend fun deleteRoute(routeId: String) = withContext(Dispatchers.IO) {
        try {
            database.routeDao().deleteRoute(routeId)
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error deleting route", e)
        }
    }

    suspend fun addSearchHistory(query: String) = withContext(Dispatchers.IO) {
        try {
            val trimmed = query.trim()
            if (trimmed.length >= 2) {
                database.searchHistoryDao().deleteByQuery(trimmed)
                database.searchHistoryDao().insertSearch(SearchHistoryItem(query = trimmed))
            }
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error adding search history", e)
        }
    }

    suspend fun deleteSearchItem(query: String) = withContext(Dispatchers.IO) {
        try {
            database.searchHistoryDao().deleteByQuery(query)
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error deleting search item", e)
        }
    }

    suspend fun clearSearchHistory() = withContext(Dispatchers.IO) {
        try {
            database.searchHistoryDao().clearAll()
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error clearing search history", e)
        }
    }

    suspend fun bookPackage(packageId: Int, packageTitle: String, travelDate: String): String = withContext(Dispatchers.IO) {
        val bookingCode = "CM-BOOKING-${Random.nextInt(1000, 9999)}"
        try {
            database.bookingDao().insertBooking(
                UserBooking(
                    bookingCode = bookingCode,
                    packageId = packageId,
                    packageTitle = packageTitle,
                    travelDate = travelDate
                )
            )
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error saving booking", e)
        }
        bookingCode
    }

    suspend fun insertTripBooking(booking: TripBooking): Long = withContext(Dispatchers.IO) {
        try {
            database.tripBookingDao().insertTripBooking(booking)
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error saving trip booking", e)
            -1L
        }
    }

    suspend fun cancelTripBooking(id: Long) = withContext(Dispatchers.IO) {
        try {
            database.tripBookingDao().updateStatus(id, "Cancelled")
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error cancelling trip booking", e)
        }
    }

    suspend fun deleteTripBooking(id: Long) = withContext(Dispatchers.IO) {
        try {
            database.tripBookingDao().deleteTripBooking(id)
        } catch (e: Exception) {
            Log.e("SongthaewRepo", "Error deleting trip booking", e)
        }
    }
}
