package com.joshi.mvvmwithjetpackcompose.model.db

import com.joshi.mvvmwithjetpackcompose.model.notes.DbNote
import kotlinx.coroutines.flow.Flow

interface CollectiondbRepo {
    suspend fun getCharacterFromRepo(): Flow<List<DbCharacter>>

    suspend fun getCharacterFromRepo(characterID: Int): Flow<DbCharacter>

    suspend fun addCharacterFromRepo(character: DbCharacter)

    suspend fun updateCharacterFromRepo(character: DbCharacter)

    suspend fun deleteCharacterFromRepo(character: DbCharacter)

    suspend fun getAllNote(): Flow<List<DbNote>>

    suspend fun getNoteFromRepo(characterID: Int): Flow<List<DbNote>>

    suspend fun addNoteFromRepo(note: DbNote)

    suspend fun updateFromNote(note: DbNote)

    suspend fun deleteFromNote(note: DbNote)

    suspend fun deleteAllNotes(character: DbCharacter)
}