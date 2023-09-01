package com.immersion.immersionandroid.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.immersion.immersionandroid.AuthorizationInterceptor
import com.immersion.immersionandroid.dataAccess.ACRUImmersionRepository
import com.immersion.immersionandroid.dataAccess.FileStackDataSource
import com.immersion.immersionandroid.dataAccess.IFilestackDataSource
import com.immersion.immersionandroid.dataAccess.JobImmersionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.immersion.AddBranchMutation
import com.immersion.AddCompanyMutation
import com.immersion.AddJobMutation
// import com.immersion.GetAllAugmentedImagesQuery
import com.immersion.GetBranchesQuery
import com.immersion.GetCompaniesQuery
import com.immersion.GetJobsQuery
import com.immersion.SignInMutation
import com.immersion.UpdateUserMutation
import com.immersion.immersionandroid.dataAccess.AugmentedImageRepository
import com.immersion.immersionandroid.dataAccess.AuthorizationImmersionRepository
import com.immersion.immersionandroid.dataAccess.BranchImmersionRepository
import com.immersion.immersionandroid.dataAccess.CompanyImmersionRepository
import com.immersion.immersionandroid.dataAccess.CompanySectorImmersionRepository
import com.immersion.immersionandroid.dataAccess.DataStoreRepository
import com.immersion.immersionandroid.dataAccess.IAugmentedImageRepository
import com.immersion.immersionandroid.dataAccess.IAuthorizationImmersionRepository
import com.immersion.immersionandroid.dataAccess.ICompanySectorImmersionRepository
import com.immersion.immersionandroid.dataAccess.IDataStoreRepository
import com.immersion.immersionandroid.dataAccess.IUserRepository
import com.immersion.immersionandroid.dataAccess.UserImmersionRepository
import com.immersion.immersionandroid.domain.Branch
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import com.immersion.immersionandroid.domain.Job
import com.immersion.immersionandroid.domain.User
import dagger.hilt.android.qualifiers.ApplicationContext


//TODO: divide this app module into submodules to have more granularity
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(dataStoreRepository: IDataStoreRepository): ApolloClient {
        return ApolloClient.Builder().serverUrl("http://10.5.51.90:3000/graphql")
            .addHttpInterceptor(AuthorizationInterceptor(dataStoreRepository)).build()
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
    fun provideImmersionJobRepository(apolloClient: ApolloClient): ACRUImmersionRepository<Job, AddJobMutation.Data, IEmployerOwnerShip?, AddJobMutation.Data, Boolean, GetJobsQuery.Data, List<IEmployerOwnerShip>, String> {
        return JobImmersionRepository(apolloClient)
    }

    @Provides
    @Singleton
    fun provideImmersionCompanyRepository(apolloClient: ApolloClient): ACRUImmersionRepository<Company, AddCompanyMutation.Data, IEmployerOwnerShip?, AddCompanyMutation.Data, Boolean, GetCompaniesQuery.Data, List<IEmployerOwnerShip>, Unit> {
        return CompanyImmersionRepository(apolloClient)
    }

    @Provides
    @Singleton
    fun provideImmersionBranchRepository(apolloClient: ApolloClient): ACRUImmersionRepository<Branch, AddBranchMutation.Data, IEmployerOwnerShip?, AddBranchMutation.Data, Boolean, GetBranchesQuery.Data, List<IEmployerOwnerShip>, String> {
        return BranchImmersionRepository(apolloClient)
    }

    @Provides
    @Singleton
    fun provideImmersionUserRepository(apolloClient: ApolloClient): ACRUImmersionRepository<User, SignInMutation.Data, Boolean, UpdateUserMutation.Data, Boolean, GetBranchesQuery.Data, Boolean, Unit> {
        return UserImmersionRepository(apolloClient)
    }

    @Provides
    @Singleton
    fun provideImmersionAuthorizationRepository(
        apolloClient: ApolloClient,
        dataStoreRepository: IDataStoreRepository
    ): IAuthorizationImmersionRepository {
        return AuthorizationImmersionRepository(apolloClient, dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideImmersionDataStoreRepository(@ApplicationContext context: Context): IDataStoreRepository {
        return DataStoreRepository(context)
    }

    @Provides
    @Singleton
    fun provideAugmentedImageRepository(apolloClient: ApolloClient): IAugmentedImageRepository {
        return AugmentedImageRepository(apolloClient)
    }

    @Provides
    @Singleton
    fun provideCompanySectorRepository(apolloClient: ApolloClient): ICompanySectorImmersionRepository {
        return CompanySectorImmersionRepository(apolloClient)
    }

    @Provides
    @Singleton
    fun provideUserRepository(apolloClient: ApolloClient): IUserRepository {
        return UserImmersionRepository(apolloClient)
    }
}