package com.marcos.todoapp.addtask.domain

import com.marcos.todoapp.addtask.data.TaskRepository
import com.marcos.todoapp.addtask.ui.model.TaskModel
import javax.inject.Inject

class UpdateTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.update(taskModel)
    }

}