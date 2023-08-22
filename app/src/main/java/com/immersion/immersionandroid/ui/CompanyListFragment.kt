package com.immersion.immersionandroid.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.immersion.immersionandroid.databinding.FragmentCompanyListBinding
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.domain.IEmployerOwnerShip
import com.immersion.immersionandroid.presentation.CompanyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CompanyListFragment : Fragment() {

    private var _binding: FragmentCompanyListBinding? = null

    private val binding get() = _binding!!

    private val viewModel: CompanyViewModel by activityViewModels<CompanyViewModel>()

    private lateinit var companyList: MutableList<IEmployerOwnerShip>

    private lateinit var adapter: OwnershipAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            Log.d("TESTING", "Nos vamos PA TRAS! ${result.resultCode}")
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data
                if(data != null){
                    val company = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        data.getParcelableExtra("entity", Company::class.java)!!
                    } else {
                        data.getParcelableExtra("entity")!!
                    }

                    this.companyList.add(company)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        (activity as OwnershipActivity).setAddOwnershipFloatingButton {

            val intent = Intent(context, CreateCompanyActivity::class.java)
            resultLauncher.launch(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

            companyList = viewModel.getUserCompanies()

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                adapter = OwnershipAdapter(companyList)
                binding.companyRecyclerView.adapter = adapter
                binding.companyRecyclerView.layoutManager =
                    LinearLayoutManager(context) //If we add that to the fragment, it can be omitted

                adapter.setOnClickListener(object : OwnershipAdapter.OnClickListener {
                    override fun onClick(position: Int, item: IEmployerOwnerShip) {

                        val action =
                            CompanyListFragmentDirections.actionCompanyListFragmentToBranchListFragment(
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