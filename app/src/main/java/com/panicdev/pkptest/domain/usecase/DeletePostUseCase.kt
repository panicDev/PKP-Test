package com.panicdev.pkptest.domain.usecase

import com.panicdev.pkptest.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Unit {
        return repository.deletePost(postId)
    }
}
