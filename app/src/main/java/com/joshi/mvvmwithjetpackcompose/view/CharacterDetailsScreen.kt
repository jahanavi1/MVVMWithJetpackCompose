package com.joshi.mvvmwithjetpackcompose.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.joshi.mvvmwithjetpackcompose.CharacterImage
import com.joshi.mvvmwithjetpackcompose.Destination
import com.joshi.mvvmwithjetpackcompose.comicsToString
import com.joshi.mvvmwithjetpackcompose.viewmodel.CollectionDbViewModel
import com.joshi.mvvmwithjetpackcompose.viewmodel.LibraryApiViewModel

@Composable
fun CharacterDetailsScreen(
    navController: NavHostController,
    lvm: LibraryApiViewModel,
    collectionDbViewModel: CollectionDbViewModel,
    paddingValues: PaddingValues
) {

    val character = lvm.characterDetails.value
    val collection by collectionDbViewModel.collection.collectAsState()
    val inCollection = collection.map { it.apiId }.contains(character?.id)


    if (character == null) {
        navController.navigate(Destination.Library.route) {
            popUpTo(Destination.Library.route)
            launchSingleTop = true
        }
    }

    LaunchedEffect(key1 = Unit) {
        collectionDbViewModel.setCurrentCharacterId(character?.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .padding(bottom = paddingValues.calculateBottomPadding())
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageUrl = character?.thumbnail?.path + "." + character?.thumbnail?.extension
        val title = character?.name ?: "No Name"
        val description = character?.description ?: "No Description"
        val comics =
            character?.comics?.items?.mapNotNull { it.name }?.comicsToString() ?: "No comiecs"

        CharacterImage(
            url = imageUrl, modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.Black,
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = comics,
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.padding(4.dp)
        )

        Text(
            text = description,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(4.dp)
        )

        Button(onClick = {
            if (!inCollection && character != null)
                collectionDbViewModel.addCharacter(character)

        }, modifier = Modifier.padding(bottom = 20.dp)) {

            if (!inCollection) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text(text = "Add to Collection")
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Text(text = "Added")
                }
            }
        }

    }
}