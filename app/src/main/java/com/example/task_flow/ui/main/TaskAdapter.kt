package com.example.task_flow.ui.main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task_flow.data.model.Task
import com.example.task_flow.databinding.ItemTaskBinding

class TaskAdapter(
    private val onTaskCheckedChange: (Task) -> Unit,
    private val onTaskClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<Task> = emptyList()

    fun submitList(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.tvTaskTitle.text = task.title
            binding.cbTaskCompleted.isChecked = task.isCompleted

            if (task.isCompleted) {
                binding.root.setBackgroundColor(Color.parseColor("#90D273"))
            } else {
                binding.root.setBackgroundColor(Color.parseColor("#9DC3E6"))
            }

            binding.cbTaskCompleted.setOnClickListener {
                onTaskCheckedChange(task)
            }

            binding.root.setOnClickListener {
                onTaskClick(task)
            }
        }
    }
}