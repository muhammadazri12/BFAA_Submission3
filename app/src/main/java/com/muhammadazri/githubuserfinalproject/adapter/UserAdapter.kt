package com.muhammadazri.githubuserfinalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadazri.githubuserfinalproject.databinding.ItemUserRowBinding
import com.muhammadazri.githubuserfinalproject.model.GithubUser


class UserAdapter(private val githubUser: ArrayList<GithubUser>, private val clickListener: (String, View) -> Unit) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    fun setData(items: List<GithubUser>){
        githubUser.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserRowBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(githubUser[position], clickListener)

    override fun getItemCount(): Int = githubUser.size

    inner class ViewHolder(private val binding: ItemUserRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(githubUser: GithubUser, click: (String, View) -> Unit) {
            binding.data = githubUser
            binding.root.transitionName = githubUser.login
            binding.root.setOnClickListener { click(githubUser.login, binding.root) }
        }
    }

}