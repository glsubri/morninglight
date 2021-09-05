package ch.subri.morninglight.data.db

import androidx.room.*
import ch.subri.morninglight.data.entity.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert
    suspend fun insert(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("SELECT * from alarm")
    suspend fun loadAll(): List<Alarm>

    @Query("SELECT * from alarm WHERE id != :id")
    suspend fun loadAllExcept(id: Long): List<Alarm>

    @Query("SELECT * from alarm")
    fun all(): Flow<List<Alarm>>

    @Query("SELECT * from alarm WHERE id = :id")
    fun alarm(id: Long): Alarm
}