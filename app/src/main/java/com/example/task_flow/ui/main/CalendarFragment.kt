package com.example.task_flow.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task_flow.R
import com.example.task_flow.data.local.AppDatabase
import com.example.task_flow.data.repository.TaskRepository
import com.example.task_flow.databinding.FragmentCalendarBinding
import com.example.task_flow.viewmodel.TaskViewModel
import com.example.task_flow.data.repository.FolderRepository

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext().applicationContext)
        val taskRepo = TaskRepository(db.taskDao())
        val folderRepo = FolderRepository(db.folderDao()) // Инициализируем репозиторий папок
        TaskViewModel.Factory(taskRepo, folderRepo) // Передаем оба
    }

    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCalendarBinding.bind(view)

        val currentDate = java.text.SimpleDateFormat("d MMMM", java.util.Locale("ru")).format(java.util.Date())
        binding.tvCurrentDate.text = currentDate

        setupRecyclerView()

        viewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
        }

        binding.btnCreateTask.setOnClickListener {
            findNavController().navigate(R.id.createTaskFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            onTaskCheckedChange = { task -> viewModel.toggleCompleted(task) },
            onTaskClick = { task ->
                val bundle = Bundle().apply {
                    putSerializable("task", task)
                }
                findNavController().navigate(R.id.taskDetailFragment, bundle)
            }
        )
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}