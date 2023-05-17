package com.panicdev.pkptest.presentation.list

import com.panicdev.pkptest.domain.entity.Post
import com.panicdev.pkptest.presentation.mvi.Async
import com.panicdev.pkptest.presentation.mvi.MviAction
import com.panicdev.pkptest.presentation.mvi.MviEffect
import com.panicdev.pkptest.presentation.mvi.MviState
import com.panicdev.pkptest.presentation.mvi.Uninitialized

sealed class ListAction : MviAction {
    object LoadPostsAction : ListAction()
    object RefreshPostsAction : ListAction()
    data class OpenPostDetail(val item: Post) : ListAction()
    data class CreateAction(val item: Post) : ListAction()
    data class DeleteAction(val item: Post,val position: Int) : ListAction()
    data class EditAction(val item: Post,val position: Int) : ListAction()
}

data class ListState(
    val posts: Async<List<Post>> = Uninitialized
) : MviState

sealed class ListEffect : MviEffect {
    data class OpenPostDetail(val item: Post) : ListEffect()
    data class CreateSideEffect(val desc: String) : ListEffect()
    data class EditSideEffect(val item: Post,val position: Int) : ListEffect()
    data class DeleteSideEffect(val item: Post,val position: Int) : ListEffect()
    data class PostAddedSideEffect(val item: Post) : ListEffect()
    data class PostUpdatedSideEffect(val item: Post,val position: Int) : ListEffect()
    data class PostDeletedSideEffect(val position: Int) : ListEffect()
    data class ErrorMessageSideEffect(val message: String?) : ListEffect()

}
