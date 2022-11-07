package com.svbackend.natai.android.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
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
import com.svbackend.natai.android.http.exception.LoginErrorException
import com.svbackend.natai.android.http.exception.RegistrationErrorException
import com.svbackend.natai.android.ui.NPasswordField
import com.svbackend.natai.android.ui.NPrimaryButton
import com.svbackend.natai.android.ui.NTextField
import com.svbackend.natai.android.viewmodel.LoginViewModel
import com.svbackend.natai.android.viewmodel.RegistrationViewModel
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    vm: RegistrationViewModel = viewModel(),
    onRegistrationSuccess: () -> Unit,
    onClickLogin: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun onRegister(): () -> Unit {
        if (vm.name.value.text.isEmpty() || vm.email.value.text.isEmpty() || vm.password.value.text.isEmpty()) {
            return {
                Toast
                    .makeText(context, "Email, name and password are required", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return {
            vm.isLoading.value = true
            scope.launch {

                try {
                    vm.register()
                    onRegistrationSuccess()
                } catch (e: RegistrationErrorException) {
                    Toast
                        .makeText(context, e.message, Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }

    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
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

            NTextField(
                value = vm.name.value,
                label = stringResource(R.string.name),
                onChange = {
                    vm.name.value = it
                }
            )

            NPasswordField(
                value = vm.password.value,
                label = stringResource(R.string.password),
                onChange = {
                    vm.password.value = it
                })

            NPrimaryButton(
                onClick = onRegister(),
                isLoading = vm.isLoading.value,
            ) {
                Icon(
                    Icons.Filled.Person,
                    stringResource(R.string.register)
                )
                Text(
                    text = stringResource(R.string.register),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

    }
}
