package ldev.myNotifier.presentation.fragments.today

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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

class NotificationItemDecorator(
    private val innerDivider: Int,
    private val outerDivider: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val viewHolder = parent.getChildViewHolder(view)
        val itemCount = parent.adapter?.itemCount ?: return
        val adapterLastIndex = itemCount - 1
        val itemPosition = viewHolder.absoluteAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: viewHolder.oldPosition

        val oneSizeDivider = innerDivider / 2

        outRect.top = if (itemPosition == 0) outerDivider else oneSizeDivider
        outRect.bottom = if (itemPosition == adapterLastIndex) outerDivider else oneSizeDivider
    }

}