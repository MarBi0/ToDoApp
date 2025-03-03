package com.marcos.todoapp.addtask.data

import com.marcos.todoapp.addtask.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    val tasks: Flow<List<TaskModel>> =
        taskDao.getTasks().map { items -> 
            items.map { TaskModel(it.id, it.task, it.selected, it.order) }
                .sortedBy { it.order }
        }

    suspend fun add(taskModel: TaskModel) {
        // Asignar el orden más alto + 1 para nuevas tareas
        val maxOrder = taskDao.getMaxOrder() ?: 0
        taskDao.addTask(taskModel.copy(order = maxOrder + 1).toData())
    }

    suspend fun update(taskModel: TaskModel) {
        taskDao.updateTask(taskModel.toData())
    }

    suspend fun delete(taskModel: TaskModel) {
        taskDao.deleteTask(taskModel.toData())
    }
    
    suspend fun updateTasksOrder(tasks: List<TaskModel>) {
        tasks.forEachIndexed { index, task ->
            taskDao.updateTask(task.copy(order = index).toData())
        }
    }
}

fun TaskModel.toData(): TaskEntity {
    return TaskEntity(this.id, this.task, this.selected, this.order)
}