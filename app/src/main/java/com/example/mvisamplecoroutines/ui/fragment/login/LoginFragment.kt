package com.example.mvisamplecoroutines.ui.fragment.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mvisamplecoroutines.R
import com.example.mvisamplecoroutines.databinding.FragmentLoginBinding
import com.example.mvisamplecoroutines.ui.activity.main.TeamBuildingActivity
import com.example.mvisamplecoroutines.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.asObservable
import javax.inject.Inject

@InternalCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()
    private val binding by viewBindings(FragmentLoginBinding::bind)
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? TeamBuildingActivity)?.hideBottomNavigation()
        observeLoginStates()
        handleEvents()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun observeLoginStates() {
        viewModel.viewStates()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                renderEmailEditText(it.emailErrorMessage)
                renderPasswordEditText(it.passwordErrorMessage)
                renderLoadingView(it.isLoading)
                renderSnackBar(it.snackBarMessage)
            }.addTo(compositeDisposable)
    }

    private fun renderSnackBar(errorMessage: String?) {
        if (errorMessage.isNullOrEmpty()) return
        Snackbar.make(binding.root, errorMessage, 1000).show()
    }

    private fun renderPasswordEditText(error: String?) {
        if (error.isNullOrEmpty()) {
            binding.passwordEditText.error = null
        } else {
            binding.passwordEditText.error = error
        }
    }

    private fun renderEmailEditText(error: String?) {
        if (error.isNullOrEmpty()) {
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
            .asObservable()
            .also { viewModel.processIntents(it).addTo(compositeDisposable) }
    }
}
