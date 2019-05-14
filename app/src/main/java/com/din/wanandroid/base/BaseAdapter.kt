package com.din.wanandroid.base

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.din.wanandroid.R

abstract class BaseAdapter<T : Any> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_FOOTER_COUNT = 1             // Footer布局个数

        const val ITEM_TYPE_FOOTER = 1              // Item为底部类型
        const val ITEM_TYPE_CONTENT = 2             // Item为普通类型
        const val ITEM_TYPE_ERROR = 3
        const val ITEM_TYPE_EMPTY = 4

        const val LOAD_STATUS_LOADING = 1           // 加载的状态为正在加载
        const val LOAD_STATUS_COMPLETE = 2          // 加载的状态为已完成
        const val LOAD_STATUS_END = 3               // 加载的状态为全部加载完
    }

    var beans: MutableList<T> = arrayListOf()       // list数据源

    var loadStatus = 0                              // 加载的状态标志位

    /**
     * 设置底部加载的状态
     */
    fun setLoadingStatus(status: Int) {
        this.loadStatus = status
        notifyItemRangeChanged(beans.size, 1)
    }

    /**
     * 添加更多的数据
     */
    fun addData(articleBeans: MutableList<T>, position: Int) {
        this.beans?.addAll(articleBeans)
        notifyItemRangeInserted(position + 1, articleBeans.size)    // 添加完数据刷新
    }

    /**
     * 底部ViewHolder
     */
    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progress = itemView.findViewById<ProgressBar>(R.id.progressBar)
        val tip = itemView.findViewById<TextView>(R.id.tipText)
    }

    /**
     * 加载错误的ViewHolder
     */
    class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val image = itemView.findViewById<ImageView>(R.id.image)
    }

    /**
     * 加载为空的ViewHolder
     */
    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val image = itemView.findViewById<ImageView>(R.id.image)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FooterViewHolder) {
            setFooterState(holder)
        } else if (holder is ErrorViewHolder) {
            setErrorState(holder, position)
        } else if (holder is EmptyViewHolder) {
            setEmptyState(holder, position)
        }
    }

    override fun getItemCount(): Int = beans?.size + ITEM_FOOTER_COUNT ?: 0

    /**
     * 判断Item的类型
     */
    override fun getItemViewType(position: Int): Int {
        if (position + 1 == itemCount) {
            return ITEM_TYPE_FOOTER
        }
        return ITEM_TYPE_CONTENT
    }

    /**
     * 底部加载更多的状态
     */
    fun setFooterState(holder: FooterViewHolder) {
        when (loadStatus) {
            LOAD_STATUS_LOADING -> {
                holder.progress.visibility = View.VISIBLE
                holder.tip.setText("正在加载中")
            }
            LOAD_STATUS_COMPLETE -> {
                holder.progress.visibility = View.GONE
                holder.tip.setText("加载更多")
            }
            LOAD_STATUS_END -> {
                holder.progress.visibility = View.GONE
                holder.tip.setText("滑倒底部了")
            }
        }
    }

    /**
     * 加载错误的状态
     */
    fun setErrorState(holder: ErrorViewHolder, position: Int) {
        val error = beans[position] as Error
        holder.image.setImageResource(error.image)
        holder.title.setText(error.title)
    }

    /**
     * 加载空数据的状态
     */
    fun setEmptyState(holder: EmptyViewHolder, position: Int) {
        val empty = beans[position] as Empty
        holder.image.setImageResource(empty.image)
        holder.title.setText(empty.title)
    }


    interface OnItemClickListener<T : Any> {

        fun onItemCollectClick(bean: T, position: Int)

        fun onItemClick(bean: T, position: Int)
    }

    /**
     * 加载错误的标题和图片
     */
    data class Error(val title: String, val image: Int)

    /**
     * 加载为空的标题和图片
     */
    data class Empty(val title: String, val image: Int)
}