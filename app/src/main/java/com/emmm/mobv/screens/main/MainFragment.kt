package com.emmm.mobv.screens.main;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.MainBaseViewModel
import com.emmm.mobv.R
import com.emmm.mobv.databinding.MainFragmentBinding
import com.emmm.mobv.util.Injection

class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private val mainBaseViewModel: MainBaseViewModel by activityViewModels()

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: MainFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.main_fragment, container, false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel =
            ViewModelProvider(
                requireActivity(),
                Injection.provideViewModelFactory(requireContext())
            ).get(MainViewModel::class.java)

        binding.model = mainViewModel

        val actAccountId = activity?.intent?.extras?.get("accountId").toString()

        binding.reloadBalanceButton.setOnClickListener {
            mainViewModel.fetchActualBalance(actAccountId)
        }

        mainViewModel.fetchActualBalance(actAccountId)
        mainViewModel.fetchCurrentUser(actAccountId)
        mainBaseViewModel.actualAccountId.value = actAccountId

        return binding.root
    }
}
