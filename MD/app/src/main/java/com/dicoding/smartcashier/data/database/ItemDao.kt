package com.dicoding.smartcashier.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.smartcashier.data.remote.response.AddResponse

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<AddResponse>)

    @Query("SELECT * FROM item_table")
    fun getAllAddResponses(): LiveData<List<AddResponse>>

    @Update
    suspend fun getItems(item: Item)

    @Delete
    suspend fun delete(item: Item)

}