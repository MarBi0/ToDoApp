package com.marcos.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcos.todoapp.addtask.ui.TasksScreen
import com.marcos.todoapp.addtask.ui.TasksViewModel
import com.marcos.todoapp.showlisttask.ui.ShowListScreen
import com.marcos.todoapp.ui.routes.Routes
import com.marcos.todoapp.ui.theme.ToDoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val taskViewModel: TasksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navigationController = rememberNavController()
                    NavHost(navController = navigationController, startDestination = Routes.ShowLists.route) {
                        composable(Routes.ListTasks.route) {
                            TasksScreen(
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                taskViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.ShowLists.route) {
                            ShowListScreen(
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                navigationController
                            )
                        }
                    }

                }
            }
        }
    }
}

