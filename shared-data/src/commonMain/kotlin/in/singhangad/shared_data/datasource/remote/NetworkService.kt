package `in`.singhangad.shared_data.datasource.remote

import `in`.singhangad.shared_data.httpClient
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = true
    isLenient = true
}

val httpClient = httpClient {
    install(Auth) {
        bearer {
            loadTokens {
                BearerTokens(accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxLGFuZ2FkczI1QGdtYWlsLmNvbSIsImlzcyI6IkNvZGVKYXZhIiwiZXhwIjoxNjY0NzgwMDg5LCJpYXQiOjE2NjQ2OTM2ODl9.nF49UMVWyqP7_B3V73_otSxqjsqxCJC5Z0KkEGCBOWI0Xs-ZAHl2kg8qJR8WYxFBwxjBGaeDc0G5PvbPLKdvJg", "")
            }
        }
    }

    install(ContentNegotiation) {
        json(json)
    }

    install(Logging) {
        level = LogLevel.ALL
    }
}