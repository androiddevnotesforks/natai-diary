package com.svbackend.natai.android.ui.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.svbackend.natai.android.Route

@Composable
fun BottomBar(
    onNavigateTo: (String) -> Unit,
    addNote: () -> Unit
) {
    BottomAppBar(
        actions = {
            IconButton(
                onClick = { onNavigateTo(Route.MainRoute.withArgs()) },
                modifier = Modifier.fillMaxHeight(),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = "Home (All notes screen)",
                )
            }
            IconButton(
                onClick = { onNavigateTo(Route.AnalyticsRoute.withArgs()) },
                modifier = Modifier.fillMaxHeight(),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = "Insights (Analytics)",
                )
            }
            IconButton(
                onClick = { onNavigateTo(Route.SettingsRoute.withArgs()) },
                modifier = Modifier.fillMaxHeight(),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = "Settings",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addNote() },
            ) {
                Icon(Icons.Filled.Add, "Add Note")
            }
        }
    )
}