package com.joshi.mvvmwithjetpackcompose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.joshi.mvvmwithjetpackcompose.CharacterImage
import com.joshi.mvvmwithjetpackcompose.model.notes.DbNote
import com.joshi.mvvmwithjetpackcompose.model.notes.Note
import com.joshi.mvvmwithjetpackcompose.ui.theme.GrayBackground
import com.joshi.mvvmwithjetpackcompose.ui.theme.GrayTransparentBackground
import com.joshi.mvvmwithjetpackcompose.viewmodel.CollectionDbViewModel

@Composable
fun CollectionScreen(cvm: CollectionDbViewModel, navController: NavHostController) {

    val charactersInCollection = cvm.collection.collectAsState()
    val expandedElement = remember {
        mutableStateOf(-1)
    }
    val notes = cvm.notes.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(charactersInCollection.value) { character ->
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(4.dp)
                    .clickable {
                        if (expandedElement.value == character.id) {
                            expandedElement.value = -1
                        } else {
                            expandedElement.value = character.id
                        }
                    }) {
                    CharacterImage(
                        url = character.thumbnail.toString(),
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxHeight(),
                        contentScale = ContentScale.FillHeight
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .fillMaxHeight()
                    ) {
                        Text(
                            text = character.name ?: "No Name",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 22.sp,
                            maxLines = 2
                        )
                        Text(
                            text = character.comics ?: "No Comics",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 22.sp,
                            maxLines = 2
                        )
                        Text(
                            text = character.description ?: "No Description",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 22.sp,
                            maxLines = 2
                        )
                    }

                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(4.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                cvm.deleteCharacter(character)
                            })

                        if (character.id == expandedElement.value) {
                            Icon(Icons.Outlined.KeyboardArrowUp, contentDescription = null)
                        } else {
                            Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null)
                        }
                    }
                }
            }

            if (character.id == expandedElement.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .background(GrayTransparentBackground)
                ) {

                    val filteredNotes = notes.value.filter { notes ->
                        notes.characterId == character.id
                    }

                    NotesList(filteredNotes, cvm)
                    CreateNoteForm(character.id, cvm)
                }
            }

            Divider(
                color = Color.LightGray,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 20.dp, end = 20.dp)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteForm(id: Int, cvm: CollectionDbViewModel) {
    val addNoteToElement = remember {
        mutableStateOf(-1)
    }
    val newNoteTitle = remember {
        mutableStateOf("")
    }
    val newNoteText = remember {
        mutableStateOf("")
    }

    if (addNoteToElement.value == id) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .background(GrayBackground)
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(text = "Add note", fontWeight = FontWeight.Bold, color = Color.Black)
            OutlinedTextField(
                value = newNoteTitle.value,
                onValueChange = { newNoteTitle.value = it },
                label = { Text(text = "Note Title") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(color = Color.Black),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newNoteText.value,
                    onValueChange = { newNoteText.value = it },
                    label = { Text(text = "Note Content") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    textStyle = TextStyle(color = Color.Black),
                )

                Button(onClick = {
                    val note = Note(id, newNoteTitle.value, newNoteText.value)
                    cvm.addNote(note)
                    newNoteTitle.value = ""
                    newNoteText.value = ""
                    addNoteToElement.value = -1
                }) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        }
    }

    Button(onClick = { addNoteToElement.value = id }) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun NotesList(notes: List<DbNote>, cvm: CollectionDbViewModel) {
    for (note in notes) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GrayBackground)
                .padding(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .padding(4.dp)
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 13.sp
                )
                Text(
                    text = note.text,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 13.sp
                )

            }

            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.clickable {
                cvm.deleteNote(note)
            })
        }
    }
}


