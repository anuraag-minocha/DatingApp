package com.android.datingapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemberDao {
    @Query("SELECT * FROM memberdata")
    fun getAll(): List<MemberData>

    @Insert
    fun insertAll(vararg users: MemberData)

    @Query("DELETE FROM memberdata")
    fun deleteAll()

}