package com.emmm.mobv.screens.registration;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.emmm.mobv.R
import com.emmm.mobv.databinding.RegistrationFragmentBinding

class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: RegistrationFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.registration_fragment, container, false
        )

        return binding.root
    }
}
