package edu.cnm.deepdive.graffiti.session

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface SessionModule {

    @Binds
    @Singleton
    fun bindSessionManager(implementation: GraffitiSessionManager): SessionManager

}