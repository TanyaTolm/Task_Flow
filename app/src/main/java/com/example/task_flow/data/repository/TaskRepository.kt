package com.example.task_flow.data.repository

import com.example.task_flow.data.local.TaskDao
import com.example.task_flow.data.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAll()

    fun getTasksByFolder(folderId: Long): Flow<List<Task>> {
        return taskDao.getTasksByFolder(folderId)
    }
    suspend fun insert(task: Task): Long = taskDao.insert(task)

    suspend fun update(task: Task) = taskDao.update(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun getById(id: Long): Task? = taskDao.getById(id)


}