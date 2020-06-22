package com.android.datingapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemberData(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "dob") val dob: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "mobile") val mobile: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "picture") var picture: String
)