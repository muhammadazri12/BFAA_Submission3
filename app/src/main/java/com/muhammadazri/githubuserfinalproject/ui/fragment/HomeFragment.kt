package com.muhammadazri.githubuserfinalproject.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammadazri.githubuserfinalproject.R
import com.muhammadazri.githubuserfinalproject.adapter.UserAdapter
import com.muhammadazri.githubuserfinalproject.databinding.FragmentHomeBinding
import com.muhammadazri.githubuserfinalproject.utility.ShowStates
import com.muhammadazri.githubuserfinalproject.utility.State
import com.muhammadazri.githubuserfinalproject.viewmodel.HomeViewModel


class HomeFragment : Fragment(), ShowStates {

    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var homeAdapter: UserAdapter
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.main_nav)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        homeBinding.errorLayout.emptyText.text = resources.getString(R.string.search_hint)

        homeAdapter = UserAdapter(arrayListOf()) { username, iv ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeDestinationToDetailFragment(username),
                FragmentNavigatorExtras(
                    iv to username
                )
            )
        }

        homeBinding.rvHome.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

        homeBinding.searchView.apply {
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    homeViewModel.setSearch(query)
                    homeBinding.searchView.clearFocus()
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean = false
            })
        }
        observeHome()
    }

    private fun observeHome() {
        homeViewModel.searchResult.observe(viewLifecycleOwner,  {
            it?.let { resources ->
                when (resources.state) {
                    State.SUCCESS -> {
                        resources.data?.let { user ->
                            if (!user.isNullOrEmpty()) {
                                homeSuccess(homeBinding)
                                Log.d("homeBinding","Success")
                            } else {
                                homeError(homeBinding, null)
                                Log.d("homeBinding","Failed")
                            }
                        }
                    }
                    State.LOADING -> homeLoading(homeBinding)
                    State.ERROR -> homeError(homeBinding, it.message)
                }
            }
        })
    }

    override fun homeLoading(homeBinding: FragmentHomeBinding): Int? {
        homeBinding.apply {
            errorLayout.mainNotfound.visibility = gone
            rotateLoading.start()
            rotateLoading.loadingColor = R.color.design_default_color_on_primary
            rvHome.visibility = gone
        }
        return super.homeLoading(homeBinding)
    }

    override fun homeSuccess(homeBinding: FragmentHomeBinding): Int? {
        homeBinding.apply {
            errorLayout.mainNotfound.visibility = gone
            rotateLoading.stop()
            rvHome.visibility = visible
        }
        return super.homeSuccess(homeBinding)
    }

    override fun homeError(homeBinding: FragmentHomeBinding, message: String?): Int? {
        homeBinding.apply {
            errorLayout.apply {
                mainNotfound.visibility = visible
                emptyText.text = message ?: resources.getString(R.string.not_found)
            }
            rotateLoading.stop()
            rvHome.visibility = gone
        }
        return super.homeError(homeBinding, message)
    }
}
