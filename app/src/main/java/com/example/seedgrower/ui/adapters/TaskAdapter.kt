package com.example.seedgrower.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.example.seedgrower.R
import com.example.seedgrower.models.TaskModel
import com.example.seedgrower.ui.activities.TaskManagerActivity


class TaskAdapter(private val mListener: TaskManagerActivity, private val taskDataSet: ArrayList<TaskModel>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun loadItem(task: TaskModel, listener: OnTaskListener){

            val color = (Math.random() * 16777215).toInt() or (0xFF shl 24)

            val nameOfTask: TextView = itemView.findViewById(R.id.name_of_task)
            val imageView: ImageView = itemView.findViewById(R.id.color_random_for_task)
            val descriptionOfTask: TextView = itemView.findViewById(R.id.description_task)
            val checkTask: ImageView = itemView.findViewById(R.id.checkTask)
            val closeTask : ImageView = itemView.findViewById(R.id.closeTask)

            imageView.setBackgroundColor(color)

            nameOfTask.text = task.title
            descriptionOfTask.text = task.description

            checkTask.setOnClickListener {
                listener.onTaskChangedSuccess(task)
            }

            closeTask.setOnClickListener {
                listener.onTaskChangedClose(task)
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_task, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.loadItem(taskDataSet[position], mListener)
    }

    override fun getItemCount() = taskDataSet.size

    interface OnTaskListener{
        fun onTaskChangedClose(task: TaskModel)

        fun onTaskChangedSuccess(task: TaskModel)
    }

}