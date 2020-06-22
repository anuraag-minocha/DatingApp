package com.android.datingapp

import com.google.gson.annotations.SerializedName

data class MemberParent(
    @SerializedName("user")
    var member: Member
)

data class Member(
    @SerializedName("name")
    var name: Name,
    @SerializedName("dob")
    var dob: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("cell")
    var mobile: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("picture")
    var picture: String,
    @SerializedName("location")
    var location: Location
)

data class Name(

    @SerializedName("first")
    var first: String,

    @SerializedName("last")
    var last: String
)



data class Location(

    @SerializedName("city")
    var city: String,

    @SerializedName("state")
    var state: String
)

