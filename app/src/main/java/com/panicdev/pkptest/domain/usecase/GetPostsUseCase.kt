package com.panicdev.pkptest.domain.usecase

import com.panicdev.pkptest.domain.entity.Post
import com.panicdev.pkptest.domain.repository.PostRepository
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(start: Int, limit: Int): List<Post> {
        return repository.getPosts(start = start, limit = limit)
    }
}
