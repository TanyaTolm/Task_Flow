package com.example.task_flow.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.task_flow.R
import com.example.task_flow.TaskFlowApp
import com.example.task_flow.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        val app = requireActivity().application as TaskFlowApp

        val savedUsername = app.prefs.username

        if (savedUsername.isNotEmpty()) {
            binding.tvUserName.text = savedUsername
        } else {
            binding.tvUserName.text = "Пользователь"
        }

        binding.btnLogout.setOnClickListener {
            app.prefs.clear()

            val navController = findNavController()
            navController.navigate(R.id.loginFragment) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}