package com.example.domain.di

import com.example.common.DefaultRepository
import com.example.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory { CreateTaskUseCase(get(qualifier = DefaultRepository())) }
    factory { DeleteTaskUseCase(get(qualifier = DefaultRepository())) }
    factory { GetDateSortedTaskUseCase(get(qualifier = DefaultRepository())) }
    factory { GetTaskUseCase(get(qualifier = DefaultRepository())) }
    factory { PinTaskUseCase(get(qualifier = DefaultRepository())) }
    factory { UnPinTaskUseCase(get(qualifier = DefaultRepository())) }
    factory { UpdateTaskUseCase(get(qualifier = DefaultRepository())) }
    factory { UpsertTaskUseCase(get(), get()) }
}