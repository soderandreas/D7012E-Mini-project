package com.example.bookminiproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookminiproject.model.WorksLocal

@Dao
abstract class BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertNewWork(work: WorksLocal)

    @Query("SELECT * FROM works")
    abstract suspend fun getAllWorks(): List<WorksLocal>

    @Query("DELETE FROM works")
    abstract suspend fun deleteAllWorks()
}