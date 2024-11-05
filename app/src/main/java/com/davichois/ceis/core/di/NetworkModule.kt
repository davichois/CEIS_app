package com.davichois.ceis.core.di

import com.davichois.ceis.core.common.Constants
import com.davichois.ceis.data.remote.AuthAPI
import com.davichois.ceis.data.remote.EventAPI
import com.davichois.ceis.data.remote.UserAPI
import com.davichois.ceis.data.repository.AuthRepositoryImplementation
import com.davichois.ceis.data.repository.EventRepositoryImplementation
import com.davichois.ceis.data.repository.UserRepositoryImplementation
import com.davichois.ceis.domain.repository.AuthRepository
import com.davichois.ceis.domain.repository.EventRepository
import com.davichois.ceis.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Named("CEISApiIterm")
    @Provides
    fun provideRetrofitCEIS(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    // Module's dependence injection API
    @Singleton
    @Provides
    fun authAPIClient(@Named("CEISApiIterm") retrofit: Retrofit): AuthAPI {
        return retrofit.create(AuthAPI::class.java)
    }

    @Singleton
    @Provides
    fun userAPIClient(@Named("CEISApiIterm") retrofit: Retrofit): UserAPI {
        return retrofit.create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun eventAPIClient(@Named("CEISApiIterm") retrofit: Retrofit): EventAPI {
        return retrofit.create(EventAPI::class.java)
    }

    // Repository injection module
    @Provides
    @Singleton
    fun authRepository(api: AuthAPI): AuthRepository {
        return AuthRepositoryImplementation(api = api)
    }

    @Provides
    @Singleton
    fun userRepository(api: UserAPI): UserRepository {
        return UserRepositoryImplementation(api = api)
    }

    @Provides
    @Singleton
    fun eventRepository(api: EventAPI): EventRepository {
        return EventRepositoryImplementation(api = api)
    }

}