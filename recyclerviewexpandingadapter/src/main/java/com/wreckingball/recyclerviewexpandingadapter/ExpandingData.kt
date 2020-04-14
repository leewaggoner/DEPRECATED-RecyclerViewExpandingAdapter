package com.wreckingball.recyclerviewexpandingadapter

abstract class ExpandingData(var isParent: Boolean = false, var children: MutableList<out ExpandingData>? = null) {
    var isExpanded: Boolean = false
    var index: Int = 0
}