package com.emmm.mobv.screens.transactions;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emmm.mobv.MainBaseViewModel
import com.emmm.mobv.R
import com.emmm.mobv.databinding.TransactionsFragmentBinding
import com.emmm.mobv.util.Injection

class TransactionsFragment : Fragment() {
    private lateinit var transactionsViewModel: TransactionsViewModel
    private val mainBaseViewModel: MainBaseViewModel by activityViewModels()
    private lateinit var binding: TransactionsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.transactions_fragment, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        transactionsViewModel =
            ViewModelProvider(
                requireActivity(),
                Injection.provideTransactionsViewModelFactory(
                    requireContext(),
                    mainBaseViewModel.actualAccountId.value!!
                )
            )
                .get(TransactionsViewModel::class.java)

        binding.model = transactionsViewModel

        binding.transactionList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = TransactionsAdapter(transactionsViewModel)
        binding.transactionList.adapter = adapter
        transactionsViewModel.transactions.observe(viewLifecycleOwner) {
            adapter.transactions = it
        }

        transactionsViewModel.fetchTransactions(mainBaseViewModel.actualAccountId.value!!)

        return binding.root
    }
}
