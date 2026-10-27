package com.example.task_flow.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.task_flow.R
import com.example.task_flow.data.local.AppDatabase
import com.example.task_flow.data.model.Folder
import com.example.task_flow.data.repository.FolderRepository
import com.example.task_flow.data.repository.TaskRepository
import com.example.task_flow.databinding.FragmentCreateFolderBinding
import com.example.task_flow.viewmodel.TaskViewModel

class CreateFolderFragment : Fragment(R.layout.fragment_create_folder) {
    private var _binding: FragmentCreateFolderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext().applicationContext)
        val taskRepo = TaskRepository(db.taskDao())
        val folderRepo = FolderRepository(db.folderDao())
        TaskViewModel.Factory(taskRepo, folderRepo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateFolderBinding.bind(view)

        binding.btnSaveFolder.setOnClickListener {
            val folderName = binding.etFolderName.text.toString().trim()
            if (folderName.isNotEmpty()) {
                viewModel.addFolder(Folder(name = folderName))
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}