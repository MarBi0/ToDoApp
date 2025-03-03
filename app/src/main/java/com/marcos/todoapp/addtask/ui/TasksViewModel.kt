package com.marcos.todoapp.addtask.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcos.todoapp.addtask.data.TaskRepository
import com.marcos.todoapp.addtask.domain.AddTasksUseCase
import com.marcos.todoapp.addtask.domain.DeleteTasksUseCase
import com.marcos.todoapp.addtask.domain.GetTasksUseCase
import com.marcos.todoapp.addtask.domain.UpdateTasksUseCase
import com.marcos.todoapp.addtask.ui.TaskUiState.*
import com.marcos.todoapp.addtask.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTasksUseCase: AddTasksUseCase,
    private val updateTasksUseCase: UpdateTasksUseCase,
    private val deleteTasksUseCase: DeleteTasksUseCase,
    getTasksUseCase: GetTasksUseCase,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _taskList = MutableStateFlow<List<TaskModel>>(emptyList())
    
    val uiState: StateFlow<TaskUiState> =
        getTasksUseCase().map { tasks ->
            _taskList.value = tasks
            Success(tasks)
        }
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog


    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreate(task: String) {
        _showDialog.value = false

        viewModelScope.launch {
            addTasksUseCase(TaskModel(task = task))
        }
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        viewModelScope.launch {
            updateTasksUseCase(taskModel.copy(selected = !taskModel.selected))
        }
    }

    fun onItemRemove(taskModel: TaskModel) {
        viewModelScope.launch {
            deleteTasksUseCase(taskModel)
        }
    }
    
    fun onTaskReordered(fromIndex: Int, toIndex: Int) {
        val currentList = _taskList.value.toMutableList()
        val movedItem = currentList.removeAt(fromIndex)
        currentList.add(toIndex, movedItem)
        
        // Actualizar la lista en la UI inmediatamente
        _taskList.update { currentList }
        
        // Persistir el nuevo orden en la base de datos
        viewModelScope.launch {
            taskRepository.updateTasksOrder(currentList)
        }
    }
}