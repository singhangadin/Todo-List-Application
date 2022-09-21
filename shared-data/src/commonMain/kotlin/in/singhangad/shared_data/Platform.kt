package in.singhangad.shared_data

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform