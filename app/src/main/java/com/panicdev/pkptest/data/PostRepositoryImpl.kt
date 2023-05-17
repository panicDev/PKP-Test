package com.panicdev.pkptest.data

import com.panicdev.pkptest.data.remote.ApiService
import com.panicdev.pkptest.domain.dispatcher.CoroutinesDispatchersProvider
import com.panicdev.pkptest.domain.entity.Post
import com.panicdev.pkptest.domain.repository.PostRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dispatchersProvider: CoroutinesDispatchersProvider
) : PostRepository {
    override suspend fun getPosts(start: Int, limit: Int): List<Post> {
        return withContext(dispatchersProvider.io) {
            apiService.getPosts(start = start, limit = limit).map {
                Post(
                    body = it.body,
                    title = it.title,
                    id = it.id,
                    userId = it.userId
                )
            }
        }
    }

    override suspend fun createPost(post: Post): Post {
        return withContext(dispatchersProvider.io) {
            apiService.createPost(post)
        }
    }

    override suspend fun editPost(postId: Int, post: Post): Post {
        return withContext(dispatchersProvider.io) {
            apiService.updatePost(postId, post)
        }
    }

    override suspend fun deletePost(postId: Int): Unit {
        return withContext(dispatchersProvider.io) {
            apiService.deletePost(postId)
        }
    }
}
