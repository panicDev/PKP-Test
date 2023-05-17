package com.panicdev.pkptest.di

import com.panicdev.pkptest.data.PostRepositoryImpl
import com.panicdev.pkptest.domain.dispatcher.CoroutinesDispatchersProvider
import com.panicdev.pkptest.domain.dispatcher.CoroutinesDispatchersProviderImpl
import com.panicdev.pkptest.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    fun provideCoroutinesDispatchersProvider(coroutinesDispatchersProviderImpl: CoroutinesDispatchersProviderImpl): CoroutinesDispatchersProvider

    @Binds
    fun providePostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository
}
