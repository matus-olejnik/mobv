package com.emmm.mobv.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emmm.mobv.data.db.model.ContactItem
import com.emmm.mobv.data.db.model.UserAccountItem

@Database(
    entities = [ContactItem::class, UserAccountItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun appDao(): DbDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppRoomDatabase::class.java, "emmmMobv.db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}