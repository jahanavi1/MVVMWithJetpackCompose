package com.joshi.mvvmwithjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshi.mvvmwithjetpackcompose.model.CharacterResult
import com.joshi.mvvmwithjetpackcompose.model.db.CollectiondbRepo
import com.joshi.mvvmwithjetpackcompose.model.db.DbCharacter
import com.joshi.mvvmwithjetpackcompose.model.notes.DbNote
import com.joshi.mvvmwithjetpackcompose.model.notes.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDbViewModel @Inject constructor(private val repo: CollectiondbRepo) :
    ViewModel() {
    val currentCharacter = MutableStateFlow<DbCharacter?>(null)
    val collection = MutableStateFlow<List<DbCharacter>>(listOf())
    val notes = MutableStateFlow<List<DbNote>>(listOf())

    init {
        getCollection()
        getNotes()
    }

     fun getNotes() {
        viewModelScope.launch {
            repo.getAllNote().collect {
                notes.value = it
            }
        }
    }

     fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addNoteFromRepo(DbNote.fromNote(note))
        }
    }

     fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateFromNote(DbNote.fromNote(note))
        }
    }

     fun deleteNote(note: DbNote) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFromNote(note)
        }
    }

    private fun getCollection() {
        viewModelScope.launch {
            repo.getCharacterFromRepo().collect {
                collection.value = it
            }
        }
    }

    fun setCurrentCharacterId(characterId: Int?) {
        characterId?.let {
            viewModelScope.launch {
                repo.getCharacterFromRepo(it).collect {
                    currentCharacter.value = it
                }
            }
        }
    }

    fun addCharacter(character: CharacterResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addCharacterFromRepo(DbCharacter.fromCharacter(character))
        }
    }

    fun updateCharacter(character: CharacterResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateCharacterFromRepo(DbCharacter.fromCharacter(character))
        }
    }

    fun deleteCharacter(character: DbCharacter) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllNotes(character)
            repo.deleteCharacterFromRepo(character)
        }
    }
}