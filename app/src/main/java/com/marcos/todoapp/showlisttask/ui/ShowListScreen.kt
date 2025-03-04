package com.marcos.todoapp.showlisttask.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.marcos.todoapp.ui.routes.Routes


@Composable
fun ShowListScreen(modifier: Modifier, navigationController: NavHostController) {
    Box(modifier = modifier.fillMaxSize()) {
        FabDialog(modifier = Modifier.align(Alignment.BottomEnd), navigationController = navigationController )
    }
}

@Composable
fun FabDialog(modifier: Modifier, navigationController: NavHostController) {
    FloatingActionButton(
        onClick = { navigationController.navigate(Routes.ListTasks.route) }, modifier = modifier
            .padding(16.dp)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add")
    }
}