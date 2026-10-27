package com.example.task_flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.task_flow.data.repository.AuthRepository
import com.example.task_flow.util.PreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val prefs: PreferenceHelper
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Unit>?>(null)
    val loginState: StateFlow<Result<Unit>?> = _loginState

    fun login(login: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(login, password)

            result.onSuccess { user ->
                prefs.isLoggedIn = true
                prefs.username = user.username
                prefs.userId = user.id
                _loginState.value = Result.success(Unit)
            }.onFailure {
                _loginState.value = Result.failure(it)
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.register(username, email, password)

            result.onSuccess { userId ->
                prefs.isLoggedIn = true
                prefs.username = username
                prefs.userId = userId
                _loginState.value = Result.success(Unit)
            }.onFailure {
                _loginState.value = Result.failure(it)
            }
        }
    }

    class Factory(
        private val authRepository: AuthRepository,
        private val prefs: PreferenceHelper
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(authRepository, prefs) as T
        }
    }
}