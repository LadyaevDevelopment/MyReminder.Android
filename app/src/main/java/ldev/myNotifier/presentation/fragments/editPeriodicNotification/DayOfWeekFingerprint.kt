package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
    private val onMarkItem: (dayOfWeek: DayOfWeek, isChecked: Boolean) -> Unit,
    private val onRemoveTime: (dayOfWeek: DayOfWeek, time: NotificationTimeModel) -> Unit,
) : ItemFingerprint<LayoutDayOfWeekBinding, DayOfWeekModel> {

    override fun isRelativeItem(item: Any) = item is DayOfWeekModel

    override fun getLayoutId() = R.layout.layout_day_of_week

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
        override fun getChangePayload(oldItem: DayOfWeekModel, newItem: DayOfWeekModel): Any? {
            if (oldItem.times != newItem.times) return NeedToChangeButtonFlow
            if (oldItem.isChecked != newItem.isChecked) return NeedToChangeChecked
            return super.getChangePayload(oldItem, newItem)
        }
    }

}

object NeedToChangeButtonFlow
object NeedToChangeChecked

class DayOfWeekViewHolder(
    binding: LayoutDayOfWeekBinding,
    private val onAddButtonTapped: (dayOfWeek: DayOfWeek) -> Unit,
    private val onMarkItem: (dayOfWeek: DayOfWeek, isChecked: Boolean) -> Unit,
    private val onRemoveTime: (dayOfWeek: DayOfWeek, time: NotificationTimeModel) -> Unit,
) : BaseViewHolder<LayoutDayOfWeekBinding, DayOfWeekModel>(binding) {

    init {
        binding.check.setOnCheckedChangeListener { _, isChecked ->
            if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnCheckedChangeListener
            onMarkItem(item.dayOfWeek, isChecked)
        }
        binding.addBtn.setOnClickListener {
            if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
            onAddButtonTapped(item.dayOfWeek)
        }
    }

    override fun onBind(item: DayOfWeekModel) {
        super.onBind(item)
        with (binding) {
            title.text = binding.root.context.getString(item.dayOfWeek.shortNameResourceId)
            setupButtonFlow()
            setChecked()
        }
    }

    override fun onBind(item: DayOfWeekModel, payloads: List<Any>) {
        super.onBind(item)
        if (payloads.contains(NeedToChangeButtonFlow)) {
            setupButtonFlow()
        }
        if (payloads.contains(NeedToChangeChecked)) {
            setChecked()
        }
    }

    private fun setupButtonFlow() {
        with (binding) {
            if (item.times.isEmpty()) {
                buttons.isGone = true
            } else {
                buttons.isVisible = true
            }
            for (viewId in buttons.referencedIds) {
                buttons.removeChildView(root.findViewById(viewId))
            }
            for (time in item.times.sortedBy { it.time }) {
                buttons.addChildView(createButton(binding.root.context).apply {
                    text = run { "${time.time.hour.atLeastTwoDigits()} : ${time.time.minute.atLeastTwoDigits()}" }
                    setOnClickListener {
                        onRemoveTime(item.dayOfWeek, time)
                    }
                })
            }
        }
    }

    private fun setChecked() {
        binding.check.isChecked = item.isChecked
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