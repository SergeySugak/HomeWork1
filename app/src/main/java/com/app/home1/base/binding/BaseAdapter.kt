package com.app.home1.base.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseAdapter: RecyclerView.Adapter<BaseAdapter.ViewHolder>() {

    protected val items = LinkedList<Any>()
    private val itemsMap = HashMap<Class<*>, ItemInfo>()

    fun addTypedItem(clazz: Class<*>, @LayoutRes layoutId: Int, bindingId: Int) {
        itemsMap[clazz] = ItemInfo(layoutId, bindingId)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun getItemInfo(data: Any): ItemInfo {
        itemsMap.entries
            .filter { it.key == data.javaClass }
            .first { return it.value }

        throw Exception("Cell info for class ${data.javaClass.name} not found.")
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return getItemInfo(items[position]).layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemInfo = getItemInfo(items[position])
        if (itemInfo.bindingId != 0)
            holder.binding?.setVariable(itemInfo.bindingId, items[position])
    }

    data class ItemInfo(val layoutId: Int, val bindingId: Int)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ViewDataBinding>(view)
    }
}