package com.example.mvisamplecoroutines.ui.fragment.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvisamplecoroutines.R
import com.example.mvisamplecoroutines.databinding.FragmentLoginBinding
import com.example.mvisamplecoroutines.ui.activity.main.MainActivity
import com.example.mvisamplecoroutines.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.internal.InjectedFieldSignature
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

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
                buildLoadingView(state.isLoading)
                buildEmailEditText(state.emailValidateErrorMessage)
                buildPasswordEditText(state.passwordValidateErrorMessage)
                buildSnackBar(state.errorMessage)
                handleGoToMainScreen(state.userId)
            }
            .launchIn(lifecycleScope)
    }

    private fun handleGoToMainScreen(userId: String) {
        if (userId.isNotEmpty()) {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun buildSnackBar(errorMessage: String) {
        Snackbar.make(binding.root, errorMessage, 1000).show()
    }

    private fun buildPasswordEditText(error: String) {
        binding.passwordEditText.error = error
    }

    private fun buildEmailEditText(error: String) {
        binding.emailEditText.error = error
    }

    private fun buildLoadingView(isLoading: Boolean) {
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
                .map { LoginIntent.EnterEmailField(it.toString()) },
            binding.authButton
                .clicks()
                .debounce(500)
                .map { binding.emailEditText.text?.toString() to binding.passwordEditText.text?.toString() }
                .map { LoginIntent.SubmitLogin(it.first ?: "", it.second ?: "") }
        )
            .onStart { viewModel.processIntents(LoginIntent.InitialIntent) }
            .onEach { viewModel.processIntents(it) }
            .launchIn(lifecycleScope)
    }
}