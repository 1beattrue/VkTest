package edu.mirea.onebeattrue.vktest.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.mirea.onebeattrue.vktest.data.local.model.VideoDbModel

@Database(
    entities = [VideoDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {

        private const val DB_NAME = "AppDatabase"
        private var INSTANCE: AppDatabase? = null
        private val LOCK: Any = Any()

        fun getInstance(context: Context): AppDatabase {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val database = Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = DB_NAME
                ).build()

                INSTANCE = database

                return database
            }
        }
    }
}