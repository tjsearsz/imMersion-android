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
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.immersion.immersionandroid.databinding.FragmentBranchListBinding
import com.immersion.immersionandroid.domain.Branch
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import com.immersion.immersionandroid.presentation.BranchViewModel
import com.immersion.immersionandroid.presentation.CompanyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BranchListFragment : Fragment() {

    private var _binding: FragmentBranchListBinding? = null

    private val binding get() = _binding!!

    private val viewModel: BranchViewModel by activityViewModels<BranchViewModel>()

    private val parameters: BranchListFragmentArgs by navArgs()

    private lateinit var list: MutableList<IEmployerOwnerShip>

    private lateinit var adapter: OwnershipAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBranchListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            branch ->
            if(branch.resultCode == Activity.RESULT_OK){
                val data = branch.data
                if(data != null){
                    val branch = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        data.getParcelableExtra("entity", Branch::class.java)!!
                    } else {
                        data.getParcelableExtra("entity")!!
                    }

                    this.list.add(branch)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        (activity as OwnershipActivity).setAddOwnershipFloatingButton {

            val intent = Intent(context, CreateBranchMapsActivity::class.java)
            intent.putExtra("companyId", parameters.companyId)
            resultLauncher.launch(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

            list = viewModel.companyBranches(parameters.companyId)

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                adapter = OwnershipAdapter(list)
                binding.branchRecyclerView.adapter = adapter
                binding.branchRecyclerView.layoutManager = LinearLayoutManager(context)

                adapter.setOnClickListener(object : OwnershipAdapter.OnClickListener {
                    override fun onClick(position: Int, item: IEmployerOwnerShip) {

                        val action =
                            BranchListFragmentDirections.actionBranchListFragmentToJobListFragment(
                                item.id
                            )
                        view.findNavController().navigate(action)
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}