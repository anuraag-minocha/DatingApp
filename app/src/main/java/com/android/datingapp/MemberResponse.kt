package com.android.datingapp

import com.android.datingapp.Member
import com.google.gson.annotations.SerializedName

data class MemberResponse(
    @SerializedName("results")
    var members: ArrayList<MemberParent> = ArrayList()
)