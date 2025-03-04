package com.marcos.todoapp.ui.routes

sealed class Routes(val route: String) {
    object ShowLists: Routes("ShowLists")
    object ListTasks: Routes("ListTasks")
}