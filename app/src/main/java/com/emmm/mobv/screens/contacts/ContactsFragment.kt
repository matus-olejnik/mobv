package com.emmm.mobv.screens.contacts;

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
import com.emmm.mobv.databinding.ContactsFragmentBinding
import com.emmm.mobv.util.Injection

class ContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel
    private val mainBaseViewModel: MainBaseViewModel by activityViewModels()
    private lateinit var binding: ContactsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.contacts_fragment, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        contactsViewModel =
            ViewModelProvider(
                requireActivity(),
                Injection.provideContactsViewModelFactory(
                    requireContext(),
                    mainBaseViewModel.actualAccountId.value!!
                )
            )
                .get(ContactsViewModel::class.java)

        binding.model = contactsViewModel

        binding.contactList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        //binding.wordsList.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,true)
        //binding.wordsList.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        //binding.wordsList.layoutManager = GridLayoutManager(context,3,RecyclerView.VERTICAL,false)

        val adapter = ContactsAdapter(contactsViewModel)
        binding.contactList.adapter = adapter
        contactsViewModel.contacts.observe(viewLifecycleOwner) {
            adapter.contacts = it
        }

        binding.saveButton.setOnClickListener {
            binding.contactNameEditText.error = ""
            binding.contactAccountIdEditText.error = ""

            when {
                binding.editText1.text.toString() == "" -> {
                    binding.contactNameEditText.error = "Please enter a contact name!"
                }
                binding.editText2.text.toString() == "" -> {
                    binding.contactAccountIdEditText.error = "Please enter a contact account id!"
                }
                else -> {
                    contactsViewModel.insertContact(mainBaseViewModel.actualAccountId.value!!)
                }
            }
        }
        return binding.root
    }
}
