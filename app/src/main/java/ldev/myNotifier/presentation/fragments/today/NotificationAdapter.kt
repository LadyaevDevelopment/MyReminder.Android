package ldev.myNotifier.presentation.fragments.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ldev.myNotifier.databinding.TimeLineNotificationBinding
import ldev.myNotifier.domain.entities.Notification

class NotificationAdapter : RecyclerView.Adapter<NotificationViewHolder>() {

    private val items: MutableList<Notification> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            TimeLineNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(newItems: List<Notification>) {
        val newList = newItems.toList()
        val diffUtil = NotificationDiffUtil(items, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

}

class NotificationViewHolder(
    private val viewBinding: TimeLineNotificationBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: Notification) {
        viewBinding.title.text = item.title
    }

}