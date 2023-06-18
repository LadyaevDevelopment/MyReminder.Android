package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.button.MaterialButton
import ldev.myNotifier.R
import ldev.myNotifier.databinding.LayoutDayOfWeekBinding
import ldev.myNotifier.utils.addChildView
import ldev.myNotifier.utils.atLeastTwoDigits
import ldev.myNotifier.utils.recyclerView.BaseViewHolder
import ldev.myNotifier.utils.recyclerView.ItemFingerprint
import ldev.myNotifier.utils.removeChildView
import ldev.myNotifier.utils.shortNameResourceId
import java.time.DayOfWeek

class DayOfWeekFingerprint(
    private val onAddButtonTapped: (dayOfWeek: DayOfWeek) -> Unit,
    private val onMarkItem: (item: DayOfWeekModel, isChecked: Boolean) -> Unit,
    private val onRemoveTime: (dayOfWeek: DayOfWeek, time: NotificationTime) -> Unit,
) : ItemFingerprint<LayoutDayOfWeekBinding, DayOfWeekModel> {

    override fun isRelativeItem(item: Any) = item is DayOfWeekModel

    override fun getLayoutId() = R.layout.time_line_notification

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<LayoutDayOfWeekBinding, DayOfWeekModel> {
        val binding = LayoutDayOfWeekBinding.inflate(layoutInflater, parent, false)
        return DayOfWeekViewHolder(binding, onAddButtonTapped, onMarkItem, onRemoveTime)
    }

    override fun getDiffUtil(): DiffUtil.ItemCallback<DayOfWeekModel> = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<DayOfWeekModel>() {
        override fun areItemsTheSame(oldItem: DayOfWeekModel, newItem: DayOfWeekModel) = oldItem.dayOfWeek == newItem.dayOfWeek
        override fun areContentsTheSame(oldItem: DayOfWeekModel, newItem: DayOfWeekModel) = oldItem == newItem
    }

}

class DayOfWeekViewHolder(
    binding: LayoutDayOfWeekBinding,
    private val onAddButtonTapped: (dayOfWeek: DayOfWeek) -> Unit,
    private val onMarkItem: (item: DayOfWeekModel, isChecked: Boolean) -> Unit,
    private val onRemoveTime: (dayOfWeek: DayOfWeek, time: NotificationTime) -> Unit,
) : BaseViewHolder<LayoutDayOfWeekBinding, DayOfWeekModel>(binding) {

    override fun onBind(item: DayOfWeekModel) {
        super.onBind(item)
        with (binding) {
            title.text = binding.root.context.getString(item.dayOfWeek.shortNameResourceId)
            buttons.isGone = true
            check.isChecked = item.isChecked
            check.setOnCheckedChangeListener { _, isChecked ->
                onMarkItem(item, isChecked)
                //viewModel.markDayOfWeek(item.dayOfWeek, isChecked)
            }
            addBtn.setOnClickListener {
                onAddButtonTapped(item.dayOfWeek)
//                showTimePicker { hour, minute ->
//                    viewModel.addTimeToDayOfWeek(
//                        item.dayOfWeek,
//                        EditPeriodicNotificationViewModel.Time(
//                            hour = hour,
//                            minute = minute
//                        )
//                    )
//                }
            }
            buttons.isVisible = item.times.isNotEmpty()
            for (viewId in buttons.referencedIds) {
                buttons.removeChildView(root.findViewById(viewId))
            }
            for (time in item.times) {
                buttons.addChildView(createButton(binding.root.context).apply {
                    text = run { "${time.hour.atLeastTwoDigits()} : ${time.minute.atLeastTwoDigits()}" }
                    setOnClickListener {
                        onRemoveTime(item.dayOfWeek, time)
                        //viewModel.removeTimeFromDayOfWeek(item.dayOfWeek, time)
                    }
                })
            }
        }
    }

    private fun createButton(context: Context): MaterialButton {
        val button = MaterialButton(context)
        button.minWidth = 0
        button.minHeight = 0
        button.minimumWidth = 0
        button.minimumHeight = 0
        button.outlineProvider = null
        button.insetTop = 0
        button.insetBottom = 0
        return button
    }

}