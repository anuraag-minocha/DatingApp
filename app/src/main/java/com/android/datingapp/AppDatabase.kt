package com.android.datingapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MemberData::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
}