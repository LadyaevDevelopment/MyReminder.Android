package ldev.myNotifier.presentation.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import ldev.myNotifier.R
import ldev.myNotifier.utils.dpToPixels

class CustomMaterialButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    private var topCornerRadius: Float
    private var bottomCornerRadius: Float

    init {
        topCornerRadius = context.dpToPixels(DEFAULT_TOP_CORNER_RADIUS_DP).toFloat()
        bottomCornerRadius = context.dpToPixels(DEFAULT_BOTTOM_CORNER_RADIUS_DP).toFloat()

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomMaterialButton, defStyleAttr, 0)
            topCornerRadius = typedArray.getDimension(R.styleable.CustomMaterialButton_cmb_topCornerRadius, topCornerRadius)
            bottomCornerRadius = typedArray.getDimension(R.styleable.CustomMaterialButton_cmb_bottomCornerRadius, bottomCornerRadius)
            typedArray.recycle()
        }

//        val shapeAppearanceModel = ShapeAppearanceModel()
//            .toBuilder()
//            .setTopLeftCorner(CornerFamily.ROUNDED, topCornerRadius)
//            .setTopRightCorner(CornerFamily.ROUNDED, topCornerRadius)
//            .setBottomLeftCorner(CornerFamily.ROUNDED, bottomCornerRadius)
//            .setBottomRightCorner(CornerFamily.ROUNDED, bottomCornerRadius)
//            .build()
//
//        val materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
//        materialShapeDrawable.fillColor = ColorStateList.valueOf(Color.GREEN)
//
//        println(materialShapeDrawable.canApplyTheme())

//        materialShapeDrawable.fillColor = MaterialColors.getColorStateList(
//            context,
//            com.google.android.material.R.attr.colorPrimary,
//            ColorStateList.valueOf(Color.BLACK)
//        )

//        background = materialShapeDrawable

//        rippleColor = ColorStateList.valueOf(Color.WHITE)

        cornerRadius = 0
        val curveRadius = context.dpToPixels(12).toFloat()
        outlineProvider = object : ViewOutlineProvider() {

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun getOutline(view: View?, outline: Outline?) {
                outline?.setRoundRect(0, 0, view!!.width, view.height, curveRadius)
            }
        }

        clipToOutline = true
    }

    private fun applyThemeAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, com.google.android.material.R.styleable.MaterialButton, defStyleAttr, 0)

        backgroundTintList = typedArray.getColorStateList(com.google.android.material.R.styleable.MaterialButton_backgroundTint)
        rippleColor = typedArray.getColorStateList(com.google.android.material.R.styleable.MaterialButton_rippleColor)
        //setTextColor(typedArray.getColorStateList(com.google.android.material.R.styleable.TextAppearance_android_textColor))

        typedArray.recycle()
    }

    companion object {
        private const val DEFAULT_TOP_CORNER_RADIUS_DP = 3
        private const val DEFAULT_BOTTOM_CORNER_RADIUS_DP = 3
    }
}