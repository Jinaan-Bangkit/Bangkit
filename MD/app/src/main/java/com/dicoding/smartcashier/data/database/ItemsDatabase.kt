package com.dicoding.smartcashier.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.smartcashier.data.remote.response.AddResponse

@Database(
    entities = [AddResponse::class, Item::class], version = 1, exportSchema = false
)
abstract class ItemsDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemsDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ItemsDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, ItemsDatabase::class.java, "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}