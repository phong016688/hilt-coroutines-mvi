package com.example.mvisamplecoroutines.ui.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mvisamplecoroutines.R
import com.example.mvisamplecoroutines.databinding.FragmentLoginBinding
import com.example.mvisamplecoroutines.ui.activity.main.MainActivity
import com.example.mvisamplecoroutines.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val binding by viewBindings(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigation()
        view.auth_button.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        handleEvents()
    }

    private fun handleEvents() {

    }
}