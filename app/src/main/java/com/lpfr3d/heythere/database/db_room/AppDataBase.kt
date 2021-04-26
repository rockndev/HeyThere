package com.lpfr3d.heythere.database.db_room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lpfr3d.heythere.database.db_room.dao.MensagemDAO
import com.lpfr3d.heythere.database.db_room.model.MensagemEntidade

@Database(entities = [MensagemEntidade::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun mensagemDao() : MensagemDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabse(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }

}