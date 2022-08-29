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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher