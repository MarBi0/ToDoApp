package com.marcos.todoapp.addtask.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_entity")
data class TaskEntity(
    @PrimaryKey
    val id: Int,
    val task: String,
    var selected: Boolean = false,
)