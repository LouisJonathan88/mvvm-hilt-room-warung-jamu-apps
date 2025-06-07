package com.example.suweorajamuapps.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.suweorajamuapps.data.dao.BarangDao
import com.example.suweorajamuapps.data.dao.TransaksiDao
import com.example.suweorajamuapps.data.model.Barang
import com.example.suweorajamuapps.data.model.Transaksi

@Database(
    entities = [Barang::class, Transaksi::class],
    version = 2,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun barangDao(): BarangDao
    abstract fun transaksiDao(): TransaksiDao
}