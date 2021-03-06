package com.maho_ya.di

import com.maho_ya.data.releasenotes.DataReleaseNotesDataSource
import com.maho_ya.data.releasenotes.DataReleaseNotesRepository
import com.maho_ya.data.releasenotes.ReleaseNotesDataSource
import com.maho_ya.data.releasenotes.ReleaseNotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal abstract class ReleaseNotesModule {

    @Singleton
    @Binds
    abstract fun provideReleaseNoteRepository(
        dataReleaseNotesRepository: DataReleaseNotesRepository
    ): ReleaseNotesRepository

    @Binds
    abstract fun provideReleaseNoteDataSource(
        dataReleaseNotesDataSource: DataReleaseNotesDataSource
    ): ReleaseNotesDataSource
}
