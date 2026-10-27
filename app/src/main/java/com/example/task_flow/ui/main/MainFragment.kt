package com.example.task_flow.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.ui.setupWithNavController
import com.example.task_flow.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.main_nav_host) as? androidx.navigation.fragment.NavHostFragment
        val navController = navHostFragment?.navController

        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottom_nav)

        if (navController != null) {
            bottomNav?.setupWithNavController(navController)
        }
    }
}