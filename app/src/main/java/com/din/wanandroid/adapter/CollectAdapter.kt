package com.din.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.din.wanandroid.R
import com.din.wanandroid.base.BaseAdapter
import com.din.wanandroid.model.CollectModel

/**
 * @author dinzhenyan
 * @date   2019-04-25 16:08
 * @IDE    Android Studio
 */
class CollectAdapter : BaseAdapter<CollectModel.Datas>() {

    private lateinit var onItemClickListener: OnItemClickListener       // 点击事件

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById(R.id.title) as TextView
        val star = itemView.findViewById(R.id.star) as TextView
        val time = itemView.findViewById(R.id.time) as TextView
        val author = itemView.findViewById(R.id.author) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_TYPE_CONTENT) {
            val contentView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_collect, parent, false)
            return ContentViewHolder(contentView)
        }
        return super.createViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = beans?.size + ITEM_FOOTER_COUNT ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ContentViewHolder) {
            if (position >= beans.size) {
                return
            }
            val collectBean = beans?.get(position)
            holder.title.setText(collectBean.title)
            holder.star.setText(collectBean.zan.toString())
            holder.time.setText(collectBean.niceDate)
            holder.author.setText(collectBean.author)

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it?.onItemClick(beans.get(position), position) }
            }

            holder.itemView.setOnLongClickListener {
                onItemClickListener?.let { it?.onItemLongClick(beans.get(position), position) }
                true
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(bean: CollectModel.Datas, position: Int)

        fun onItemLongClick(bean: CollectModel.Datas, position: Int)
    }
}