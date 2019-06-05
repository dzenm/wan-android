package com.dzenm.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.model.MultipleTitleBean
import com.dzenm.wanandroid.model.TreeModel

/**
 * @author dinzhenyan
 * @date   2019-06-02 18:47
 */
class TreeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val ITEM_CONTENT = 1001
        val ITEM_TITLE = 1002
        val ITEM_DEFAULT = 1003
    }

    private var mBeans: MutableList<Any> = mutableListOf()
    private lateinit var mOnItemClickListener: OnItemClickListener

    /**
     * 添加更多的数据
     */
    fun addData(beans: MutableList<Any>) {
        mBeans = beans
        notifyDataSetChanged()                  // 添加完数据刷新
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemViewType(position: Int): Int = when (mBeans.get(position)) {
        is MultipleTitleBean -> ITEM_TITLE
        is TreeModel.Children -> ITEM_CONTENT
        else -> ITEM_DEFAULT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        if (viewType == ITEM_CONTENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.rv_item_tree, parent, false)
            return ViewHolder(view)
        } else if (viewType == ITEM_TITLE) {
            val view = LayoutInflater.from(context).inflate(R.layout.rv_item_tree_title, parent, false)
            return TitleViewHolder(view)
        }
        return super.createViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = mBeans?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val treeModel = mBeans.get(position) as TreeModel.Children
            holder.title.setText(treeModel.name)
            holder.title.setOnClickListener {
                mOnItemClickListener?.let { it.onItemClick(position) }
            }
        } else if (holder is TitleViewHolder) {
            val titleModel = mBeans.get(position) as MultipleTitleBean
            holder.title.setText(titleModel.title)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)
    }


    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)
    }

    public interface OnItemClickListener {

        fun onItemClick(position: Int)
    }
}