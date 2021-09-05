package ch.subri.morninglight.di

import android.content.Context
import androidx.room.Room
import ch.subri.morninglight.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val databaseName = "MorningLightDB"

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        databaseName,
    ).build()

    @Provides
    fun provideAlarmDao(
        db: AppDatabase,
    ) = db.alarmDao()
}