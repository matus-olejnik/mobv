package com.emmm.mobv.screens.registration;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.R
import com.emmm.mobv.databinding.RegistrationFragmentBinding
import com.emmm.mobv.util.Injection

class RegistrationFragment : Fragment() {

    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: RegistrationFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.registration_fragment, container, false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        registrationViewModel =
            ViewModelProvider(requireActivity(), Injection.provideViewModelFactory(requireContext()))
                .get(RegistrationViewModel::class.java)

        binding.model = registrationViewModel

        return binding.root
    }
}