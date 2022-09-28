package `in`.singhangad.shared_data.datasource.database.factory

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {

    fun createDriver(): SqlDriver
}