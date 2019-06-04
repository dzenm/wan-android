package com.din.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.din.wanandroid.R
import com.din.wanandroid.base.BaseAdapter
import com.din.wanandroid.model.TreeArticleModel

/**
 * @author dinzhenyan
 * @date   2019-06-03 20:35
 */
class TreeArticleAdapter : BaseAdapter<TreeArticleModel.Datas>() {

    private lateinit var mOnItemClickListener: OnItemClickListener<TreeArticleModel.Datas>

    fun setItemOnClickListener(onItemClickListener: OnItemClickListener<TreeArticleModel.Datas>) {
        mOnItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_TYPE_CONTENT) {
            val contentView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_wx, parent, false)
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
            if (position >= mBeans.size) {
                return
            }
            val treeArticleModel = mBeans?.get(position)
            holder.title.setText(treeArticleModel.title)
            holder.date.setText(treeArticleModel.niceDate)

            var icon: Int
            if (treeArticleModel.collect) {
                icon = R.drawable.ic_already_collect
            } else {
                icon = R.drawable.ic_no_collect
            }
            holder.collect.setImageResource(icon)

            holder.collect.setOnClickListener {
                mOnItemClickListener?.onItemCollectClick(treeArticleModel, position)
                notifyItemChanged(position)
            }

            holder.itemView.setOnClickListener {
                mOnItemClickListener?.let {
                    it.onItemClick(treeArticleModel, position)
                }
            }
        }
    }


    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
        val collect = itemView.findViewById<ImageView>(R.id.iv_collect)
    }
}