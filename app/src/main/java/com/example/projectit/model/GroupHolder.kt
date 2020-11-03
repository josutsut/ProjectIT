package com.example.projectit.model

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.group_row.view.*
import kotlinx.android.synthetic.main.group_row.view.tv_group_row
import kotlinx.android.synthetic.main.row.view.*

class GroupHolder(val customView: View): RecyclerView.ViewHolder(customView) {

    fun bind(group:Group){
        customView.tv_group_row?.text = group.grouphead
    }
}