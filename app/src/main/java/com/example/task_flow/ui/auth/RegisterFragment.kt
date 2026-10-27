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
import com.example.task_flow.databinding.FragmentRegisterBinding
import com.example.task_flow.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (username.isEmpty()) {
                binding.tilUsername.error = "Введите имя пользователя"
                return@setOnClickListener
            } else {
                binding.tilUsername.error = null
            }

            if (password.length < 6) {
                binding.tilPassword.error = "Пароль должен быть не короче 6 символов"
                return@setOnClickListener
            } else {
                binding.tilPassword.error = null
            }

            if (password != confirmPassword) {
                binding.tilConfirmPassword.error = "Пароли не совпадают"
                return@setOnClickListener
            } else {
                binding.tilConfirmPassword.error = null
            }

            val generatedEmail = "${username}@taskflow.com"
            viewModel.register(username, generatedEmail, password)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collectLatest { result ->
                result?.onSuccess {
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }?.onFailure {
                    Toast.makeText(requireContext(), it.message ?: "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}