package com.muhammadazri.githubuserfinalproject.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammadazri.githubuserfinalproject.R
import com.muhammadazri.githubuserfinalproject.adapter.UserAdapter
import com.muhammadazri.githubuserfinalproject.databinding.FragmentFavoriteBinding
import com.muhammadazri.githubuserfinalproject.utility.ShowStates
import com.muhammadazri.githubuserfinalproject.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment(), ShowStates {
    private lateinit var favoriteBinding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: UserAdapter
    private val viewModel: FavoriteViewModel by navGraphViewModels(R.id.main_nav)


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.favorite)
        favoriteBinding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return favoriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteAdapter = UserAdapter(arrayListOf()) { username, iv ->
            findNavController().navigate(
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(username),
                    FragmentNavigatorExtras(iv to username)
            )
        }

        favoriteBinding.rvFav.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
        }

        observeFavorite()
    }

    private fun observeFavorite() {
        favoriteLoading(favoriteBinding)
        viewModel.dataFavorite.observe(viewLifecycleOwner, {
            it?.let { users ->
                if (!users.isNullOrEmpty()) {
                    favoriteSuccess(favoriteBinding)
                    favoriteAdapter.setData(users)
                } else {
                    favoriteError(
                            favoriteBinding,
                            resources.getString(R.string.not_have, "", resources.getString(R.string.favorite))
                    )
                }
            }
        })
    }

    override fun favoriteLoading(favoriteFragmentBinding: FragmentFavoriteBinding): Int? {
        favoriteBinding.apply {
            errorLayout.mainNotfound.visibility = gone
            progressBar.start()
            progressBar.loadingColor = R.color.design_default_color_primary
            rvFav.visibility = gone
        }
        return super.favoriteLoading(favoriteFragmentBinding)
    }

    override fun favoriteSuccess(favoriteFragmentBinding: FragmentFavoriteBinding): Int? {
        favoriteBinding.apply {
            errorLayout.mainNotfound.visibility = gone
            progressBar.stop()
            rvFav.visibility = visible
        }
        return super.favoriteSuccess(favoriteFragmentBinding)
    }

    override fun favoriteError(favoriteFragmentBinding: FragmentFavoriteBinding, message: String?): Int? {
        favoriteBinding.apply {
            errorLayout.apply {
                mainNotfound.visibility = visible
                emptyText.text = message ?: resources.getString(R.string.not_found)
            }
            progressBar.stop()
            rvFav.visibility = gone
        }
        return super.favoriteError(favoriteFragmentBinding, message)
    }

}