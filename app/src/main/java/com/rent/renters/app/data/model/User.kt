package com.rent.renters.app.data.model

import android.os.Parcelable
import com.stfalcon.chatkit.commons.models.IUser
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(private val id: String, private val name: String, private val avatar: String, val isOnline: Boolean) : IUser, Parcelable {

    override fun getId(): String {
        return id
    }

    override fun getName(): String {
        return name
    }

    override fun getAvatar(): String {
        return avatar
    }
}
