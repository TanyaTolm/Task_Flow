package com.example.task_flow.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task_flow.R
import com.example.task_flow.data.local.AppDatabase
import com.example.task_flow.data.model.Folder
import com.example.task_flow.data.repository.FolderRepository
import com.example.task_flow.data.repository.TaskRepository
import com.example.task_flow.databinding.FragmentFolderDetailBinding
import com.example.task_flow.viewmodel.TaskViewModel

class FolderDetailFragment : Fragment(R.layout.fragment_folder_detail) {
    private var _binding: FragmentFolderDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext().applicationContext)
        val taskRepo = TaskRepository(db.taskDao())
        val folderRepo = FolderRepository(db.folderDao())
        TaskViewModel.Factory(taskRepo, folderRepo)
    }

    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFolderDetailBinding.bind(view)

        // ИСПРАВЛЕНО: Используем getLong и 0L вместо getInt и 0
        val folderId = arguments?.getLong("folderId") ?: 0L
        val folderName = arguments?.getString("folderName") ?: "Папка"

        binding.tvFolderTitle.text = folderName

        adapter = TaskAdapter(
            onTaskCheckedChange = { task -> viewModel.toggleCompleted(task) },
            onTaskClick = { task ->
                val bundle = Bundle().apply { putSerializable("task", task) }
                findNavController().navigate(R.id.taskDetailFragment, bundle)
            }
        )
        binding.rvFolderTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFolderTasks.adapter = adapter

        viewModel.getTasksByFolder(folderId).observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
        }

        binding.btnBackFromFolder.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDeleteFolder.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Удаление папки")
                .setMessage("Вы уверены, что хотите удалить эту папку? Задачи внутри неё останутся на главном экране.")
                .setPositiveButton("Удалить") { _, _ ->
                    val folderToDelete = Folder(id = folderId, name = folderName)
                    viewModel.deleteFolder(folderToDelete)
                    findNavController().popBackStack()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}