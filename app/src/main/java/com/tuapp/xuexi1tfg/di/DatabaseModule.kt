package com.tuapp.xuexi1tfg.di

import com.tuapp.xuexi1tfg.data.local.dao.ExampleSentenceDao
import com.tuapp.xuexi1tfg.data.local.dao.HanziCharDao
import com.tuapp.xuexi1tfg.data.local.dao.RelatedWordDao
import android.content.Context
import com.tuapp.xuexi1tfg.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Instala este módulo en el SingletonComponent, lo que significa que las dependencias serán singletons
object DatabaseModule {

    // Provee la instancia de AppDatabase como un Singleton
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context // Hilt inyectará el contexto de la aplicación aquí
    ): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    // Provee HanziCharDao
    @Provides
    fun provideHanziCharDao(database: AppDatabase): HanziCharDao {
        return database.hanziCharDao()
    }

    // Provee RelatedWordDao
    @Provides
    fun provideRelatedWordDao(database: AppDatabase): RelatedWordDao {
        return database.relatedWordDao()
    }

    // Provee ExampleSentenceDao
    @Provides
    fun provideExampleSentenceDao(database: AppDatabase): ExampleSentenceDao {
        return database.exampleSentenceDao()
    }
}