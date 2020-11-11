package com.example.projectit.model

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.group_row.view.tv_group_row

class GroupmemberHolder(val customView: View): RecyclerView.ViewHolder(customView) {

    fun bind(groupmember:Groupmember){
        customView.tv_group_row?.text = groupmember.groupname
    }
}