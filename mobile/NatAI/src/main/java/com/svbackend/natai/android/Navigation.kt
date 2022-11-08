package com.svbackend.natai.android

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.svbackend.natai.android.ui.screen.*
import com.svbackend.natai.android.ui.screen.auth.LoginScreen
import com.svbackend.natai.android.ui.screen.auth.ManageAccountScreen
import com.svbackend.natai.android.ui.screen.auth.RegistrationScreen
import com.svbackend.natai.android.ui.screen.settings.ThemesScreen
import com.svbackend.natai.android.utils.go
import com.svbackend.natai.android.viewmodel.EditNoteViewModel
import com.svbackend.natai.android.viewmodel.NoteViewModel


@Composable
fun Navigation(
    controller: NavHostController,
    vm: NoteViewModel,
    editNoteViewModel: EditNoteViewModel = viewModel(),
) {

    NavHost(navController = controller, startDestination = Route.MainRoute.route) {
        composable(route = Route.MainRoute.route) {
            MainScreen(
                vm = vm,
                onAddClick = {
                    controller.go(Route.NewNoteRoute.withArgs())
                },
                onLoginClick = {
                    controller.go(Route.LoginRoute.withArgs())
                },
                onNoteClick = {
                    vm.selectNote(it.id)
                    controller.go(Route.NoteDetailsRoute.withArgs(noteId = it.id))
                }
            )
        }

        composable(route = Route.NewNoteRoute.route) {
            NewNoteScreen(onSuccess = {
                controller.popBackStack()
            })
        }

        composable(
            route = Route.NoteDetailsRoute.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            NoteDetailsScreen(
                vm = vm,
                noteId = entry.arguments!!["noteId"] as String,
                onEditClick = {
                    editNoteViewModel.loadNote(it)
                    controller.go(Route.EditNoteRoute.withArgs(noteId = it))
                },
                onDeleteClick = {
                    vm.deleteNote(it)
                    controller.popBackStack()
                }
            )
        }

        composable(
            route = Route.EditNoteRoute.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                }
            )
        ) {
            EditNoteScreen(
                vm = editNoteViewModel,
                onSuccess = {
                    controller.popBackStack()
                }
            )
        }

        composable(route = Route.SettingsRoute.route) {
            SettingsScreen(
                onThemeClick = {
                    controller.go(Route.SettingsThemesRoute.withArgs())
                },
            )
        }

        composable(route = Route.AnalyticsRoute.route) {
            AnalyticsScreen(vm = vm)
        }

        composable(route = Route.LoginRoute.route) {
            LoginScreen(
                onLoginSuccess = {
                    controller.popBackStack(
                        Route.MainRoute.route,
                        inclusive = true
                    )
                },
                onClickCreateAccount = {
                    controller.go(Route.RegistrationRoute.withArgs())
                }
            )
        }

        composable(route = Route.RegistrationRoute.route) {
            RegistrationScreen(
                onRegistrationSuccess = {
                    controller.popBackStack(
                        Route.MainRoute.route,
                        inclusive = true
                    )
                },
                onClickLogin = {
                    controller.go(Route.LoginRoute.withArgs())
                }
            )
        }

        composable(route = Route.ManageAccountRoute.route) {
            ManageAccountScreen(
                vm = vm,
                onClickCreateAccount = {
                    controller.go(Route.RegistrationRoute.withArgs())
                }
            )
        }

        composable(route = Route.SettingsThemesRoute.route) {
            ThemesScreen(
                vm = vm,
                onThemeChanged = {
                    vm.changeTheme(it)
                }
            )
        }
    }
}