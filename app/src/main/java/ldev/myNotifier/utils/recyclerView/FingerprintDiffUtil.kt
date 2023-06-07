package ldev.myNotifier.utils.recyclerView

import androidx.recyclerview.widget.DiffUtil

class FingerprintDiffUtil(
    private val fingerprints: List<ItemFingerprint<*, *>>,
) : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return getItemCallback(oldItem).areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return getItemCallback(oldItem).areContentsTheSame(oldItem, newItem)
    }

    override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
        if (oldItem::class != newItem::class) {
            return false
        }
        return getItemCallback(oldItem).getChangePayload(oldItem, newItem)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getItemCallback(
        item: Any
    ): DiffUtil.ItemCallback<Any> {
        return fingerprints.find { it.isRelativeItem(item) }
            ?.getDiffUtil()
            ?.let { it as DiffUtil.ItemCallback<Any> }
            ?: throw IllegalStateException("DiffUtil not found for $item")
    }

}