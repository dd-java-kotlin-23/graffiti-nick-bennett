package edu.cnm.deepdive.graffiti.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAuthRepository(implementation: GoogleAuthRepository): AuthRepository

    @Binds
    @Singleton
    fun bindTokenParser(implementation: GoogleTokenParser): TokenParser
    
}