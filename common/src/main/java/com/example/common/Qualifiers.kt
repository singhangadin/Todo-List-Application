package com.example.common

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DBDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InMemoryDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FileDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CacheRepository