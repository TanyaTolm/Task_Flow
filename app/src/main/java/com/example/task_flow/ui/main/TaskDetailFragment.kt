package com.example.task_flow.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.task_flow.R
import com.example.task_flow.data.local.AppDatabase
import com.example.task_flow.data.model.Task
import com.example.task_flow.data.repository.TaskRepository
import com.example.task_flow.databinding.FragmentTaskDetailBinding
import com.example.task_flow.viewmodel.TaskViewModel
import com.example.task_flow.data.repository.FolderRepository

class TaskDetailFragment : Fragment(R.layout.fragment_task_detail) {
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext().applicationContext)
        val taskRepo = TaskRepository(db.taskDao())
        val folderRepo = FolderRepository(db.folderDao())
        TaskViewModel.Factory(taskRepo, folderRepo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskDetailBinding.bind(view)

        val task = arguments?.getSerializable("task") as? Task

        if (task != null) {
            binding.tvDetailTitle.text = task.title
            binding.tvDetailDescription.text = if (task.description.isEmpty()) "Нет описания" else task.description
            binding.tvDetailDate.text = "📅  " + (if (task.date.isEmpty()) "Без даты" else task.date)

            binding.btnClose.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.btnDelete.setOnClickListener {
                viewModel.deleteTask(task)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}