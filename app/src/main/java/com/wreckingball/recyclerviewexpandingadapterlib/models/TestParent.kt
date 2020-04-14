package com.wreckingball.recyclerviewexpandingadapterlib.models

import com.wreckingball.recyclerviewexpandingadapter.ExpandingData

class TestParent(
    var parentText: String,
    childList: MutableList<TestChild>?
) : ExpandingData(true, childList)