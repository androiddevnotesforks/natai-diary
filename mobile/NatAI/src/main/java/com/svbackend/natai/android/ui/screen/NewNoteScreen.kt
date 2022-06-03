package com.svbackend.natai.android.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.svbackend.natai.android.R
import com.svbackend.natai.android.ui.NProgressBtn
import com.svbackend.natai.android.ui.NTextField
import com.svbackend.natai.android.ui.NTextarea
import com.svbackend.natai.android.viewmodel.NewNoteViewModel
import com.svbackend.natai.android.utils.throttleLatest
import kotlinx.coroutines.launch

@Composable
fun NewNoteScreen(
    vm: NewNoteViewModel = viewModel(),
    onSuccess: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val onTitleChange: (String) -> Unit = throttleLatest(
        intervalMs = 350L,
        coroutineScope = scope,
        destinationFunction = vm::saveTitle
    )

    val onContentChange: (String) -> Unit = throttleLatest(
        intervalMs = 350L,
        coroutineScope = scope,
        destinationFunction = vm::saveContent
    )

    fun addNote(): () -> Unit {
        if (vm.title.value.text.isEmpty() || vm.content.value.text.isEmpty()) {
            return {
                Toast
                    .makeText(context, "Title and content are required", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return {
            scope.launch {
                vm.addNote()
                onSuccess()
            }
        }

    }

    Column(
        Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.newNote),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )
        NTextField(
            value = vm.title.value,
            label = stringResource(R.string.noteTitle),
            onChange = {
                vm.title.value = it
                onTitleChange(it.text)
            }
        )
        NTextarea(
            value = vm.content.value,
            label = stringResource(R.string.noteContent),
            onChange = {
                vm.content.value = it
                onContentChange(it.text)
            }
        )
        if (vm.isLoading.value) {
            Button(onClick = {}) {
                NProgressBtn()
                Text(
                    text = stringResource(R.string.saving),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        } else {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(R.string.addNote),
                    )
                },
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        stringResource(R.string.addNote)
                    )
                },
                onClick = addNote()
            )

        }

    }
}