package com.example.data.datasource.db

import com.example.data.datasource.db.converters.RoomTypeConverter
import com.example.data.datasource.db.dao.TaskDao
import com.example.data.datasource.db.entity.Task
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}