package com.example.bookminiproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookminiproject.model.WorkLocal

@Dao
abstract class BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertNewWork(work: WorkLocal)

    @Query("SELECT * FROM work")
    abstract suspend fun getAllWorks(): List<WorkLocal>

    @Query("SELECT * FROM work WHERE `key` = :key")
    abstract suspend fun getWork(key: String): WorkLocal

    @Query("DELETE FROM work WHERE `key` = :key")
    abstract suspend fun deleteWork(key: String)

    @Query("DELETE FROM work")
    abstract suspend fun deleteAllWorks()
}