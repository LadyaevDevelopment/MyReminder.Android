package ldev.myNotifier.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalItemDecorator(
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