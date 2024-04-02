package com.joshi.mvvmwithjetpackcompose.model.db

import com.joshi.mvvmwithjetpackcompose.model.notes.DbNote
import com.joshi.mvvmwithjetpackcompose.model.notes.NoteDao
import kotlinx.coroutines.flow.Flow

class CollectionDbRepoImpl(private val characterDao: CharacterDao, private val noteDao: NoteDao) :
    CollectiondbRepo {

    override suspend fun getCharacterFromRepo(): Flow<List<DbCharacter>> =
        characterDao.getCharacters()

    override suspend fun getCharacterFromRepo(characterID: Int): Flow<DbCharacter> =
        characterDao.getCharacter(characterID)

    override suspend fun addCharacterFromRepo(character: DbCharacter) =
        characterDao.addCharacter(character)

    override suspend fun updateCharacterFromRepo(character: DbCharacter) =
        characterDao.updateCharacter(character)

    override suspend fun deleteCharacterFromRepo(character: DbCharacter) =
        characterDao.deleteCharacter(character)

    override suspend fun getAllNote(): Flow<List<DbNote>> =
        noteDao.getAllNotes()


    override suspend fun getNoteFromRepo(characterID: Int): Flow<List<DbNote>> =
        noteDao.getNotes(characterID)

    override suspend fun addNoteFromRepo(note: DbNote) =
        noteDao.addNotes(note)

    override suspend fun updateFromNote(note: DbNote) =
        noteDao.updateNote(note)

    override suspend fun deleteFromNote(note: DbNote) =
        noteDao.deleteNote(note)

    override suspend fun deleteAllNotes(character: DbCharacter) =
        noteDao.deleteAllNote(character.id)

}