package com.dicoding.smartcashier.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("keterangan")
    val keterangan: String?,
    @ColumnInfo("nama")
    val nama: String?,
    @ColumnInfo("hargaJual")
    val hargaJual: String?,
    @ColumnInfo("hargaBeli")
    val hargaBeli: String?,
    @ColumnInfo("stock")
    val stock: String?
)
