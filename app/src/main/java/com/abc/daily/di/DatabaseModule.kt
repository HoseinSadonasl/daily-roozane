package com.abc.daily.di

import android.content.Context
import androidx.room.Room
import com.abc.daily.data.local.db.NoteDatabase
import com.abc.daily.data.repository.NoteRepositoryImpl
import com.abc.daily.domain.repository.NoteRepository
import com.abc.daily.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        NoteDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideNoteRepository(database: NoteDatabase): NoteRepository =
        NoteRepositoryImpl(database.noteDao)

    @Provides
    @Singleton
    fun provideDomainUseCases(repository: NoteRepository): NotesDomain =
        NotesDomain(
            saveNote = SaveNote(repository),
            getNote = GetNote(repository),
            deleteNote = DeleteNote(repository),
            getNotesList = GetNotesList(repository),
        )

}