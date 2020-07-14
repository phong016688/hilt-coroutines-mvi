package com.example.mvisamplecoroutines.ui.fragment.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvisamplecoroutines.R
import com.example.mvisamplecoroutines.databinding.FragmentLoginBinding
import com.example.mvisamplecoroutines.domain.entity.User
import com.example.mvisamplecoroutines.ui.activity.main.MainActivity
import com.example.mvisamplecoroutines.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

@InternalCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBindings(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigation()
        observeLoginStates()
        handleEvents()
    }

    private fun observeLoginStates() {
        viewModel.states()
            .onEach { state ->
                renderLoadingView(state.isLoading)
                renderEmailEditText(state.emailValidateErrorMessage)
                renderPasswordEditText(state.passwordValidateErrorMessage)
                renderSnackBar(state.errorMessage)
                handleGoToMainScreen(state.user)
            }.launchIn(lifecycleScope)
    }

    private fun handleGoToMainScreen(user: User?) {
        if (user != null) {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun renderSnackBar(errorMessage: String) {
        if (errorMessage.isEmpty()) return
        Snackbar.make(binding.root, errorMessage, 1000).show()
    }

    private fun renderPasswordEditText(error: String) {
        if (error.isEmpty()) {
            binding.passwordEditText.error = null
        } else {
            binding.passwordEditText.error = error
        }
    }

    private fun renderEmailEditText(error: String) {
        if (error.isEmpty()) {
            binding.emailEditText.error = null
        } else {
            binding.emailEditText.error = error
        }
    }

    private fun renderLoadingView(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingProgress.show()
        } else {
            binding.loadingProgress.gone()
        }
    }

    private fun handleEvents() {
            merge(
                binding.emailEditText
                    .onTextChangesEvents()
                    .debounce(500)
                    .map { LoginIntent.EnterEmailField(it.toString()) },
                binding.passwordEditText
                    .onTextChangesEvents()
                    .debounce(500)
                    .map { LoginIntent.EnterPasswordField(it.toString()) },
                binding.authButton
                    .clicks()
                    .debounce(500)
                    .map { binding.emailEditText.text?.toString() to binding.passwordEditText.text?.toString() }
                    .map { LoginIntent.SubmitLogin(it.first ?: "", it.second ?: "") }
            )
                .onStart { LoginIntent.Initial }
                .onEach { viewModel.processIntent(it) }
                .launchIn(lifecycleScope)
    }
}