package `in`.singhangad.shared_data.repository


import `in`.singhangad.shared_data.base.TaskDataSource
import `in`.singhangad.shared_domain.contract.LogService
import `in`.singhangad.shared_domain.contract.TaskRepositoryContract
import `in`.singhangad.shared_domain.entity.Task
import `in`.singhangad.shared_domain.exception.DataNotFoundException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DefaultTaskRepository constructor(
    private val taskDataSource: TaskDataSource,
    private val logService: LogService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
): TaskRepositoryContract {
    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            taskDataSource.getAllTasks()
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.toString(), it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun getTaskById(taskId: String): Result<Task> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val task = taskDataSource.getTaskWithId(taskId)
            task ?: throw DataNotFoundException()
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.toString(), it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun createNewTask(task: Task): Result<Task?> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            taskDataSource.insertTask(task)
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.toString(), it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val task = taskDataSource.getTaskWithId(taskId)
            if (task != null) {
                taskDataSource.removeTaskWithId(taskId)
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.toString(), it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun updateTask(taskId: String, task: Task): Result<Task?> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val savedTask = taskDataSource.getTaskWithId(taskId)
            if (savedTask != null) {
                taskDataSource.updateTask(task)
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.toString(), it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun pinTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val savedTask = taskDataSource.getTaskWithId(taskId)
            if (savedTask != null) {
                taskDataSource.pinTask(taskId)
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.toString(), it)
            Result.failure<Throwable>(it)
        }
    }

    override suspend fun unPinTask(taskId: String): Result<Unit> = withContext(ioDispatcher) {
        return@withContext kotlin.runCatching {
            val savedTask = taskDataSource.getTaskWithId(taskId)
            if (savedTask != null) {
                taskDataSource.unPinTask(taskId)
            } else {
                throw DataNotFoundException()
            }
        }.onSuccess {
            Result.success(it)
        }.onFailure {
            logService.logException(this@DefaultTaskRepository.toString(), it)
            Result.failure<Throwable>(it)
        }
    }
}