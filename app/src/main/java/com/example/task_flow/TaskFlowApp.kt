package com.example.task_flow

import android.app.Application
import com.example.task_flow.data.local.AppDatabase
import com.example.task_flow.data.repository.AuthRepository
import com.example.task_flow.data.repository.FolderRepository
import com.example.task_flow.data.repository.TaskRepository
import com.example.task_flow.util.PreferenceHelper

class TaskFlowApp : Application() {

    val database by lazy { AppDatabase.getInstance(this) }

    val authRepository by lazy { AuthRepository(database.userDao()) }
    val folderRepository by lazy { FolderRepository(database.folderDao()) }
    val taskRepository by lazy { TaskRepository(database.taskDao()) }

    val prefs by lazy { PreferenceHelper(this) }
}