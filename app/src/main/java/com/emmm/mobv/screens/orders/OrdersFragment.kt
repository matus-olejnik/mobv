package com.emmm.mobv.screens.orders;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.MainBaseViewModel
import com.emmm.mobv.R
import com.emmm.mobv.data.db.model.ContactItem
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

        ordersViewModel.eventMoneySent.observe(viewLifecycleOwner) { event ->
            val text = if (event == true) "Odoslanie uspesne" else "Odoslanie NEUSPESNE"
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }

        val contactList = ArrayList<ContactItem>()
        val adapter = CustomContactSpinnerAdapter(context!!, android.R.layout.simple_spinner_item, contactList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.contactNamesSpinner.adapter = adapter

        ordersViewModel.contactsList.observe(viewLifecycleOwner) {
            Log.i("aaa", "dwadwa")
            contactList.clear()
            contactList.add(ContactItem("-1", "Vyberte kontakt", "", ""))
            contactList.addAll(it)
            adapter.notifyDataSetChanged()

            if (args.contactAccountId.isNotEmpty()) {
                binding.contactNamesSpinner.setSelection(
                    contactList.indexOf(contactList.find { contactItem -> contactItem.contactAccountId == args.contactAccountId })
                )
            }
        }

        ordersViewModel.eventSendMoney.observe(viewLifecycleOwner) { event ->
            if (event) {
                ordersViewModel.sendMoney(
                    mainBaseViewModel.actualAccountId.value!!,
                    args.contactAccountId.ifEmpty { (binding.contactNamesSpinner.selectedItem as ContactItem).contactAccountId }
                )
            }
        }

        ordersViewModel.fetchAllContacts(mainBaseViewModel.actualAccountId.value!!)

        return binding.root
    }

    class CustomContactSpinnerAdapter(
        context: Context,
        textViewResourceId: Int,
        val list: List<ContactItem>
    ) : ArrayAdapter<ContactItem>(
        context,
        textViewResourceId,
        list
    ) {
        override fun getCount() = list.size

        override fun getItem(position: Int) = list[position]

        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return (super.getDropDownView(position, convertView, parent) as TextView).apply {
                text = list[position].name
            }
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return (super.getDropDownView(position, convertView, parent) as TextView).apply {
                text = list[position].name
            }
        }
    }
}
