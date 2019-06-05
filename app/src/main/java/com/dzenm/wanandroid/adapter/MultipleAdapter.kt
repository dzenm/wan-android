package com.dzenm.wanandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dzenm.wanandroid.R
import com.dzenm.wanandroid.model.MultipleTitleBean
import com.dzenm.wanandroid.model.NaviModel
import com.dzenm.wanandroid.model.NewProjectModel
import com.dzenm.wanandroid.model.WxModel

/**
 * @author dinzhenyan
 * @date   2019-04-25 21:51
 */
class MultipleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mBeans: MutableList<Any> = mutableListOf()
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
        mBeans = beans
        notifyDataSetChanged()                  // 添加完数据刷新
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    /**
     * 设置多类型的Item
     */
    override fun getItemViewType(position: Int): Int = when (mBeans.get(position)) {
        is NewProjectModel.Datas -> TYPE_PROJECT
        is WxModel -> TYPE_WX
        is NaviModel -> TYPE_NAVI
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
            TYPE_WX -> {
                val view = LayoutInflater.from(context).inflate(R.layout.rv_item_multiple_wx, parent, false)
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
                val projectModel = mBeans.get(position) as NewProjectModel.Datas
                Glide.with(context).load(projectModel.envelopePic).into(holder.image)
                holder.title.setText(projectModel.title)
                var icon: Int
                if (projectModel.collect) {
                    icon = R.drawable.ic_already_collect
                } else {
                    icon = R.drawable.ic_no_collect
                }
                holder.collect.setImageResource(icon)

                holder.content.setText(projectModel.desc)
                holder.time.setText(projectModel.niceDate)
                holder.author.setText(projectModel.author)
                holder.title.setText(projectModel.title)

                holder.itemView.setOnClickListener {
                    onItemClickListener?.let { it.onItemClick(position) }
                }
            }
            is WxViewHolder -> {
                val wxModel = mBeans.get(position) as WxModel
                holder.title.setText(wxModel.name)

                var icon: Int
                if (wxModel.userControlSetTop) {
                    icon = R.drawable.ic_already_collect
                } else {
                    icon = R.drawable.ic_no_collect
                }
                holder.collect.setImageResource(icon)

                holder.itemView.setOnClickListener {
                    onItemClickListener?.let { it.onItemClick(position) }
                }
            }
            is NaviViewHolder -> {

            }
            is TitleViewHolder -> {
                val titleModel = mBeans.get(position) as MultipleTitleBean
                holder.title.setText(titleModel.title)
                holder.more.setOnClickListener {
                    onItemClickListener?.let { it.onItemClick(position) }
                }
            }
        }
    }

    override fun getItemCount(): Int = mBeans?.size ?: 0

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.iv_image)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val content = itemView.findViewById<TextView>(R.id.tv_content)
        val collect = itemView.findViewById<ImageView>(R.id.iv_collect)
        val time = itemView.findViewById<TextView>(R.id.tv_time)
        val author = itemView.findViewById<TextView>(R.id.tv_author)
        val same_project = itemView.findViewById<TextView>(R.id.tv_same_project)
    }

    class WxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val collect = itemView.findViewById<ImageView>(R.id.iv_collect)
    }

    class NaviViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val more = itemView.findViewById<TextView>(R.id.tv_more)
    }

    interface OnItemClickListener {

        fun onItemClick(position: Int)

    }
}