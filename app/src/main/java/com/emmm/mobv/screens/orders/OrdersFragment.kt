package com.emmm.mobv.screens.orders;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.MainBaseViewModel
import com.emmm.mobv.R
import com.emmm.mobv.databinding.OrdersFragmentBinding
import com.emmm.mobv.util.Injection

class OrdersFragment : Fragment() {
    private lateinit var ordersViewModel: OrdersViewModel
    private val mainBaseViewModel: MainBaseViewModel by activityViewModels()
    private lateinit var binding: OrdersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = OrdersFragmentArgs.fromBundle(requireArguments())

        // Inflate the layout for this fragment
        val binding: OrdersFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.orders_fragment, container, false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        ordersViewModel =
            ViewModelProvider(requireActivity(), Injection.provideViewModelFactory(requireContext()))
                .get(OrdersViewModel::class.java)

        binding.model = ordersViewModel

        ordersViewModel.eventSendMoney.observe(viewLifecycleOwner) { event ->
            if (event) {
                ordersViewModel.sendMoney(mainBaseViewModel.actualAccountId.value!!, args.contactAccountId)
            }
        }

        ordersViewModel.eventMoneySent.observe(viewLifecycleOwner) { event ->
            val text = if (event == true) "Odoslanie uspesne" else "Odoslanie NEUSPESNE"
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }


        return binding.root
    }
}
