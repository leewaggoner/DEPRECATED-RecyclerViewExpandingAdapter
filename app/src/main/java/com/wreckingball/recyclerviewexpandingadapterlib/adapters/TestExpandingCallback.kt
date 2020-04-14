package com.wreckingball.recyclerviewexpandingadapterlib.adapters

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.wreckingball.recyclerviewexpandingadapter.ExpandingAdapterCallback
import com.wreckingball.recyclerviewexpandingadapter.ExpandingData
import com.wreckingball.recyclerviewexpandingadapterlib.R
import com.wreckingball.recyclerviewexpandingadapterlib.models.TestChild
import com.wreckingball.recyclerviewexpandingadapterlib.models.TestParent
import kotlinx.android.synthetic.main.item_child.view.*
import kotlinx.android.synthetic.main.item_parent.view.*

class TestExpandingCallback : ExpandingAdapterCallback {
    override fun onBindParentView(itemView: View, item: ExpandingData) {
        val tag = itemView.tag
        val parent = item as TestParent
        itemView.parent_title.text = parent.parentText

        //needs to be done because of the way RecyclerView updates views -- if the expanded view
        //is off the screen, and you expand an onscreen view, the onParentCollapse view is null
        //(because it's been recycled), so when you scroll back to the initial expanded view, it's
        //got a new view
        if (item.isExpanded) {
            itemView.arrow.setImageResource(R.drawable.up)
        } else {
            itemView.arrow.setImageResource(R.drawable.down)
        }
    }

    override fun onParentExpand(itemView: View) {
        val tag = itemView.tag
        itemView.arrow.setImageResource(R.drawable.up)
    }

    override fun onParentCollapse(itemView: View) {
        //if the expanded view is off the screen, and you expand an onscreen view, onParentCollapse
        //will not be called
        val tag = itemView.tag
        itemView.arrow.setImageResource(R.drawable.down)
    }

    override fun onBindChildView(itemView: View, item: ExpandingData) {
        val child = item as TestChild
        itemView.child_title.text = child.childText
    }

    override fun onChildClick(itemView: View, item: ExpandingData) {
        val child = item as TestChild
        Snackbar.make(itemView, "Clicked {${child.childText}!", Snackbar.LENGTH_LONG).show()
    }
}