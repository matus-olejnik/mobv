package com.emmm.mobv.screens.main;

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.emmm.mobv.R
import com.emmm.mobv.WelcomeActivity
import com.emmm.mobv.databinding.MainFragmentBinding
import com.emmm.mobv.util.Injection

class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

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

        binding.contactsButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToContactsFragment(actAccountId))
        }

        binding.logoutButton.setOnClickListener {
            logout(actAccountId)
        }

        mainViewModel.fetchActualBalance(actAccountId)
        mainViewModel.fetchCurrentUser(actAccountId)
        mainViewModel.actualAccountId.value = actAccountId

        return binding.root
    }

    private fun logout(actAccountId: String) {
        mainViewModel.logout(actAccountId)
        val intent = Intent(activity, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }
}
