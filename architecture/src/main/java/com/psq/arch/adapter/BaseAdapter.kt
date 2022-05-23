package com.psq.arch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/22 11:23
 * @desc   :
 */


class ArchSingleAdapter<T>(
    private val context: Context,
    private val data: MutableList<T> = mutableListOf(),
    @LayoutRes private val layoutRes: Int,
    private val variableId: Int,
    private val itemClickListener: ((View, Int, T) -> Unit)? = null,
    private val itemLongClickListener: ((View, Int, T) -> Unit)? = null
) :
    RecyclerView.Adapter<ArchViewHolder>() {


    fun updateItemChanged(position: Int, item: T) {
        data[position] = item
        notifyItemChanged(position)
    }

    fun updateData(items: List<T>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItemChanged(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchViewHolder {
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, parent, false)
        return ArchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArchViewHolder, position: Int) {
        holder.binding.apply {
            val item = data[position]
            setVariable(variableId, item)
            onBindViewHolder(this, position, item)
            holder.itemView.setOnClickListener {
                itemClickListener?.invoke(it, position, item)
            }
            holder.itemView.setOnLongClickListener {
                itemLongClickListener?.invoke(it, position, item)
                true
            }
            executePendingBindings()
        }
    }

    fun onBindViewHolder(binding: ViewDataBinding, position: Int, item: T) {}

    override fun getItemCount(): Int = data.size
}

class ArchViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)