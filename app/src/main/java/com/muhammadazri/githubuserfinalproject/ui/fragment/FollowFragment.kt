package com.muhammadazri.githubuserfinalproject.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammadazri.githubuserfinalproject.R
import com.muhammadazri.githubuserfinalproject.adapter.UserAdapter
import com.muhammadazri.githubuserfinalproject.databinding.FragmentFollowBinding
import com.muhammadazri.githubuserfinalproject.utility.State
import com.muhammadazri.githubuserfinalproject.utility.TypeView
import com.muhammadazri.githubuserfinalproject.viewmodel.FollowViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import com.muhammadazri.githubuserfinalproject.utility.ShowStates

class FollowFragment : Fragment(), ShowStates {

    companion object {
        fun newInstance(username: String, type: String) =
                FollowFragment().apply {
                    arguments = Bundle().apply {
                        putString(USERNAME, username)
                        putString(TYPE, type)
                    }
                }

        private const val USERNAME = "username"
        private const val TYPE = "type"
    }

    private lateinit var followBinding: FragmentFollowBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: FollowViewModel
    private lateinit var username: String
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(USERNAME).toString()
            type = it.getString(TYPE)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        followBinding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return followBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)

        userAdapter = UserAdapter(arrayListOf()) { user, _ ->
            FancyToast.makeText(
                    context, user, Toast.LENGTH_SHORT, FancyToast.INFO, false
            ).show()
        }

        followBinding.rvFollow.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = userAdapter
        }

        when (type) {
            resources.getString(R.string.followers) -> viewModel.setFollow(username, TypeView.FOLLOWERS)
            resources.getString(R.string.following) -> viewModel.setFollow(username, TypeView.FOLLOWING)
            else -> followError(followBinding, null)
        }
        observeFollow()
    }

    private fun observeFollow() {
        viewModel.dataFollow.observe(viewLifecycleOwner, {
            when (it.state) {
                State.SUCCESS ->
                    if (!it.data.isNullOrEmpty()) {
                        followSuccess(followBinding)
                        userAdapter.run { setData(it.data) }
                    } else {
                        followError(followBinding, resources.getString(R.string.not_have, username, type))
                    }
                State.LOADING -> followLoading(followBinding)
                State.ERROR -> followError(followBinding, it.message)
            }
        })
    }

    override fun followLoading(followBinding: FragmentFollowBinding): Int? {
        followBinding.apply {
            errorLayout.mainNotfound.visibility = gone
            progressBar.start()
            progressBar.loadingColor = R.color.design_default_color_primary
            rvFollow.visibility = gone
        }
        return super.followLoading(followBinding)
    }

    override fun followSuccess(followBinding: FragmentFollowBinding): Int? {
        followBinding.apply {
            errorLayout.mainNotfound.visibility = gone
            progressBar.stop()
            rvFollow.visibility = visible
        }
        return super.followSuccess(followBinding)
    }

    override fun followError(followBinding: FragmentFollowBinding, message: String?): Int? {
        followBinding.apply {
            errorLayout.apply {
                mainNotfound.visibility = visible
                emptyText.text = message ?: resources.getString(R.string.not_found)
            }
            progressBar.stop()
            rvFollow.visibility = gone
        }
        return super.followError(followBinding, message)
    }

}