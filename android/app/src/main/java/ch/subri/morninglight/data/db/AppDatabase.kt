package ch.subri.morninglight.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ch.subri.morninglight.data.entity.Alarm

@Database(entities = [
    Alarm::class,
], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}