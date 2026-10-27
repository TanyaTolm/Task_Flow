package com.example.task_flow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String = "",
    val date: String = "",
    val isCompleted: Boolean = false,
    val folderId: Long? = null
) : java.io.Serializable