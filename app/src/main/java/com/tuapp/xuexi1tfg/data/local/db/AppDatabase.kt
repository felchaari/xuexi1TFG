package com.tuapp.xuexi1tfg.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tuapp.xuexi1tfg.data.local.dao.ExampleSentenceDao
import com.tuapp.xuexi1tfg.data.local.dao.HanziCharDao
import com.tuapp.xuexi1tfg.data.local.dao.RelatedWordDao
import com.tuapp.xuexi1tfg.data.local.entities.ExampleSentenceEntity
import com.tuapp.xuexi1tfg.data.local.entities.HanziCharEntity
import com.tuapp.xuexi1tfg.data.local.entities.RelatedWordEntity
import com.tuapp.xuexi1tfg.data.local.db.DatabasePrepopulate
import kotlinx.coroutines.CoroutineScope // Asegúrate de que este import esté presente
import kotlinx.coroutines.Dispatchers // Asegúrate de que este import esté presente
import kotlinx.coroutines.launch     // Asegúrate de que este import esté presente

@Database(
    entities = [HanziCharEntity::class, RelatedWordEntity::class, ExampleSentenceEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hanziCharDao(): HanziCharDao
    abstract fun relatedWordDao(): RelatedWordDao
    abstract fun exampleSentenceDao(): ExampleSentenceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val builder = Room.databaseBuilder( // CORREGIDO: Usar una variable builder
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chinese_data_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // CORREGIDO: Usamos INSTANCE?.let y lanzamos la coroutine aquí.
                            // DatabasePrepopulate.populateDatabase ahora es 'suspend'.
                            INSTANCE?.let { appDb ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    // Esta línea ahora debería funcionar sin errores de compilación
                                    DatabasePrepopulate.populateDatabase(context.applicationContext, appDb)
                                }
                            }
                        }
                    })

                val instance = builder.build() // CORREGIDO: Construir la instancia aquí
                INSTANCE = instance // Asignar al singleton
                instance // Devolver la instancia
            }
        }

        fun converters() = Converters()
    }
}