package com.muhammadazri.githubuserfinalproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.muhammadazri.githubuserfinalproject.db.UserDao
import com.muhammadazri.githubuserfinalproject.db.UserDatabase
import com.muhammadazri.githubuserfinalproject.model.GithubUser
import com.muhammadazri.githubuserfinalproject.network.UserFavoriteRepository
import com.muhammadazri.githubuserfinalproject.utility.Resource
import kotlinx.coroutines.launch

class DetailViewModel(app: Application) : AndroidViewModel(app) {

    private var userDao : UserDao = UserDatabase.getDatabase(app).userDao()
    private var userFavoriteRepos: UserFavoriteRepository

    init {
        userFavoriteRepos = UserFavoriteRepository(userDao)
    }

    fun data(username: String) : LiveData<Resource<GithubUser>> = userFavoriteRepos.getDetailUser(username)

    fun addFavorite(githubUser: GithubUser) = viewModelScope.launch {
        userFavoriteRepos.insert(githubUser)
    }

    fun removeFavorite(githubUser: GithubUser)= viewModelScope.launch {
        userFavoriteRepos.delete(githubUser)
    }

    val isFavorite: LiveData<Boolean> = userFavoriteRepos.isFavorite


}