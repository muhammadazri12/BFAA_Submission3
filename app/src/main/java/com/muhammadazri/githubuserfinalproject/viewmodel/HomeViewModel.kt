package com.muhammadazri.githubuserfinalproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.muhammadazri.githubuserfinalproject.model.GithubUser
import com.muhammadazri.githubuserfinalproject.network.UserRepositories
import com.muhammadazri.githubuserfinalproject.utility.Resource

class HomeViewModel : ViewModel() {

    private val username: MutableLiveData<String> = MutableLiveData()

    val searchResult: LiveData<Resource<List<GithubUser>>> = Transformations
            .switchMap(username) {
                UserRepositories.searchUsers(it)
            }

    fun setSearch(query: String) {
        if (username.value == query) {
            return
        }
        username.value = query
    }
}