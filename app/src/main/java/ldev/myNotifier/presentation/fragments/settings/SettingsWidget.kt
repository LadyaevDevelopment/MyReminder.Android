package ldev.myNotifier.presentation.fragments.settings

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ldev.myNotifier.R
import ldev.myNotifier.databinding.LayoutSettingsWidgetBinding

class SettingsWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: LayoutSettingsWidgetBinding

    init {
        binding = LayoutSettingsWidgetBinding.inflate(LayoutInflater.from(context), this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingsWidget)

        val iconDrawable = typedArray.getDrawable(R.styleable.SettingsWidget_settingsWidget_icon)
        binding.icon.setImageDrawable(iconDrawable)

        val titleText = typedArray.getString(R.styleable.SettingsWidget_settingsWidget_title)
        binding.title.text = titleText

        val bgColor = typedArray.getColor(R.styleable.SettingsWidget_settingsWidget_bgColor, Color.BLACK)
        binding.root.setCardBackgroundColor(bgColor)
    }
}