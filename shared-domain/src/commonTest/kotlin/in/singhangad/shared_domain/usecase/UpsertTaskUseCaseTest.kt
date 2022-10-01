package `in`.singhangad.shared_domain.usecase
// TODO: Fix this
//import `in`.singhangad.shared_domain.contract.TaskRepositoryContract
//import `in`.singhangad.shared_domain.entity.Task
//import kotlinx.coroutines.test.runTest
//import kotlinx.datetime.Clock
//import java.util.*
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//
//
//class UpsertTaskUseCaseTest {
//    private lateinit var upsertTaskUseCase: UpsertTaskUseCase
//
////    @Mock
//    private lateinit var taskRepositoryContract: TaskRepositoryContract
//
//    @BeforeTest
//    fun init() {
////        MockitoAnnotations.openMocks(this);
//        upsertTaskUseCase = UpsertTaskUseCase(
//            CreateTaskUseCase(taskRepositoryContract),
//            UpdateTaskUseCase(taskRepositoryContract)
//        )
//    }
//
//    @Test
//    fun testCreate() = runTest {
//        // GIVEN
//        val testTask = Task(
//            null, "Title",
//            "Description", false,
//            Clock.System.now().toEpochMilliseconds(),
//            Clock.System.now().toEpochMilliseconds()
//        )
//
//        // WHEN
//        upsertTaskUseCase.invoke(
//            UpsertTaskUseCase.UseCaseParams(
//                testTask
//            )
//        )
//
//        // THEN
//        // Not testing implementation as they are already tested in other tests
////        verify(taskRepositoryContract).createNewTask(any())
//    }
//
//    @Test
//    fun testUpdate() = runTest {
//        // GIVEN
//        val testTask = Task(
//            "1", "Title",
//            "Description", false,
//            Clock.System.now().toEpochMilliseconds(),
//            Clock.System.now().toEpochMilliseconds()
//        )
//
//        // WHEN
//        upsertTaskUseCase.invoke(
//            UpsertTaskUseCase.UseCaseParams(
//                testTask
//            )
//        )
//
//        // THEN
//        // Not testing implementation as they are already tested in other tests
////        verify(taskRepositoryContract).updateTask(eq(testTask.taskId!!), any())
//    }
//}