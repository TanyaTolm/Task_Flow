package com.example.task_flow.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.task_flow.R
import com.example.task_flow.TaskFlowApp
import com.example.task_flow.databinding.FragmentLoginBinding
import com.example.task_flow.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        val app = requireActivity().application as TaskFlowApp
        AuthViewModel.Factory(app.authRepository, app.prefs)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as TaskFlowApp
        if (app.prefs.isLoggedIn) {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            return
        }

        binding.btnLogin.setOnClickListener {
            val login = binding.etLogin.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (login.isEmpty()) {
                binding.tilLogin.error = "Введите логин"
                return@setOnClickListener
            } else {
                binding.tilLogin.error = null
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = "Введите пароль"
                return@setOnClickListener
            } else {
                binding.tilPassword.error = null
            }

            viewModel.login(login, password)
        }

        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collectLatest { result ->
                result?.onSuccess {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }?.onFailure {
                    Toast.makeText(requireContext(), it.message ?: "Ошибка входа", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}