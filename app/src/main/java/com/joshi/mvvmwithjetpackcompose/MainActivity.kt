package com.joshi.mvvmwithjetpackcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joshi.mvvmwithjetpackcompose.ui.theme.MVVMWithJetpackComposeTheme
import com.joshi.mvvmwithjetpackcompose.view.CharacterDetailsScreen
import com.joshi.mvvmwithjetpackcompose.view.CharactersBottomNav
import com.joshi.mvvmwithjetpackcompose.view.CollectionScreen
import com.joshi.mvvmwithjetpackcompose.view.LibraryScreen
import com.joshi.mvvmwithjetpackcompose.viewmodel.CollectionDbViewModel
import com.joshi.mvvmwithjetpackcompose.viewmodel.LibraryApiViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
    data object Library : Destination("library")
    data object Collection : Destination("collection")
    data object CharacterDetails : Destination("character/{characterId}") {
        fun createRoute(characterId: Int?) = "character/$characterId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val lvm by viewModels<LibraryApiViewModel>()
    private val cvm by viewModels<CollectionDbViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MVVMWithJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    CharacterScaffold(navController = navController, lvm, cvm)
                }
            }
        }
    }
}

@Composable
fun CharacterScaffold(
    navController: NavHostController,
    lvm: LibraryApiViewModel,
    cvm: CollectionDbViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val ctx = LocalContext.current
    Scaffold(scaffoldState = scaffoldState, bottomBar = {
        CharactersBottomNav(navController = navController)
    }) { paddingValues ->
        NavHost(navController = navController, startDestination = Destination.Library.route) {
            composable(Destination.Library.route) {
                LibraryScreen(navController, vm = lvm, paddingValues)
            }
            composable(Destination.Collection.route) {
                CollectionScreen(cvm,navController)
            }
            composable(Destination.CharacterDetails.route) {
                val id = it.arguments?.getString("characterId")?.toIntOrNull()
                if (id == null)
                    Toast.makeText(ctx, "Character id is required", Toast.LENGTH_SHORT).show()
                else
                    lvm.retrieveSingleCharacter(id)
                CharacterDetailsScreen(navController, lvm, cvm, paddingValues)
            }
        }
    }
}

