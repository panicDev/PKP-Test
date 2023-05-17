package com.panicdev.pkptest.di

import com.panicdev.pkptest.data.remote.ApiService
import com.panicdev.pkptest.domain.repository.PostRepository
import com.panicdev.pkptest.domain.usecase.AllUseCase
import com.panicdev.pkptest.domain.usecase.CreatePostUseCase
import com.panicdev.pkptest.domain.usecase.DeletePostUseCase
import com.panicdev.pkptest.domain.usecase.EditPostUseCase
import com.panicdev.pkptest.domain.usecase.GetPostsUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(client)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi
                        .Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .apply {
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
                    .let(::addInterceptor)
            }
            .build()
    }

    @Provides
    fun provideAllUseCase(repository: PostRepository): AllUseCase {
        return AllUseCase(
            GetPostsUseCase(repository),
            CreatePostUseCase(repository),
            EditPostUseCase(repository),
            DeletePostUseCase(repository),

        )
    }


}
