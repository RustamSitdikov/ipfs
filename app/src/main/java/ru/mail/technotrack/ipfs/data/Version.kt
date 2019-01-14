package ru.mail.technotrack.ipfs.data

import com.squareup.moshi.Json

class Version(@Json(name = "version") val value: String)