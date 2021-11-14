package com.emmm.mobv.screens.login;

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.MainActivity
import com.emmm.mobv.R
import com.emmm.mobv.databinding.LoginFragmentBinding
import com.emmm.mobv.util.Injection

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.login_fragment, container, false
        )

        binding.lifecycleOwner = this

        loginViewModel =
            ViewModelProvider(this, Injection.provideViewModelFactory(requireContext()))
                .get(LoginViewModel::class.java)

        binding.model = loginViewModel

        loginViewModel.action.observe(viewLifecycleOwner) {
            if (it.action == LoginViewModel.Action.SHOW_MAIN_ACTIVITY) {
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }

        return binding.root
    }
}