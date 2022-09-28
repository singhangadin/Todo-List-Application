package `in`.singhangad.shared_data.datasource.database.factory

import `in`.singhangad.shared_data.database.TodoDatabase
import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(TodoDatabase.Schema, context, "todo_database.db")
    }
}