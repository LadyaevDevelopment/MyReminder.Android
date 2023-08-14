package ldev.myNotifier.presentation.fragments.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ldev.myNotifier.R
import ldev.myNotifier.databinding.TimeLineNotificationBinding
import ldev.myNotifier.domain.entities.TodayNotification
import ldev.myNotifier.domain.entities.TodayNotificationStatus
import ldev.myNotifier.utils.recyclerView.BaseViewHolder
import ldev.myNotifier.utils.recyclerView.ItemFingerprint

class NotificationFingerprint(
    private val onTap: (notification: TodayNotification) -> Unit
) : ItemFingerprint<TimeLineNotificationBinding, TodayNotification> {

    override fun isRelativeItem(item: Any) = item is TodayNotification

    override fun getLayoutId() = R.layout.time_line_notification

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<TimeLineNotificationBinding, TodayNotification> {
        val binding = TimeLineNotificationBinding.inflate(layoutInflater, parent, false)
        return NotificationViewHolder(binding, onTap)
    }

    override fun getDiffUtil(): DiffUtil.ItemCallback<TodayNotification> = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<TodayNotification>() {
        override fun areItemsTheSame(oldItem: TodayNotification, newItem: TodayNotification) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TodayNotification, newItem: TodayNotification) = oldItem == newItem
    }

}

class NotificationViewHolder(
    binding: TimeLineNotificationBinding,
    onTap: (notification: TodayNotification) -> Unit
) : BaseViewHolder<TimeLineNotificationBinding, TodayNotification>(binding) {

    init {
        binding.root.setOnClickListener {
            onTap(item)
        }
    }

    override fun onBind(item: TodayNotification) {
        super.onBind(item)
        with(binding) {
            title.text = item.title
            root.setCardBackgroundColor(when (item.status) {
                TodayNotificationStatus.Completed -> root.context.getColor(R.color.limeGreen)
                TodayNotificationStatus.Postponed -> root.context.getColor(R.color.brightRed)
                TodayNotificationStatus.Pending -> root.context.getColor(R.color.bisque)
            })
            if (item.status == TodayNotificationStatus.Postponed) {
                time.text = item.time.toString()
                time.setTextColor(root.context.getColor(R.color.white))
            } else {
                time.text = item.time.toString()
                time.setTextColor(root.context.getColor(R.color.black))
            }
        }
    }

}