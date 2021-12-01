package com.emmm.mobv.screens.welcome;

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.emmm.mobv.R
import com.emmm.mobv.databinding.WelcomeFragmentBinding
import com.emmm.mobv.util.Injection

class WelcomeFragment : Fragment() {

    private lateinit var welcomeViewModel: WelcomeViewModel
    private lateinit var binding: WelcomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: WelcomeFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.welcome_fragment, container, false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        welcomeViewModel =
            ViewModelProvider(
                requireActivity(),
                Injection.provideViewModelFactory(requireContext())
            ).get(WelcomeViewModel::class.java)

        binding.model = welcomeViewModel

        binding.toLoginButton.setOnClickListener {
            if (checkInternetConnection(activity?.applicationContext!!)) {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
            } else {
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        binding.toRegistrationButton.setOnClickListener {
            if (checkInternetConnection(activity?.applicationContext!!)) {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegistrationFragment())
            } else {
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            }
        }
        return false
    }
}
