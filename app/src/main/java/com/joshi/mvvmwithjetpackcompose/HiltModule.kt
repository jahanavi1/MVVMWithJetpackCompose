package com.joshi.mvvmwithjetpackcompose

import android.content.Context
import androidx.room.Room
import com.joshi.mvvmwithjetpackcompose.model.api.ApiService
import com.joshi.mvvmwithjetpackcompose.model.api.MarvelApiRepo
import com.joshi.mvvmwithjetpackcompose.model.connectivity.ConnectivityMonitor
import com.joshi.mvvmwithjetpackcompose.model.db.CharacterDao
import com.joshi.mvvmwithjetpackcompose.model.db.CollectionDb
import com.joshi.mvvmwithjetpackcompose.model.db.CollectionDbRepoImpl
import com.joshi.mvvmwithjetpackcompose.model.db.CollectiondbRepo
import com.joshi.mvvmwithjetpackcompose.model.db.Constants.DB
import com.joshi.mvvmwithjetpackcompose.model.notes.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)

    @Provides
    fun provideCollectionDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CollectionDb::class.java, DB).build()


    @Provides
    fun provideCharacterDao(collectionDb: CollectionDb) = collectionDb.characterDao()

    @Provides
    fun provideNoteDao(collectionDb: CollectionDb) = collectionDb.noteDao()

    @Provides
    fun provideDbRepoImpl(characterDao: CharacterDao, noteDao: NoteDao): CollectiondbRepo =
        CollectionDbRepoImpl(characterDao, noteDao)


    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        ConnectivityMonitor.getInstance(context)

}