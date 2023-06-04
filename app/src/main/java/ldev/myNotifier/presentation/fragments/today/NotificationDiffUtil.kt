package ldev.myNotifier.presentation.fragments.today

import androidx.recyclerview.widget.DiffUtil
import ldev.myNotifier.domain.entities.Notification

class NotificationDiffUtil(
    private val oldItems: List<Notification>,
    private val newItems: List<Notification>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

}