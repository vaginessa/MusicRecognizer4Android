package com.mrsep.musicrecognizer.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.mrsep.musicrecognizer.UserPreferencesProto
import com.mrsep.musicrecognizer.core.common.BidirectionalMapper
import com.mrsep.musicrecognizer.core.common.di.ApplicationScope
import com.mrsep.musicrecognizer.core.common.di.IoDispatcher
import com.mrsep.musicrecognizer.data.preferences.FontSizeDo
import com.mrsep.musicrecognizer.data.preferences.FallbackActionDo
import com.mrsep.musicrecognizer.data.preferences.UserPreferencesDo
import com.mrsep.musicrecognizer.data.preferences.UserPreferencesProtoSerializer
import com.mrsep.musicrecognizer.data.preferences.mappers.FontSizeDoMapper
import com.mrsep.musicrecognizer.data.preferences.mappers.LyricsFontStyleDoMapper
import com.mrsep.musicrecognizer.data.preferences.mappers.RequiredServicesDoMapper
import com.mrsep.musicrecognizer.data.preferences.mappers.FallbackActionDoMapper
import com.mrsep.musicrecognizer.data.preferences.mappers.FallbackPolicyDoMapper
import com.mrsep.musicrecognizer.data.preferences.mappers.HapticFeedbackDoMapper
import com.mrsep.musicrecognizer.data.preferences.mappers.TrackFilterDoMapper
import com.mrsep.musicrecognizer.data.preferences.mappers.UserPreferencesDoMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

private const val USER_PREFERENCES_STORE = "USER_PREFERENCES_STORE"

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {

    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext appContext: Context,
        @ApplicationScope appScope: CoroutineScope,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DataStore<UserPreferencesProto> {
        return DataStoreFactory.create(
            serializer = UserPreferencesProtoSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserPreferencesProto.getDefaultInstance() }
            ),
            scope = CoroutineScope(appScope.coroutineContext + ioDispatcher),
            produceFile = { appContext.dataStoreFile(USER_PREFERENCES_STORE) }
        )
    }

}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface PreferencesMappersModule {

    @Binds
    fun bindUserPreferencesDoMapper(implementation: UserPreferencesDoMapper):
            BidirectionalMapper<UserPreferencesProto, UserPreferencesDo>

    @Binds
    fun bindRequiredServicesDoMapper(implementation: RequiredServicesDoMapper):
            BidirectionalMapper<UserPreferencesProto.RequiredServicesProto, UserPreferencesDo.RequiredServicesDo>

    @Binds
    fun bindFallbackActionDoMapper(implementation: FallbackActionDoMapper):
            BidirectionalMapper<UserPreferencesProto.FallbackActionProto, FallbackActionDo>

    @Binds
    fun bindFallbackPolicyDoMapper(implementation: FallbackPolicyDoMapper):
            BidirectionalMapper<UserPreferencesProto.FallbackPolicyProto, UserPreferencesDo.FallbackPolicyDo>

    @Binds
    fun bindFontSizeDoMapper(implementation: FontSizeDoMapper):
            BidirectionalMapper<UserPreferencesProto.FontSizeProto, FontSizeDo>

    @Binds
    fun bindLyricsFontStyleDoMapper(implementation: LyricsFontStyleDoMapper):
            BidirectionalMapper<UserPreferencesProto.LyricsFontStyleProto, UserPreferencesDo.LyricsFontStyleDo>

    @Binds
    fun bindTrackFilterDoMapper(implementation: TrackFilterDoMapper):
            BidirectionalMapper<UserPreferencesProto.TrackFilterProto, UserPreferencesDo.TrackFilterDo>

    @Binds
    fun bindHapticFeedbackDoMapper(implementation: HapticFeedbackDoMapper):
            BidirectionalMapper<UserPreferencesProto.HapticFeedbackProto, UserPreferencesDo.HapticFeedbackDo>

}