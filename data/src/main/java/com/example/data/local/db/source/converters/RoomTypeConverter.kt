package com.example.data.local.db.source.converters

import androidx.room.TypeConverter
import java.util.*

class RoomTypeConverter {

    @TypeConverter
    fun convertDateToLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun convertLongToDate(millis: Long): Date {
        return Date(millis)
    }
}