package com.svbackend.natai.android.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.svbackend.natai.android.R
import com.svbackend.natai.android.entity.Note
import com.svbackend.natai.android.utils.LocalDateTimeFormatter
import com.svbackend.natai.android.viewmodel.NoteViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteDetailsScreen(
    vm: NoteViewModel,
    noteId: String, // here just to have an example of arguments
    onEditClick: (String) -> Unit,
    onDeleteClick: (Note) -> Unit,
) {
    val note = vm.selectedNote.collectAsState(initial = null).value

    val context = LocalContext.current

    if (note == null) {
        LoadingScreen()
        return
    }

    Column(
        Modifier.padding(16.dp)
    ) {
        SelectionContainer {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )
        }

        Text(
            text = LocalDateTimeFormatter.fullDateTime.format(note.createdAt),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )

        SelectionContainer {
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )
        }

        // Space between
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(R.string.editNote),
                    )
                },
                icon = {
                    Icon(
                        Icons.Filled.Edit,
                        stringResource(R.string.editNote)
                    )
                },
                onClick = { onEditClick(noteId) }
            )


            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(R.string.deleteNote),
                    )
                },
                icon = {
                    Icon(
                        Icons.Filled.Delete,
                        stringResource(R.string.deleteNote)
                    )
                },
                onClick = {
                    Toast
                        .makeText(context, "Note deleted!", Toast.LENGTH_SHORT)
                        .show()
                    onDeleteClick(note)
                }
            )
        }
    }
}