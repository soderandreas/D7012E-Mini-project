package com.example.bookminiproject

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookminiproject.model.Book
import com.example.bookminiproject.model.Work
import com.example.bookminiproject.ui.screens.BookAuthorScreen
import com.example.bookminiproject.ui.screens.BookDetailScreen
import com.example.bookminiproject.ui.screens.BookListScreen
import com.example.bookminiproject.ui.screens.BookSearchScreen
import com.example.bookminiproject.viewmodel.BooksDBViewModel

enum class BookDBScreen(@StringRes val title: Int) {
    List(title = R.string.book_main),
    Detail(title = R.string.book_detail),
    Author(title = R.string.book_author),
    Edition(title = R.string.book_edition),
    Search(title = R.string.book_search)
}

data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = BookDBScreen.List.name
            ),
            BottomNavigationItem(
                label = "Search",
                icon = Icons.Filled.Search,
                route = BookDBScreen.Search.name
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDBAppBar(
    currentScreen: BookDBScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BookDBAppBottomNavigation(
    navigateTo: (String) -> Unit
) {
    NavigationBar {
        BottomNavigationItem().bottomNavigationItems().forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                //selected = index == navigationSelectedItem,
                selected = true,
                label = {
                    Text(navigationItem.label)
                },
                icon = {
                    Icon(
                        navigationItem.icon,
                        contentDescription = navigationItem.label
                    )
                },
                onClick = {
                    navigateTo(navigationItem.route)
                }
            )

        }
    }
}

@Composable
fun BookDBApp(
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val booksDBViewModel: BooksDBViewModel = viewModel(factory = BooksDBViewModel.Factory)

    val currentScreen = BookDBScreen.valueOf(
        backStackEntry?.destination?.route ?: BookDBScreen.List.name
    )

    Scaffold(
        topBar = {
            BookDBAppBar(
                currentScreen = currentScreen,
                canNavigateBack =
                    navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            BookDBAppBottomNavigation(
                navigateTo = {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BookDBScreen.List.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = BookDBScreen.List.name) {
                BookListScreen(
                    booksDBViewModel = booksDBViewModel,
                    onBookListItemClicked = { work ->
                        booksDBViewModel.setSelectedWork(work)
                        navController.navigate(BookDBScreen.Detail.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            composable(route = BookDBScreen.Detail.name) {
                BookDetailScreen(
                    booksDBViewModel = booksDBViewModel,
                    onAuthorClick = { author ->
                        booksDBViewModel.setSelectedAuthor(author.key)
                        navController.navigate(BookDBScreen.Author.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            composable(route = BookDBScreen.Author.name) {
                BookAuthorScreen(
                    booksDBViewModel = booksDBViewModel,
                    onWorkClick = { work ->
                        booksDBViewModel.setSelectedWork(work)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            composable(route = BookDBScreen.Search.name) {
                BookSearchScreen(
                    booksDBViewModel,
                    onBookListItemClicked = { work ->
                        booksDBViewModel.setSelectedWork(work)
                        navController.navigate(BookDBScreen.Detail.name)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}