package com.joshi.mvvmwithjetpackcompose.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshi.mvvmwithjetpackcompose.model.notes.DbNote
import com.joshi.mvvmwithjetpackcompose.model.notes.NoteDao


@Database(entities = [DbCharacter::class, DbNote::class], version = 2, exportSchema = false)
abstract  class  CollectionDb : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun noteDao() : NoteDao


}