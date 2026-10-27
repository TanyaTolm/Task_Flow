package com.example.task_flow.data.repository

import com.example.task_flow.data.local.UserDao
import com.example.task_flow.data.model.User

class AuthRepository(private val userDao: UserDao) {

    suspend fun register(username: String, email: String, password: String): Result<Long> {
        return try {
            if (userDao.getByUsername(username) != null) {
                Result.failure(Exception("Пользователь с таким именем уже существует"))
            } else {
                val id = userDao.insert(
                    User(username = username, email = email, password = password)
                )
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(username: String, password: String): Result<User> {
        return try {
            val user = userDao.login(username, password)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Неверный логин или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(id: Long): User? {
        return userDao.getById(id)
    }
}