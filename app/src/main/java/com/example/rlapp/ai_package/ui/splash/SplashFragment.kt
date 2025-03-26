package com.example.rlapp.ai_package.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rlapp.R
import com.example.rlapp.ai_package.base.BaseFragment
import com.example.rlapp.ai_package.utils.AppPreference
import com.example.rlapp.databinding.FragmentSplashBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    private lateinit var appPreference: AppPreference
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appPreference = AppPreference(requireContext())
        //  val loggedIn = appPreference.isLoggedIn()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val loggedIn = appPreference.isLoggedIn()
        if (loggedIn == true) {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homefragment)
            }, SPLASH_DELAY)
        } else {
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_homefragment)
            }, SPLASH_DELAY)
        }

        /*
                Handler().postDelayed({
                   findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }, SPLASH_DELAY)
        */

    }

    companion object {
        private const val SPLASH_DELAY = 2000L
    }
}