package `in`.singhangad.ui_common.listtask.entity

data class TaskListItem(
    val itemId: String,
    val itemType: ItemType,
    val itemTitle: String,
    val itemDescription: String?,
    val itemPinned: Boolean
)

sealed class ItemType {
    object HEADER : ItemType()
    object TASK_ITEM : ItemType()
}