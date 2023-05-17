package com.panicdev.pkptest.presentation.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.dylanc.viewbinding.binding
import com.google.android.material.snackbar.Snackbar
import com.panicdev.pkptest.R
import com.panicdev.pkptest.databinding.FragmentListBinding
import com.panicdev.pkptest.domain.entity.Post
import com.panicdev.pkptest.presentation.mvi.renderState
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.viewmodel.observe

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    companion object {
        private var DEFAULT_POSTS_SIZE = 100
        private var DEFAULT_POSTS_ADDED = 0
    }

    private val viewModel by activityViewModels<PostListViewModel>()
    private val binding by binding<FragmentListBinding>()
    private lateinit var adapter: PostsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dispatch(ListAction.LoadPostsAction)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.dispatch(ListAction.RefreshPostsAction)
        }

        adapter = PostsAdapter(
            arrayListOf(),
            onClick = {
                viewModel.dispatch(ListAction.OpenPostDetail(it))
            },
            onEditPost = { post, editPosition ->
                viewModel.editPost(post, editPosition)
            },
            onDeletePost = { post, delPosition ->
                viewModel.deletePost(post, delPosition)
            })

        binding.recycler.layoutManager = LinearLayoutManager(activity)
        binding.recycler.itemAnimator = DefaultItemAnimator()
        binding.recycler.adapter = adapter

        viewModel.observe(viewLifecycleOwner, state = ::render, sideEffect = ::sideEffect)
    }

    private fun render(state: ListState) {
        state.posts.renderState(
            onLoading = {
                binding.swipeRefresh.isRefreshing = true
            },
            onError = {
                binding.swipeRefresh.isRefreshing = false
                showErrorDialog(it)
            },
            onSuccess = {
                binding.swipeRefresh.isRefreshing = false
                adapter.swapData(it as ArrayList<Post>)
            }
        )
    }

    private fun sideEffect(sideEffect: ListEffect) {
        when (sideEffect) {
            is ListEffect.OpenPostDetail -> {}

            is ListEffect.DeleteSideEffect -> {
                MaterialDialog(requireContext()).show {
                    message(R.string.delete_post_desc)
                    lifecycleOwner(viewLifecycleOwner)
                    positiveButton(R.string.yes) {
                        viewModel.dispatch(
                            ListAction.DeleteAction(
                                sideEffect.item,
                                sideEffect.position
                            )
                        )
                    }
                    negativeButton(R.string.cancel)
                }
            }

            is ListEffect.EditSideEffect -> {
                MaterialDialog(requireContext()).show {
                    message(R.string.edit_post_desc)
                    lifecycleOwner(viewLifecycleOwner)
                    positiveButton(R.string.yes) {
                        showDialogEdit(sideEffect.item, sideEffect.position)
                    }
                    negativeButton(R.string.cancel)
                }
            }

            is ListEffect.CreateSideEffect -> {
                val post = Post(
                    sideEffect.desc,
                    ++DEFAULT_POSTS_SIZE,
                    "Added title ${++DEFAULT_POSTS_ADDED}",
                    1
                )
                viewModel.dispatch(ListAction.CreateAction(post))
            }

            is ListEffect.ErrorMessageSideEffect -> {
                showErrorDialog(sideEffect.message)
            }

            is ListEffect.PostDeletedSideEffect -> {
                adapter.deletePost(sideEffect.position)
                showSnackBar("Post Deleted")
            }

            is ListEffect.PostAddedSideEffect -> {
                adapter.addPost(sideEffect.item)
                showSnackBar("Post Added")
            }

            is ListEffect.PostUpdatedSideEffect -> {
                adapter.notifyItemChanged(sideEffect.position, sideEffect.item)
                showSnackBar("Post Updated")
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun showDialogEdit(item: Post, pos: Int) {
        MaterialDialog(requireContext()).show {
            cornerRadius(16F)
            title(R.string.create_new_post_title)
            input(allowEmpty = false, prefill = item.body) { _, text ->
                item.body = text.toString()
            }
            positiveButton(R.string.submit) {
                viewModel.dispatch(
                    ListAction.EditAction(
                        item,
                        pos
                    )
                )
            }
            negativeButton(R.string.cancel)
        }

    }

    private fun showErrorDialog(msg: String?) {
        MaterialDialog(requireContext()).show {
            message(text = msg)
            positiveButton(R.string.close)
        }
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }

}
