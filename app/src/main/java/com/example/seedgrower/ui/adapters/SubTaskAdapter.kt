package com.example.seedgrower.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.example.seedgrower.R
import com.example.seedgrower.models.SubTaskModel
import com.example.seedgrower.ui.activities.DetailTaskActivity


class SubTaskAdapter(private val mListener: DetailTaskActivity, private val subTaskDataSet: ArrayList<SubTaskModel>?) : RecyclerView.Adapter<SubTaskAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun loadItem(task: SubTaskModel?, listener: OnTaskListener){

            val nameOfSubTask: TextView = itemView.findViewById(R.id.name_for_sub_task)
            val checkSubTask: CheckBox = itemView.findViewById(R.id.checkboxSubTask)

            nameOfSubTask.text = task?.title

            checkSubTask.setOnClickListener {
                if(it.isActivated)
                    listener.onSubCheck(task)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_sub_task, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.loadItem(subTaskDataSet?.get(position), mListener)
    }

    override fun getItemCount() = subTaskDataSet!!.size

    interface OnTaskListener{
        fun onSubCheck(task: SubTaskModel?)
    }

}