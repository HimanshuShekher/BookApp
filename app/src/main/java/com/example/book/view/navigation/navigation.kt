package com.example.book.view.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.book.view.LoginScreen
import com.example.book.view.SignupScreen
import androidx.wear.compose.material.Text
import com.example.book.view.BookListScreen
import com.example.book.viewmodel.BookListViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object BookList : Screen("book_list")
}
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.BookList.route) {

                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(onSignupSuccess = {
                navController.navigate(Screen.Login.route) {

                    popUpTo(Screen.Signup.route) { inclusive = true }
                }
            })
        }

        composable(Screen.BookList.route) {
            BookListScreen(
                viewModel = hiltViewModel(),
                onBookClick = { book ->

                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {

                        popUpTo(Screen.BookList.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
