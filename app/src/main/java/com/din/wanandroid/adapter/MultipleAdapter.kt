package com.din.wanandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.din.wanandroid.R
import com.din.wanandroid.model.*

/**
 * @author dinzhenyan
 * @date   2019-04-25 21:51
 * @IDE    Android Studio
 */
class MultipleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var beans: MutableList<Any> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    private lateinit var context: Context

    companion object {
        val TYPE_PROJECT = 1
        val TYPE_TREE = 2
        val TYPE_WX = 3
        val TYPE_NAVI = 4
        val TYPE_TITLE = 5
        val TYPE_DEFAULT = 6
    }

    /**
     * 添加更多的数据
     */
    fun addData(beans: MutableList<Any>) {
        this.beans = beans
        notifyDataSetChanged()                  // 添加完数据刷新
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemViewType(position: Int): Int = when (beans.get(position)) {
        is NewProjectModel.Data.Datas -> TYPE_PROJECT
        is TreeModel.Data -> TYPE_TREE
        is WxModel.Data -> TYPE_WX
        is NaviModel.Data -> TYPE_NAVI
        is MultipleTitleBean -> TYPE_TITLE
        else -> TYPE_DEFAULT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        when (viewType) {
            TYPE_PROJECT -> {
                val view = LayoutInflater.from(context).inflate(R.layout.rv_item_multiple_project, parent, false)
                return ProjectViewHolder(view)
            }
            TYPE_TREE -> {
                val view = LayoutInflater.from(context).inflate(0, parent, false)
                return TreeViewHolder(view)
            }
            TYPE_WX -> {
                val view = LayoutInflater.from(context).inflate(0, parent, false)
                return WxViewHolder(view)
            }
            TYPE_NAVI -> {
                val view = LayoutInflater.from(context).inflate(0, parent, false)
                return NaviViewHolder(view)
            }
            TYPE_TITLE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.rv_item_multiple_title, parent, false)
                return TitleViewHolder(view)
            }
        }
        return super.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProjectViewHolder -> {
                val projectBean = beans.get(position) as NewProjectModel.Data.Datas
                Glide.with(context).load(projectBean.envelopePic).into(holder.image)
                holder.title.setText(projectBean.title)
                var icon: Int
                if (projectBean.collect) {
                    icon = R.drawable.ic_already_collect
                } else {
                    icon = R.drawable.ic_no_collect
                }
                holder.collect.setImageResource(icon)

                holder.content.setText(projectBean.desc)
                holder.time.setText(projectBean.niceDate)
                holder.author.setText(projectBean.author)
                holder.title.setText(projectBean.title)

                holder.itemView.setOnClickListener {
                    onItemClickListener?.let { it.onItemClick(position) }
                }
            }

            is TreeViewHolder -> {

            }

            is WxViewHolder -> {

            }

            is NaviViewHolder -> {

            }
            is TitleViewHolder -> {
                val titleBean = beans.get(position) as MultipleTitleBean
                holder.title.setText(titleBean.title)
                holder.more.setOnClickListener {
                    onItemClickListener?.let { it.onItemClick(position ) }
                }
            }
        }
    }

    override fun getItemCount(): Int = beans?.size ?: 0

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)
        val content = itemView.findViewById<TextView>(R.id.content)
        val collect = itemView.findViewById<ImageView>(R.id.collect)
        val time = itemView.findViewById<TextView>(R.id.time)
        val author = itemView.findViewById<TextView>(R.id.author)
        val same_project = itemView.findViewById<TextView>(R.id.same_project)
    }

    class TreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class WxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class NaviViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val more = itemView.findViewById<TextView>(R.id.more)
    }

    interface OnItemClickListener {

        fun onItemClick(position: Int)

    }
}