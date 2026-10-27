package com.example.task_flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.task_flow.data.model.Folder
import com.example.task_flow.data.model.Task
import com.example.task_flow.data.repository.FolderRepository
import com.example.task_flow.data.repository.TaskRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData

class TaskViewModel(
    private val repository: TaskRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    val allTasks: LiveData<List<Task>> = repository.getAllTasks().asLiveData()

    val allFolders: LiveData<List<Folder>> = folderRepository.getAllFolders().asLiveData()

    fun addTask(task: Task) {
        viewModelScope.launch { repository.insert(task) }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch { repository.update(task) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { repository.delete(task) }
    }

    fun toggleCompleted(task: Task) {
        viewModelScope.launch { repository.update(task.copy(isCompleted = !task.isCompleted)) }
    }

    fun addFolder(folder: Folder) {
        viewModelScope.launch { folderRepository.insert(folder) }
    }

    fun getTasksByFolder(folderId: Long): LiveData<List<Task>> {
        return repository.getTasksByFolder(folderId).asLiveData()
    }

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            folderRepository.delete(folder)
        }
    }

    class Factory(
        private val repository: TaskRepository,
        private val folderRepository: FolderRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(repository, folderRepository) as T
        }
    }
}