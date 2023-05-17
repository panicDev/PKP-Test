package com.panicdev.pkptest.domain.usecase

import com.panicdev.pkptest.domain.entity.Post
import com.panicdev.pkptest.domain.repository.PostRepository
import javax.inject.Inject

class EditPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int, post: Post): Post {
        return repository.editPost(postId, post)
    }
}
