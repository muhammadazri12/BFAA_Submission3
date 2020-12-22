package com.muhammadazri.githubuserfinalproject.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.muhammadazri.githubuserfinalproject.R
import com.muhammadazri.githubuserfinalproject.databinding.FragmentDetailBinding
import com.muhammadazri.githubuserfinalproject.model.GithubUser
import com.muhammadazri.githubuserfinalproject.utility.State
import com.muhammadazri.githubuserfinalproject.viewmodel.DetailViewModel
import com.shashank.sony.fancytoastlib.FancyToast


class DetailFragment : Fragment() {

    private lateinit var detailBinding: FragmentDetailBinding
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var model: GithubUser
    private val args: DetailFragmentArgs by navArgs()
    private var isFavorite: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel = ViewModelProvider(this)
                .get(DetailViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        detailBinding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        detailBinding.lifecycleOwner = viewLifecycleOwner
        observeDetail()
        return detailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailBinding.detailContent.transitionName = args.Username
        detailBinding.fabFav.setOnClickListener { addOrRemoveFavorite() }

        val tabList = arrayOf(
                resources.getString(R.string.following),
                resources.getString(R.string.following)
        )
        pagerAdapter = PagerAdapter(tabList, args.Username, this)
        detailBinding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(detailBinding.tabs, detailBinding.viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }


    private fun observeDetail() {
        detailViewModel.data(args.Username).observe(viewLifecycleOwner, {
            if (it.state == State.SUCCESS) {
                model = it.data!!
                detailBinding.data = it.data
            }
        })

        detailViewModel.isFavorite.observe(viewLifecycleOwner, { fav ->
            isFavorite = fav
            changeFavorite(fav)
        })
    }

    private fun addOrRemoveFavorite() {
        if (!isFavorite) {
            detailViewModel.addFavorite(model)
            FancyToast.makeText(
                    context, resources.getString(R.string.favorite_add, model.login), Toast.LENGTH_SHORT, FancyToast.SUCCESS, false
            ).show()
        } else {
            detailViewModel.removeFavorite(model)
            FancyToast.makeText(
                    context, resources.getString(R.string.favorite_remove, model.login), Toast.LENGTH_SHORT, FancyToast.ERROR, false
            ).show()
        }
    }

    private fun changeFavorite(condition: Boolean) {
        if (condition) {
            detailBinding.fabFav.setImageResource(R.drawable.ic_favorite)
        } else {
            detailBinding.fabFav.setImageResource(R.drawable.ic_fav_border)
        }
    }

    inner class PagerAdapter(
            private val tabList: Array<String>,
            private val username: String,
            fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = tabList.size

        override fun createFragment(position: Int): Fragment {
            return FollowFragment.newInstance(username, tabList[position])
        }
    }
}