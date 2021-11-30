package com.emmm.mobv.screens.login;

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

        binding.lifecycleOwner = viewLifecycleOwner

        loginViewModel =
            ViewModelProvider(requireActivity(), Injection.provideViewModelFactory(requireContext()))
                .get(LoginViewModel::class.java)

        binding.model = loginViewModel

        loginViewModel.action.observe(viewLifecycleOwner) {
            if (it.action == LoginViewModel.Action.SHOW_MAIN_ACTIVITY) {
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("accountId", loginViewModel.accountId.value)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                activity?.finish()
            }
        }

        binding.signUpTextView.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }

        binding.loginButton.setOnClickListener {
            binding.secretSeedEditText.error = ""
            binding.pinEditText.error = ""

            if (binding.editText1.text.toString() == "") {
                binding.secretSeedEditText.error = "Please enter your private key!"
            } else if (binding.editText2.text.toString() == "") {
                binding.pinEditText.error = "Please enter your pin!"
            } else {
                loginViewModel.loginNewUser()
            }
        }

        return binding.root
    }
}