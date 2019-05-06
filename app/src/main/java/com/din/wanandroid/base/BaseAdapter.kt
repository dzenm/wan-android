package com.din.wanandroid.base

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.din.wanandroid.R

abstract class BaseAdapter<T : Any> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_FOOTER_COUNT = 1             // Footer布局个数

        const val ITEM_TYPE_FOOTER = 1              // Item为底部类型
        const val ITEM_TYPE_CONTENT = 2             // Item为普通类型
        const val ITEM_TYPR_ERROR = 3
        const val ITEM_TYPE_EMPTY = 4

        const val LOAD_STATUS_LOADING = 1           // 加载的状态为正在加载
        const val LOAD_STATUS_COMPLETE = 2          // 加载的状态为已完成
        const val LOAD_STATUS_END = 3               // 加载的状态为全部加载完
    }

    var beans: MutableList<T> = arrayListOf()       // list数据源

    var loadStatus = 0                              // 加载的状态标志位

    /**
     * 加载失败的界面
     */
    fun setLoadError() {
        beans.clear()
        notifyDataSetChanged()
    }

    /**
     * 加载为空的界面
     */
    fun setLoadEmpty() {
        beans.clear()
        notifyDataSetChanged()
    }

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

    override fun getItemCount(): Int = beans?.size + ITEM_FOOTER_COUNT ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FooterViewHolder) {
            setFooterData(holder)
        }
    }

    /**
     * 判断Item的类型
     */
    override fun getItemViewType(position: Int): Int {
        if (position + 1 == itemCount) {
            return ITEM_TYPE_FOOTER
        }
        return ITEM_TYPE_CONTENT
    }

    fun setFooterData(holder: FooterViewHolder) {
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

    interface OnItemClickListener<T : Any> {

        fun onItemCollectClick(bean: T, position: Int)

        fun onItemClick(bean: T, position: Int)
    }
}