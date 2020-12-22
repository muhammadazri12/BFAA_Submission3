package com.muhammadazri.githubuserfinalproject.network

import android.os.Parcelable
import com.muhammadazri.githubuserfinalproject.model.GithubUser
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchRespond(
    val total_count : String,
    val incomplete_results: Boolean? = null,
    val items : List<GithubUser>
): Parcelable