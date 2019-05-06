package com.din.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.din.wanandroid.R
import com.din.wanandroid.base.BaseAdapter
import com.din.wanandroid.model.ArticleModel

class ArticleAdapter : BaseAdapter<ArticleModel.Datas>() {

    private lateinit var onItemClickListener: OnItemClickListener<ArticleModel.Datas>       // 点击事件

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<ArticleModel.Datas>) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById(R.id.title) as TextView
        val collect = itemView.findViewById(R.id.collect) as ImageView
        val star = itemView.findViewById(R.id.star) as TextView
        val time = itemView.findViewById(R.id.time) as TextView
        val author = itemView.findViewById(R.id.author) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_TYPE_CONTENT) {
            val contentView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_artical, parent, false)
            return ContentViewHolder(contentView)
        } else if (viewType == ITEM_TYPE_FOOTER) {
            val footerView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_foot, parent, false)
            return FooterViewHolder(footerView)
        }
        return super.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ContentViewHolder) {
            if (position >= beans.size) {
                return
            }
            val articleBean = beans?.get(position)
            holder.title.setText(articleBean.title)
            var icon: Int

            if (articleBean.collect) {
                icon = R.drawable.ic_already_collect
            } else {
                icon = R.drawable.ic_no_collect
            }
            holder.collect.setImageResource(icon)

            holder.star.setText(articleBean.zan.toString())
            holder.time.setText(articleBean.niceDate)
            holder.author.setText(articleBean.author)

            holder.collect.setOnClickListener {
                onItemClickListener?.onItemCollectClick(beans.get(position), position)
                notifyItemChanged(position)
            }
            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemClick(beans.get(position), position)
            }
        }
    }
}