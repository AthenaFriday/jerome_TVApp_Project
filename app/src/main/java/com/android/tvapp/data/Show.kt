package com.android.tvapp.data

import java.io.Serializable

data class Show(
    val id: Int,
    val name: String,
    val summary: String?,
    val image: ShowImage?, // <--- This must be a separate class
    val premiered: String?
) : Serializable



