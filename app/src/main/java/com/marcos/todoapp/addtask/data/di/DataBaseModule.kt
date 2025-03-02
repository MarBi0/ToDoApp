package com.marcos.todoapp.addtask.data.di

import android.content.Context
import androidx.room.Room
import com.marcos.todoapp.addtask.data.TaskDao
import com.marcos.todoapp.addtask.data.ToDoDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    fun provideTaskDao(todoDatabase: ToDoDataBase): TaskDao {
        return todoDatabase.taskDao()
    }

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): ToDoDataBase {
        return Room.databaseBuilder(appContext, ToDoDataBase::class.java, "TaskDatabase").build()

    }
}