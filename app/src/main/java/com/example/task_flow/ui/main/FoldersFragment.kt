package com.example.task_flow.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task_flow.R
import com.example.task_flow.data.local.AppDatabase
import com.example.task_flow.data.repository.FolderRepository
import com.example.task_flow.data.repository.TaskRepository
import com.example.task_flow.databinding.FragmentFoldersBinding
import com.example.task_flow.viewmodel.TaskViewModel

class FoldersFragment : Fragment(R.layout.fragment_folders) {
    private var _binding: FragmentFoldersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext().applicationContext)
        val taskRepo = TaskRepository(db.taskDao())
        val folderRepo = FolderRepository(db.folderDao())
        TaskViewModel.Factory(taskRepo, folderRepo)
    }

    private lateinit var adapter: FolderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFoldersBinding.bind(view)

        setupRecyclerView()

        viewModel.allFolders.observe(viewLifecycleOwner) { folders ->
            adapter.submitList(folders)
        }

        binding.btnCreateTaskFolder.setOnClickListener {
            findNavController().navigate(R.id.createTaskFragment)
        }

        binding.btnCreateFolder.setOnClickListener {
            findNavController().navigate(R.id.createFolderFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = FolderAdapter { folder ->
            val bundle = Bundle().apply {
                putLong("folderId", folder.id)
                putString("folderName", folder.name)
            }
            findNavController().navigate(R.id.folderDetailFragment, bundle)
        }
        binding.rvFolders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFolders.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}