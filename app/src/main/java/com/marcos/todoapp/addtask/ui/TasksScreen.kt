package com.marcos.todoapp.addtask.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.marcos.todoapp.addtask.ui.model.TaskModel
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TasksScreen(
    modifier: Modifier,
    taskViewModel: TasksViewModel,
    navigationController: NavHostController
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<TaskUiState>(
        initialValue = TaskUiState.Loading,
        key1 = lifecycle,
        key2 = taskViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            taskViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is TaskUiState.Error -> {

        }

        TaskUiState.Loading -> {
            CircularProgressIndicator()
        }

        is TaskUiState.Success -> {
            Scaffold(modifier = modifier, topBar = { TopBar(navController = navigationController) }) {
                Column(modifier = modifier.fillMaxSize()) {
                    TaskList((uiState as TaskUiState.Success).tasks, taskViewModel)
                }
            }
        }
    }
}

@Composable
fun AddTask(taskViewModel: TasksViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable {
            taskViewModel.onTaskCreate("")
        }) {
        Spacer(
            modifier = Modifier
                .size(36.dp)
        )
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Task",
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "Elemento de lista",
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun TopBar(navController: NavHostController) {
    Row {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Ir atras",
            modifier = Modifier
                .padding(16.dp)
                .clickable { navController.popBackStack() }
        )
    }
}

@Composable
fun TaskList(tasks: List<TaskModel>, taskViewModel: TasksViewModel) {
    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            taskViewModel.onTaskReordered(from.index - 1 , to.index - 1)
        })

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .fillMaxWidth()
            .reorderable(state)
    ) {
        item {
            TextField(
                readOnly = false,
                value = "",
                onValueChange = { },
                placeholder = { Text("Título", color = Color.Gray, fontSize = 24.sp) },
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                textStyle = TextStyle(MaterialTheme.colorScheme.onBackground, 24.sp),
                modifier = Modifier
                    .padding(horizontal = 8.dp),
            )
        }

        items(
            items = tasks,
            key = { it.id }
        ) { task ->
            ReorderableItem(
                state,
                key = task.id,
                defaultDraggingModifier = Modifier
            ) { isDragging ->
                ItemTask(task, taskViewModel, state)
            }
        }

        item { AddTask(taskViewModel) }
    }
}

@Composable
fun ItemTask(
    taskModel: TaskModel,
    taskViewModel: TasksViewModel,
    state: ReorderableLazyListState,
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = "Arrastrar",
                modifier = Modifier
                    .padding(8.dp)
                    .detectReorder(state)
            )
            Checkbox(
                checked = taskModel.selected,
                onCheckedChange = { taskViewModel.onCheckBoxSelected(taskModel) })
            TextField(
                readOnly = taskModel.selected,
                value = taskModel.task,
                onValueChange = { },
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                textStyle = TextStyle(MaterialTheme.colorScheme.onBackground, 16.sp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            )
        }
    }
}
