package com.example.bookminiproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookminiproject.model.WorkLocal

@Database(entities = [WorkLocal::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var Instance: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BookDatabase::class.java, "book_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}