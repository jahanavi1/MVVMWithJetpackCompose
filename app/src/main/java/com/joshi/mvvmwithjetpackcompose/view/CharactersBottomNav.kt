package com.joshi.mvvmwithjetpackcompose.view

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.joshi.mvvmwithjetpackcompose.Destination
import com.joshi.mvvmwithjetpackcompose.R

@Composable
fun CharactersBottomNav(navController: NavController) {
    BottomNavigation(elevation = 5.dp) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestionation = navBackStackEntry.value?.destination

        val iconLibrary = painterResource(id = R.drawable.ic_lib)
        val iconCollection = painterResource(id = R.drawable.ic_collection)

        BottomNavigationItem(
            selected = currentDestionation?.route == Destination.Library.route,
            onClick = {
                navController.navigate(Destination.Library.route) {
                    popUpTo(Destination.Library.route)
                    launchSingleTop = true
                }
            },
            icon = { Icon(painter = iconLibrary, contentDescription = null) },
            label = { Text(text = Destination.Library.route) }
        )

        BottomNavigationItem(
            selected = currentDestionation?.route == Destination.Collection.route,
            onClick = {
                navController.navigate(Destination.Collection.route) {
                    launchSingleTop = true
                }
            },
            icon = { Icon(painter = iconCollection, contentDescription = null) },
            label = { Text(text = Destination.Collection.route) }
        )
    }
}