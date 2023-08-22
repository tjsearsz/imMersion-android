package com.immersion.immersionandroid.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.immersion.immersionandroid.databinding.FragmentJobListBinding
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import com.immersion.immersionandroid.domain.Job
import com.immersion.immersionandroid.presentation.JobViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class JobListFragment : Fragment() {

    private var _binding: FragmentJobListBinding? = null

    private val binding get() = _binding!!

    private val viewModel: JobViewModel by activityViewModels<JobViewModel>()

    private val parameters: JobListFragmentArgs by navArgs()

    private lateinit var list: MutableList<IEmployerOwnerShip>

    private lateinit var adapter: OwnershipAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJobListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            job ->
            if(job.resultCode == Activity.RESULT_OK){
                val data = job.data
                if(data !== null){
                   val job = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                       data.getParcelableExtra("entity", Job::class.java)!!
                   } else {
                       data.getParcelableExtra("entity")!!
                   }

                    this.list.add(job)
                    this.adapter.notifyDataSetChanged()
                }
            }
        }

        (activity as OwnershipActivity).setAddOwnershipFloatingButton {
            val intent = Intent(context, CreateJobActivity::class.java)
            intent.putExtra("branchId", parameters.branchId)
            resultListener.launch(intent)
        }

        lifecycleScope.launch(Dispatchers.IO) {

            list = viewModel.branchJobs(parameters.branchId)

            lifecycleScope.launch(Dispatchers.Main) {
                adapter = OwnershipAdapter(list)
                binding.jobRecyclerView.adapter = adapter
                binding.jobRecyclerView.layoutManager = LinearLayoutManager(context)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }
}