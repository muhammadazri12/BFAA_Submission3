package com.muhammadazri.githubuserfinalproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.muhammadazri.githubuserfinalproject.db.UserDatabase
import com.muhammadazri.githubuserfinalproject.model.GithubUser
import com.muhammadazri.githubuserfinalproject.network.UserRepositories

class FavoriteViewModel(app: Application) : AndroidViewModel(app) {
    val dataFavorite: LiveData<List<GithubUser>>

    init {
        val userDao = UserDatabase.getDatabase(app).userDao()
        dataFavorite = UserRepositories.getFavorite(userDao)
    }
}