package com.pblcnm.notes.di

import com.pblcnm.notes.data.NoteRepository
import com.pblcnm.notes.data.local.LocalDataSource
import com.pblcnm.notes.data.local.file.FileNotebook
import com.pblcnm.notes.data.remote.RemoteDataSource
import com.pblcnm.notes.data.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(): LocalDataSource {
        return FileNotebook()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
    ): NoteRepository {
        return NoteRepository(localDataSource, remoteDataSource)
    }
}