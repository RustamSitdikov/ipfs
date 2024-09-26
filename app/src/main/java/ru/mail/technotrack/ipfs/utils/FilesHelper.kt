package ru.mail.technotrack.ipfs.utils

val FOLDER = "Folder"
val FILE = "File"

fun getTypeFile(type: Int): String {
    return when(type) {
        1 -> FOLDER
        else -> FILE
    }
}