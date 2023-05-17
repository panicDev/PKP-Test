package com.panicdev.pkptest.domain.usecase

data class AllUseCase(
    val getPosts: GetPostsUseCase,
    val createPost: CreatePostUseCase,
    val editPost: EditPostUseCase,
    val deletePost: DeletePostUseCase
)
