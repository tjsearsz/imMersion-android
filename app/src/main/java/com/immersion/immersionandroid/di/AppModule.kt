package com.immersion.immersionandroid.di

import com.apollographql.apollo3.ApolloClient
import com.immersion.immersionandroid.AuthorizationInterceptor
import com.immersion.immersionandroid.dataAccess.ApolloAugmentedImageClient
import com.immersion.immersionandroid.dataAccess.FileStackDataSource
import com.immersion.immersionandroid.dataAccess.IFilestackDataSource
import com.immersion.immersionandroid.dataAccess.IJobRepository
import com.immersion.immersionandroid.dataAccess.JobRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder().serverUrl("http://10.5.48.68:3000/graphql")
            .addHttpInterceptor(AuthorizationInterceptor()).build()
    }

    @Provides
    @Singleton
    fun provideFileStackAPI(): IFilestackDataSource {
        return Retrofit
            .Builder()
            .baseUrl("https://www.filestackapi.com/api/store/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IFilestackDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideFileStackRepository(api: IFilestackDataSource): FileStackDataSource {
        return FileStackDataSource(api)
    }

    @Provides
    @Singleton
    fun provideImmersionJobRepository(apolloClient: ApolloClient): IJobRepository {
        return JobRepository(apolloClient)
    }

    /*@Provides
    @Singleton
    fun provideAugmentedImageClient(apolloClient: ApolloClient): ApolloAugmentedImageClient {
        return ApolloAugmentedImageClient(apolloClient)
    }*/
}