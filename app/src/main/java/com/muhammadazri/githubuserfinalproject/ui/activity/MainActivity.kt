package com.muhammadazri.githubuserfinalproject.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.muhammadazri.githubuserfinalproject.R
import com.muhammadazri.githubuserfinalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navControler: NavController
    private lateinit var appbarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navControler = findNavController(R.id.nav_host)
        NavigationUI.setupWithNavController(binding.bottomNavView, navControler)

        appbarConfiguration = AppBarConfiguration(navControler.graph)
        navControler.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){
                R.id.detail_nav -> binding.bottomNavView.visibility = GONE
                else -> binding.bottomNavView.visibility = View.VISIBLE
            }
        }
        setupActionBarWithNavController(navControler, appbarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        navControler.navigateUp(appbarConfiguration)
        return super.onSupportNavigateUp()
    }
}
