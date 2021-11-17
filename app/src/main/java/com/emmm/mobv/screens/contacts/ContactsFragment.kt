package com.emmm.mobv.screens.contacts;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emmm.mobv.R
import com.emmm.mobv.databinding.ContactsFragmentBinding
import com.emmm.mobv.util.Injection

class ContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var binding: ContactsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.contacts_fragment, container, false
        )
        binding.lifecycleOwner = this
        contactsViewModel =
            ViewModelProvider(this, Injection.provideViewModelFactory(requireContext()))
                .get(ContactsViewModel::class.java)

        val args = ContactsFragmentArgs.fromBundle(requireArguments())

        binding.model = contactsViewModel

        binding.contactList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        //binding.wordsList.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,true)
        //binding.wordsList.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        //binding.wordsList.layoutManager = GridLayoutManager(context,3,RecyclerView.VERTICAL,false)

        val adapter = ContactsAdapter(contactsViewModel)
        binding.contactList.adapter = adapter
        contactsViewModel.contacts.observe(viewLifecycleOwner) {
            adapter.contacts = it
        }

        binding.saveButton.setOnClickListener {
            contactsViewModel.insertContact(args.accountId)
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ContactsFragmentArgs.fromBundle(requireArguments())
        binding.model?.fetchContacts(args.accountId)
    }
}
