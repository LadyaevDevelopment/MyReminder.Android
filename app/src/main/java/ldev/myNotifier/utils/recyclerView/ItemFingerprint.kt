package ldev.myNotifier.utils.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

interface ItemFingerprint<VB : ViewBinding, T : Any> {

    fun isRelativeItem(item: Any): Boolean

    @LayoutRes
    fun getLayoutId(): Int

    fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<VB, T>

    fun getDiffUtil(): DiffUtil.ItemCallback<T>

}