package com.example.task_flow.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.task_flow.R
import com.example.task_flow.data.local.AppDatabase
import com.example.task_flow.data.model.Task
import com.example.task_flow.data.repository.FolderRepository
import com.example.task_flow.data.repository.TaskRepository
import com.example.task_flow.databinding.FragmentCreateTaskBinding
import com.example.task_flow.viewmodel.TaskViewModel
import java.util.Calendar
import com.example.task_flow.data.model.Folder

class CreateTaskFragment : Fragment(R.layout.fragment_create_task) {

    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext().applicationContext)
        val taskRepo = TaskRepository(db.taskDao())
        val folderRepo = FolderRepository(db.folderDao())
        TaskViewModel.Factory(taskRepo, folderRepo)
    }

    private var selectedDate: String = ""
    private var selectedFolderId: Long? = null

    private var currentFoldersList: List<Folder> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateTaskBinding.bind(view)

        viewModel.allFolders.observe(viewLifecycleOwner) { folders ->
            currentFoldersList = folders
        }

        binding.tvSelectDate.setOnClickListener {
            showDatePicker()
        }

        binding.tvSelectFolder.setOnClickListener {
            showFolderPicker()
        }

        binding.btnSave.setOnClickListener {
            val taskTitle = binding.etTaskTitle.text.toString().trim()
            val taskDescription = binding.etTaskDescription.text.toString().trim()

            if (taskTitle.isNotEmpty()) {
                val newTask = Task(
                    title = taskTitle,
                    description = taskDescription,
                    date = selectedDate,
                    folderId = selectedFolderId
                )
                viewModel.addTask(newTask)
                findNavController().popBackStack()
            }
        }
    }

    private fun showFolderPicker() {
        val folders = currentFoldersList

        if (folders.isEmpty()) {
            Toast.makeText(requireContext(), "Сначала создайте хотя бы одну папку!", Toast.LENGTH_SHORT).show()
            return
        }

        val folderNames = folders.map { it.name }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Выберите папку")
            .setItems(folderNames) { _, which ->
                val selectedFolder = folders[which]
                selectedFolderId = selectedFolder.id
                binding.tvSelectFolder.text = "📁  ${selectedFolder.name}"
            }
            .show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear)
                binding.tvSelectDate.text = "📅  $selectedDate"
            }, year, month, day)
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}