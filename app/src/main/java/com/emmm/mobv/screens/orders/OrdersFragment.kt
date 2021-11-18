package com.emmm.mobv.screens.orders;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.R
import com.emmm.mobv.databinding.OrdersFragmentBinding
import com.emmm.mobv.util.Injection

class OrdersFragment : Fragment() {
    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var binding: OrdersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = OrdersFragmentArgs.fromBundle(requireArguments())
        args.

        // Inflate the layout for this fragment
        val binding: OrdersFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.orders_fragment, container, false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        ordersViewModel =
            ViewModelProvider(requireActivity(), Injection.provideViewModelFactory(requireContext()))
                .get(OrdersViewModel::class.java)

        binding.model = ordersViewModel

        ordersViewModel.eventSendMoney.observe(viewLifecycleOwner, Observer { event ->
            if (event) {
                ordersViewModel.sendMoney(args.contactAccountId);
            }
        })


        return binding.root
    }
}
