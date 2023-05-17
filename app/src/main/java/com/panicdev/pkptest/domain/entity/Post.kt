package com.panicdev.pkptest.domain.entity
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    var body: String,
    val id: Int,
    val title: String,
    val userId: Int
): Parcelable
