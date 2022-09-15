package com.psq.arch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.psq.arch.model.EmptyWrapper
import com.psq.architecture.BR
import com.psq.architecture.R

/**
 * @author : Anthony.Pan
 * @date   : 2022/9/6 16:48
 * @desc   :
 */

class ContainsEmptyWrapperAdapter<T : Any,DB:ViewDataBinding>(
    private val context: Context,
    private val data: MutableList<Any> = mutableListOf(),
    @LayoutRes private val layoutRes: Int,
    private val variableId: Int,
    private val onBindViewHolder: ((binding: DB, position: Int, item: T) -> Unit)? = null
) : RecyclerView.Adapter<ContainsEmptyWrapperViewHolder>() {


    private val emptyWrapperLayoutRes :Int = R.layout.item_empty


    fun updateItemChanged(position: Int, item: Any) {
        data[position] = item
        notifyItemChanged(position)
    }

    fun updateData(items: List<Any>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItemChanged(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun insertData(items: List<Any>) {
        if (items.isEmpty()) return
        data.addAll(items)
        notifyItemRangeInserted(itemCount, items.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainsEmptyWrapperViewHolder {
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), viewType, parent, false)
        return ContainsEmptyWrapperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainsEmptyWrapperViewHolder, position: Int) {
        holder.binding.apply {
            val item = data[position]
            when (getItemViewType(position)) {
                emptyWrapperLayoutRes -> {
                    setVariable(BR.emptyWrapper, item)
                }
                layoutRes -> {
                    setVariable(variableId, item)
                    //todo 后续添加item点击事件
                    onBindViewHolder?.invoke(holder.binding as DB, position, item as T)
                }
            }
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        if (data[position] is EmptyWrapper) {
            return emptyWrapperLayoutRes
        }
        return layoutRes
    }
}

class ContainsEmptyWrapperViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)