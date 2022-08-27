package com.example.data.local.db.source

import com.example.data.local.db.source.converters.RoomTypeConverter
import com.example.data.local.db.source.dao.TaskDao
import com.example.data.local.db.source.entity.Task
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}