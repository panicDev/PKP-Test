package com.panicdev.pkptest.domain.repository

import com.panicdev.pkptest.domain.entity.Post

interface PostRepository {
    suspend fun getPosts(
        start: Int,
        limit: Int
    ): List<Post>

    suspend fun createPost(post: Post): Post

    suspend fun editPost(postId: Int, post: Post): Post

    suspend fun deletePost(postId: Int): Unit
}
