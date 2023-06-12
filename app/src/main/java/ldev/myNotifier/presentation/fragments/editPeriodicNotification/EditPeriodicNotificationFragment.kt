package ldev.myNotifier.presentation.fragments.editPeriodicNotification

import android.R
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import ldev.myNotifier.databinding.FragmentEditPeriodicNotificationBinding
import ldev.myNotifier.databinding.LayoutDayOfWeekBinding
import ldev.myNotifier.utils.BaseFragment
import ldev.myNotifier.utils.addChildView
import ldev.myNotifier.utils.atLeastTwoDigits
import ldev.myNotifier.utils.dpToPixels
import ldev.myNotifier.utils.removeChildView
import java.util.Calendar


class EditPeriodicNotificationFragment : BaseFragment<FragmentEditPeriodicNotificationBinding>() {

    override fun getContentInflater(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditPeriodicNotificationBinding {
        return FragmentEditPeriodicNotificationBinding::inflate
    }

    private var checkedCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.controlButtons.isVisible = false
        binding.checkAll.setOnCheckedChangeListener { _, isChecked ->
            for (dayOfWeekView in binding.daysOfWeek.children) {
                val dayOfWeekBinding = LayoutDayOfWeekBinding.bind(dayOfWeekView)
                dayOfWeekBinding.check.isChecked = isChecked
            }
        }

        binding.addBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
            val initialMinute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    for (dayOfWeekView in binding.daysOfWeek.children) {
                        val dayOfWeekBinding = LayoutDayOfWeekBinding.bind(dayOfWeekView)
                        if (dayOfWeekBinding.check.isChecked) {
                            if (dayOfWeekBinding.buttons.referencedIds.isEmpty()) {
                                dayOfWeekBinding.buttons.isVisible = true
                            }
                            dayOfWeekBinding.buttons.addChildView(createButton().apply {
                                text = run { "${hour.atLeastTwoDigits()} : ${minute.atLeastTwoDigits()}" }
                                setOnClickListener {
                                    dayOfWeekBinding.buttons.removeChildView(this)
                                    if (dayOfWeekBinding.buttons.referencedIds.isEmpty()) {
                                        dayOfWeekBinding.buttons.isGone = true
                                    }
                                }
                            })
                        }
                    }
                },
                initialHour,
                initialMinute,
                DateFormat.is24HourFormat(context)
            )
            timePickerDialog.show()
        }
        binding.removeBtn.setOnClickListener {
            for (dayOfWeekView in binding.daysOfWeek.children) {
                val dayOfWeekBinding = LayoutDayOfWeekBinding.bind(dayOfWeekView)
                if (dayOfWeekBinding.check.isChecked) {
                    val ids: IntArray = dayOfWeekBinding.buttons.referencedIds
                    for (id in ids) {
                        val button: View = dayOfWeekBinding.root.findViewById(id)
                        (dayOfWeekBinding.buttons.parent as ViewGroup).removeView(button)
                    }
                    dayOfWeekBinding.buttons.referencedIds = IntArray(0)
                    dayOfWeekBinding.buttons.isGone = true
                }
            }
        }

        layoutSections("ПН")
        layoutSections("ВТ")
        layoutSections("СР")
        layoutSections("ЧТ")
        layoutSections("ПТ")
        layoutSections("СБ")
        layoutSections("ВС")
    }

    private fun createButton(): MaterialButton {
        val button = MaterialButton(requireContext())
        button.minWidth = 0
        button.minHeight = 0
        button.minimumWidth = 0
        button.minimumHeight = 0
        button.outlineProvider = null
        button.insetTop = 0
        button.insetBottom = 0
        button.text = "button"
        return button
    }

    private fun layoutSections(name: String) {
        val viewBinding = LayoutDayOfWeekBinding.inflate(LayoutInflater.from(requireContext()))
        viewBinding.title.text = name
        viewBinding.buttons.isGone = true
        viewBinding.check.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedCount++
            } else {
                checkedCount--
            }
            binding.controlButtons.isVisible = checkedCount > 0
            if (checkedCount == 0) {
                binding.checkAll.isChecked = false
            } else if (checkedCount == 7) {
                binding.checkAll.isChecked = true
            }
        }
        viewBinding.addBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
            val initialMinute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    viewBinding.buttons.isVisible = true
                    viewBinding.buttons.addChildView(createButton().apply {
                        text = run { "${hour.atLeastTwoDigits()} : ${minute.atLeastTwoDigits()}" }
                        setOnClickListener {
                            viewBinding.buttons.removeChildView(this)
                            if (viewBinding.buttons.referencedIds.isEmpty()) {
                                viewBinding.buttons.isGone = true
                            }
                        }
                    })
                },
                initialHour,
                initialMinute,
                DateFormat.is24HourFormat(context)
            )
            timePickerDialog.show()
        }
        binding.daysOfWeek.addView(viewBinding.root)
        (viewBinding.root.layoutParams as MarginLayoutParams).bottomMargin = dpToPixels(8)
    }

}