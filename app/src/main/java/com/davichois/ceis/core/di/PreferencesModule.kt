package com.davichois.ceis.core.di

import android.content.Context
import com.davichois.ceis.data.preferences.PreferencesImplement
import com.davichois.ceis.data.repository.PreferencesRepositoryImplementation
import com.davichois.ceis.domain.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext app: Context) = PreferencesImplement(app)

    @Provides
    @Singleton
    fun providePreferenceRepository(api: PreferencesImplement): PreferencesRepository {
        return PreferencesRepositoryImplementation(api = api)
    }

}