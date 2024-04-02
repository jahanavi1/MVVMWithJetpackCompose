package com.joshi.mvvmwithjetpackcompose.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.joshi.mvvmwithjetpackcompose.AttributionText
import com.joshi.mvvmwithjetpackcompose.CharacterImage
import com.joshi.mvvmwithjetpackcompose.Destination
import com.joshi.mvvmwithjetpackcompose.model.CharactersApiResponse
import com.joshi.mvvmwithjetpackcompose.model.api.NetworkResult
import com.joshi.mvvmwithjetpackcompose.model.connectivity.ConnectivityObserver
import com.joshi.mvvmwithjetpackcompose.viewmodel.LibraryApiViewModel

@Composable
fun LibraryScreen(
    navController: NavHostController,
    vm: LibraryApiViewModel,
    paddingValues: PaddingValues
) {

    val result by vm.result.collectAsState()
    val text by vm.queryText.collectAsState()
    val networkAvailables =
        vm.networkAvialbel.observe().collectAsState(ConnectivityObserver.Status.Available)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (networkAvailables.value == ConnectivityObserver.Status.UnAvailable) {
            Row(
                modifier = Modifier
                    .background(Color.Red)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Network UnAvailable",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        OutlinedTextField(
            value = text,
            onValueChange = vm::onQueryUpdate,
            label = { Text(text = "Character Search") },
            placeholder = { Text(text = "Character") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (result) {
                is NetworkResult.Initial -> {
                    Text(text = "Search for a Character")
                }

                is NetworkResult.Success -> {
                    ShowCharacterList(
                        result as NetworkResult.Success<CharactersApiResponse>,
                        navController
                    )
                }

                is NetworkResult.Error -> {
                    Text(text = "Error: ${result.message}")
                }

                is NetworkResult.Loading -> CircularProgressIndicator()
                else -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun ShowCharacterList(
    result: NetworkResult.Success<CharactersApiResponse>,
    navController: NavHostController
) {
    result.data?.data?.results?.let { characterResults ->
        LazyColumn(
            modifier = Modifier.background(Color.Black),
            verticalArrangement = Arrangement.Top
        ) {
            result.data.attributioonText?.let {
                item {
                    AttributionText(text = it)
                }
            }

            items(characterResults) { characterResults ->
                val imageUrl =
                    characterResults.thumbnail?.path + "." + characterResults.thumbnail?.extension
                val title = characterResults.name
                val description = characterResults.description
                val context = LocalContext.current
                val id = characterResults.id

                Column(modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.White)
                    .padding(4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        if (characterResults.id != null) {
                            navController.navigate(Destination.CharacterDetails.createRoute(id))
                        } else {
                            Toast
                                .makeText(context, "character id is null", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CharacterImage(
                            url = imageUrl,
                            modifier = Modifier
                                .padding(4.dp)
                                .width(100.dp)
                        )

                        Column(modifier = Modifier.padding(4.dp)) {
                            Text(
                                text = title ?: "",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Text(
                        text = description ?: "",
                        color = Color.Black,
                        maxLines = 4,
                        fontSize = 14.sp
                    )
                }

            }
        }
    }
}
