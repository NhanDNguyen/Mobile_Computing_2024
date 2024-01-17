package com.example.mobilecomputing.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(profile: Profile)

    @Update
    suspend fun update(profile: Profile)

    @Delete
    suspend fun delete(profile: Profile)

    @Query("SELECT * from profiles WHERE id= :id")
    fun getProfile(id: Int): Profile

    @Query("SELECT * from profiles ORDER BY name ASC")
    fun getAllProfiles(): Flow<List<Profile>>
}