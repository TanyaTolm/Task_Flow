package com.example.task_flow.data.repository

import com.example.task_flow.data.local.FolderDao
import com.example.task_flow.data.model.Folder
import kotlinx.coroutines.flow.Flow

class FolderRepository(private val folderDao: FolderDao) {

    fun getAllFolders(): Flow<List<Folder>> = folderDao.getAll()

    suspend fun insert(folder: Folder): Long = folderDao.insert(folder)

    suspend fun update(folder: Folder) = folderDao.update(folder)

    suspend fun delete(folder: Folder) {
        folderDao.deleteFolder(folder)
    }

    suspend fun getById(id: Long): Folder? = folderDao.getById(id)
}