package com.wreckingball.recyclerviewexpandingadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewExpandingAdapter(
    private val context: Context,
    list: List<ExpandingData>,
    private val parentLayout: Int,
    private val childLayout: Int,
    private val expandingAdapterCallback: ExpandingAdapterCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private var itemList: MutableList<ExpandingData>
    private var expandingAdapter: RecyclerViewExpandingAdapter = this

    companion object {
        private const val TYPE_PARENT = 1
        private const val TYPE_CHILD = 2
        private const val UNEXPANDED = -1
        private var itemExpanded = UNEXPANDED
    }

    init {
        itemExpanded = UNEXPANDED
        itemList = mutableListOf()
        for ((index, item) in list.withIndex()) {
            item.index = index
            itemList.add(item)
        }
    }

    class ParentViewHolder(private val parentView: View,
                           private val list: MutableList<ExpandingData>,
                           private val expandingAdapter: RecyclerViewExpandingAdapter,
                           private val adapterCallback: ExpandingAdapterCallback) : RecyclerView.ViewHolder(parentView) {
        private var maxItems = 0

        init {
            maxItems = list[list.size - 1].index
        }
        fun bindView(item: ExpandingData) {
            parentView.tag = item.index
            adapterCallback.onBindParentView(parentView, item)
            parentView.setOnClickListener {view ->
                expandCollapse(view)
            }
        }

        private fun expandCollapse(view: View) {
            val index = list[layoutPosition].index
            when (itemExpanded) {
                UNEXPANDED -> expandAlbum(list, index, view)
                layoutPosition -> collapseAlbum(list, itemExpanded, view)
                else -> {
                    //user clicked a parent while a different parent is expanded
                    collapseAlbum(list, itemExpanded, view)
                    expandAlbum(list, index, view)
                }
            }
        }

        private fun expandAlbum(list: MutableList<ExpandingData>, index: Int, view: View) {
            val listSize = list.size - 1
            val children = list[index].children
            if (!children.isNullOrEmpty()) {
                adapterCallback.onParentExpand(view)
                itemExpanded = index
                for ((childIndex, i) in (index + 1 until index + 1 + children.size).withIndex()) {
                    val child = children[childIndex]
                    list.add(i, child)
                }
                list[index].isExpanded = true
                expandingAdapter.notifyItemRangeInserted(layoutPosition + 1, children.size)
                if (index == maxItems) {
                    val recyclerView = view.parent as RecyclerView
                    recyclerView.scrollToPosition(listSize + 1)
                }
            }
        }

        private fun collapseAlbum(list: MutableList<ExpandingData>, expandedItem: Int, view: View) {
            val parentView = view.parent as View
            val collapseView = parentView.findViewWithTag<View>(expandedItem)
            if (collapseView != null) {
                adapterCallback.onParentCollapse(collapseView)
            }

            val children = list[expandedItem].children
            if (!children.isNullOrEmpty()) {
                itemExpanded = UNEXPANDED
                for (i in (expandedItem + 1 until expandedItem + 1 + children.size)) {
                    list.removeAt(expandedItem + 1)
                }
                list[expandedItem].isExpanded = false
                expandingAdapter.notifyItemRangeRemoved(expandedItem + 1, children.size)
            }
        }
    }

    class ChildViewHolder(private val childView: View, private val adapterCallback: ExpandingAdapterCallback) : RecyclerView.ViewHolder(childView) {
        fun bindView(item: ExpandingData) {
            adapterCallback.onBindChildView(childView, item)
            childView.setOnClickListener {
                adapterCallback.onChildClick(childView, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PARENT) {
            val view = LayoutInflater.from(context).inflate(parentLayout, parent, false)
            ParentViewHolder(view, itemList, expandingAdapter, expandingAdapterCallback)
        } else {
            val view = LayoutInflater.from(context).inflate(childLayout, parent, false)
            ChildViewHolder(view, expandingAdapterCallback)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        if (item.isParent) {
            (holder as ParentViewHolder).bindView(item)
        } else {
            (holder as ChildViewHolder).bindView(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isParent) {
            TYPE_PARENT
        } else {
            TYPE_CHILD
        }
    }
}