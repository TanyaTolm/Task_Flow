package com.example.task_flow.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.task_flow.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks ORDER BY date ASC")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE folderId = :folderId ORDER BY date ASC")
    fun getByFolder(folderId: Long): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Long): Task?

    @Query("SELECT * FROM tasks WHERE folderId = :folderId ORDER BY id DESC")
    fun getTasksByFolder(folderId: Long): Flow<List<Task>>
}