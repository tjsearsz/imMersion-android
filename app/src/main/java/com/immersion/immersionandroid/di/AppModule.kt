package com.immersion.immersionandroid.di

import com.apollographql.apollo3.ApolloClient
import com.immersion.immersionandroid.dataAccess.ApolloAugmentedImageClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient{
        return ApolloClient.Builder().serverUrl("http://10.9.8.1:3000/graphql").build()
    }

   /* @Provides
    @Singleton
    fun provideAugmentedImageClient(apolloClient: ApolloClient): ApolloAugmentedImageClient {
        return ApolloAugmentedImageClient(apolloClient)
    }*/
}