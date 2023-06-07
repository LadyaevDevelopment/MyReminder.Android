package ldev.myNotifier.utils.recyclerView

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<out VB : ViewBinding, T : Any>(
    val binding: VB
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var item: T

    open fun onBind(item: T) {
        this.item = item
    }

    open fun onBind(item: T, payloads: List<Any>) {
        this.item = item
    }

    open fun onViewDetached() = Unit

}