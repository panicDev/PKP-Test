package com.panicdev.pkptest.presentation.list

import com.panicdev.pkptest.domain.entity.Post
import com.panicdev.pkptest.domain.usecase.AllUseCase
import com.panicdev.pkptest.presentation.mvi.MviViewModel
import com.panicdev.pkptest.presentation.mvi.async
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val useCase: AllUseCase
) : MviViewModel<ListAction, ListState, ListEffect>(ListState()) {

    override fun dispatch(action: ListAction) = intent {
        when (action) {

            is ListAction.LoadPostsAction, ListAction.RefreshPostsAction -> {
                async { useCase.getPosts.invoke(0, 100) }.execute {
                    state.copy(posts = it)
                }
            }

            is ListAction.OpenPostDetail -> {
                postSideEffect(ListEffect.OpenPostDetail(action.item))
            }

            is ListAction.DeleteAction -> {
                runCatching {
                    useCase.deletePost.invoke(action.item.id)
                }.onSuccess {
                    postSideEffect(ListEffect.PostDeletedSideEffect(action.position))
                }.onFailure {
                    postSideEffect(ListEffect.ErrorMessageSideEffect(it.localizedMessage))
                }
            }

            is ListAction.EditAction -> {
                runCatching {
                    useCase.deletePost.invoke(action.item.id)
                }.onSuccess {
                    postSideEffect(ListEffect.PostUpdatedSideEffect(action.item, action.position))
                }.onFailure {
                    postSideEffect(ListEffect.ErrorMessageSideEffect(it.localizedMessage))
                }
            }

            is ListAction.CreateAction -> {
                runCatching {
                    useCase.deletePost.invoke(action.item.id)
                }.onSuccess {
                    postSideEffect(ListEffect.PostAddedSideEffect(action.item))
                }.onFailure {
                    postSideEffect(ListEffect.ErrorMessageSideEffect(it.localizedMessage))
                }
            }
        }
    }

    fun deletePost(item: Post, pos: Int) = intent {
        postSideEffect(ListEffect.DeleteSideEffect(item, pos))
    }

    fun editPost(item: Post, pos: Int) = intent {
        postSideEffect(ListEffect.EditSideEffect(item, pos))
    }

    fun createPost(desc: String) = intent {
        postSideEffect(ListEffect.CreateSideEffect(desc))
    }
}
