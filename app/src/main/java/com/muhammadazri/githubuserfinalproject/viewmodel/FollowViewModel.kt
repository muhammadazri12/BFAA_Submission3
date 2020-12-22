package com.muhammadazri.githubuserfinalproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.muhammadazri.githubuserfinalproject.model.GithubUser
import com.muhammadazri.githubuserfinalproject.network.UserRepositories
import com.muhammadazri.githubuserfinalproject.utility.Resource
import com.muhammadazri.githubuserfinalproject.utility.TypeView

class FollowViewModel : ViewModel() {
    private val username: MutableLiveData<String> = MutableLiveData()

    private lateinit var type: TypeView

    val dataFollow: LiveData<Resource<List<GithubUser>>> = Transformations
            .switchMap(username){
                when (type) {
                    TypeView.FOLLOWERS -> {
                        UserRepositories.getFollowers(it)
                    }
                    TypeView.FOLLOWING -> {
                        UserRepositories.getFollowing(it)
                    }
                }
            }

    fun setFollow(user: String, typeView: TypeView) {
        if (username.value == user) {
            return
        }
        username.value = user
        type = typeView
    }
}