package com.emmm.mobv.screens.logout;

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.emmm.mobv.MainBaseViewModel
import com.emmm.mobv.R
import com.emmm.mobv.WelcomeActivity
import com.emmm.mobv.databinding.LogoutFragmentBinding
import com.emmm.mobv.util.Injection

class LogoutFragment : Fragment() {

    private lateinit var logoutViewModel: LogoutViewModel
    private val mainBaseViewModel: MainBaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: LogoutFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.logout_fragment, container, false
        )

        logoutViewModel =
            ViewModelProvider(requireActivity(), Injection.provideViewModelFactory(requireContext()))
                .get(LogoutViewModel::class.java)

        logout(mainBaseViewModel.actualAccountId.value!!)

        return binding.root
    }

    private fun logout(actAccountId: String) {
        val editor: SharedPreferences.Editor =
            requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit()
        editor.remove("public_key").commit()

        logoutViewModel.logout(actAccountId)
        val intent = Intent(activity, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }
}