package com.wreckingball.recyclerviewexpandingadapterlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wreckingball.recyclerviewexpandingadapter.RecyclerViewExpandingAdapter
import com.wreckingball.recyclerviewexpandingadapterlib.adapters.TestExpandingCallback
import com.wreckingball.recyclerviewexpandingadapterlib.models.TestChild
import com.wreckingball.recyclerviewexpandingadapterlib.models.TestParent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val expandingData = getData()
        recyclerViewExpanding.adapter = RecyclerViewExpandingAdapter(this, expandingData, R.layout.item_parent, R.layout.item_child, TestExpandingCallback())
    }

    private fun getData() : List<TestParent> {
        val list = mutableListOf<TestParent>()
        for (i in 0 until 20) {
            val childList = mutableListOf<TestChild>()
            for (j in 0 until 5) {
                val childData = TestChild("Hi there child $i:$j!")
                childList.add(childData)
            }
            list.add(TestParent("Hello there, parent $i!", childList))
        }

        return list
    }
}
