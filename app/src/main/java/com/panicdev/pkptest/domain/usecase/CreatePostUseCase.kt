package com.panicdev.pkptest.domain.usecase

import com.panicdev.pkptest.domain.entity.Post
import com.panicdev.pkptest.domain.repository.PostRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(post: Post): Post {
        return repository.createPost(post)
    }
}
