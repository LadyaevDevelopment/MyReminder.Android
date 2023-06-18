package ldev.myNotifier.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewParent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import ldev.myNotifier.R
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.Date
import java.util.Locale
import kotlin.math.abs

fun <K, V> Map<K, V>.reversed() = HashMap<V, K>().also { newMap ->
    entries.forEach { newMap[it.value] = it.key }
}

fun ViewPager2.smoothScrollTo(newPosition: Int, duration: Long = 75) {
    val numberOfPages = abs(newPosition - currentItem)
    if (numberOfPages == 0) {
        return
    }
    val leftToRight = currentItem < newPosition
    val pxToDrag: Int = width
    val animator = ValueAnimator.ofInt(0, pxToDrag)
    var previousValue = 0

    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        var currentPxToDrag: Float = (currentValue - previousValue).toFloat() * numberOfPages
        if (leftToRight) {
            currentPxToDrag *= -1
        }
        fakeDragBy(currentPxToDrag)
        previousValue = currentValue
    }
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) { beginFakeDrag() }
        override fun onAnimationEnd(animation: Animator) { endFakeDrag() }
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.duration = duration
    animator.start()
}

val Fragment.ctx get() = requireContext()

fun Fragment.dpToPixels(dp: Int): Int {
    return ctx.dpToPixels(dp)
}

fun Context.dpToPixels(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Date.formatAsHoursMinutes(): String {
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(this)
}

fun Date.formatAsFullDayFullMonthFullYear(locale: Locale = Locale("ru")): String {
    val format = SimpleDateFormat("dd MMMM yyyy", locale)
    return format.format(this)
}

fun ViewPager2.reduceDragSensitivity(factor: Int) {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop * factor)
}

var ViewPager2.innerOverScrollMode: Int
    get() = (getChildAt(0) as RecyclerView).overScrollMode
    set(value) {
        (getChildAt(0) as RecyclerView).overScrollMode = value
    }

fun Number.atLeastTwoDigits(): String {
    return String.format("%02d", this)
}

private typealias FlowLayout = androidx.constraintlayout.helper.widget.Flow

fun FlowLayout.addChildView(view: View) {
    view.id = View.generateViewId()
    var parentCL: ViewParent? = parent
    while (parentCL != null && parentCL !is ConstraintLayout) {
        parentCL = parent
    }
    if (parentCL == null) throw Exception("Cannot find ConstraintLayout widget")
    val cl = parentCL as ConstraintLayout

    cl.addView(view)
    addView(view)
}

fun FlowLayout.removeChildView(view: View) {
    var parentCL: ViewParent? = parent
    while (parentCL != null && parentCL !is ConstraintLayout) {
        parentCL = parent
    }
    if (parentCL == null) throw Exception("Cannot find ConstraintLayout widget")
    val cl = parentCL as ConstraintLayout

    cl.removeView(view)
    removeView(view)
}

@get:StringRes
val DayOfWeek.shortNameResourceId: Int get() {
    return when (this) {
        DayOfWeek.MONDAY -> R.string.short_monday
        DayOfWeek.TUESDAY -> R.string.short_tuesday
        DayOfWeek.WEDNESDAY -> R.string.short_wednesday
        DayOfWeek.THURSDAY -> R.string.short_thursday
        DayOfWeek.FRIDAY -> R.string.short_friday
        DayOfWeek.SATURDAY -> R.string.short_saturday
        DayOfWeek.SUNDAY -> R.string.short_sunday
    }
}