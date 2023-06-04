package ldev.myNotifier.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.viewpager2.widget.ViewPager2
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