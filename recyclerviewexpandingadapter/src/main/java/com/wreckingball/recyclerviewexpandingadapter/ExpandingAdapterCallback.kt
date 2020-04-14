package com.wreckingball.recyclerviewexpandingadapter

import android.view.View

interface ExpandingAdapterCallback {
    fun onBindParentView(itemView: View, item: ExpandingData)
    fun onParentExpand(itemView: View)
    fun onParentCollapse(itemView: View)
    fun onBindChildView(itemView: View, item: ExpandingData)
    fun onChildClick(itemView: View, item: ExpandingData)
}