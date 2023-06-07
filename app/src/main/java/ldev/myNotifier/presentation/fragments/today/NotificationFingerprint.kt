package ldev.myNotifier.presentation.fragments.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ldev.myNotifier.R
import ldev.myNotifier.databinding.TimeLineNotificationBinding
import ldev.myNotifier.domain.entities.Notification
import ldev.myNotifier.utils.recyclerView.BaseViewHolder
import ldev.myNotifier.utils.recyclerView.ItemFingerprint

class NotificationFingerprint : ItemFingerprint<TimeLineNotificationBinding, Notification> {

    override fun isRelativeItem(item: Any) = item is Notification

    override fun getLayoutId() = R.layout.time_line_notification

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<TimeLineNotificationBinding, Notification> {
        val binding = TimeLineNotificationBinding.inflate(layoutInflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun getDiffUtil(): DiffUtil.ItemCallback<Notification> = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Notification, newItem: Notification) = oldItem == newItem
    }

}

class NotificationViewHolder(
    binding: TimeLineNotificationBinding
) : BaseViewHolder<TimeLineNotificationBinding, Notification>(binding) {

    override fun onBind(item: Notification) {
        super.onBind(item)
        with(binding) {
            title.text = item.text
        }
    }

}