package com.pblcnm.notes.di

import com.pblcnm.notes.data.FileNotebook
import com.pblcnm.notes.data.FileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindFileRepository(
        fileRepository: FileNotebook
    ): FileRepository
}