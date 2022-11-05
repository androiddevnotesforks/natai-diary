package com.svbackend.natai.android.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.svbackend.natai.android.R
import com.svbackend.natai.android.ui.NPasswordField
import com.svbackend.natai.android.ui.NPrimaryButton
import com.svbackend.natai.android.ui.NTextField
import com.svbackend.natai.android.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    vm: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onClickCreateAccount: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun onLogin(): () -> Unit {
        if (vm.email.value.text.isEmpty() || vm.password.value.text.isEmpty()) {
            return {
                Toast
                    .makeText(context, "Title and content are required", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return {
            vm.isLoading.value = true
            scope.launch {
                // todo try to login
                onLoginSuccess()
            }
        }

    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        // title - Login
        // 2 fields - email & password
        // 2 buttons - login & text-like link to "Create an account"
        Column(
            Modifier
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.login),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )

            NTextField(
                value = vm.email.value,
                label = stringResource(R.string.email),
                onChange = {
                    vm.email.value = it
                }
            )

            NPasswordField(
                value = vm.password.value,
                label = stringResource(R.string.password),
                onChange = {
                    vm.password.value = it
                })

            NPrimaryButton(
                onClick = onLogin(),
                isLoading = vm.isLoading.value,
            ) {
                Icon(
                    Icons.Filled.AccountCircle,
                    stringResource(R.string.login)
                )
                Text(
                    text = stringResource(R.string.login),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

    }
}