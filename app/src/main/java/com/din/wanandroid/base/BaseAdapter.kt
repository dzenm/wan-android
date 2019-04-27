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
        const val ITEM_TYPE_LOADING = 4

        const val LOAD_STATUS_LOADING = 1           // 加载的状态为正在加载
        const val LOAD_STATUS_COMPLETE = 2          // 加载的状态为已完成
        const val LOAD_STATUS_END = 3               // 加载的状态为全部加载完
    }

    var beans: MutableList<T> = arrayListOf()       // list数据源

    var loadStatus = 0                              // 加载的状态标志位

    /**
     * 底部ViewHolder
     */
    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progress = itemView.findViewById<ProgressBar>(R.id.progressBar)
        val tip = itemView.findViewById<TextView>(R.id.tipText)
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
}