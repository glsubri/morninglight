package ch.subri.morninglight.di

import android.content.Context
import ch.subri.morninglight.MorningLight
import ch.subri.morninglight.data.api.ble.BLEScanner
import ch.subri.morninglight.data.api.ble.DeviceScanner
import ch.subri.morninglight.data.api.preferences.Preferences
import ch.subri.morninglight.data.api.preferences.PreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MorningLight {
        return app as MorningLight
    }

    // https://medium.com/androiddevelopers/create-an-application-coroutinescope-using-hilt-dd444e721528
    @Singleton
    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Singleton
    @Provides
    fun provideDeviceScanner(): DeviceScanner {
        return BLEScanner()
    }

    @Singleton
    @Provides
    fun providePreferences(
        @ApplicationContext context: Context,
    ): Preferences {
        return PreferencesImpl(
            context = context,
            coroutineScope = provideCoroutineScope(),
        )
    }
}