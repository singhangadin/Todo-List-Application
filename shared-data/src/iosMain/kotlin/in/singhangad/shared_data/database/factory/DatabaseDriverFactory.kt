package `in`.singhangad.shared_data.database.factory

import `in`.singhangad.shared_data.database.TodoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(TodoDatabase.Schema, "todo_database.db")
    }
}