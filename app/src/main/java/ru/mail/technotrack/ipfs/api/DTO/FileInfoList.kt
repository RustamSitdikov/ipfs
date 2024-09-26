package ru.mail.technotrack.ipfs.api.DTO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FileInfoList {
    @SerializedName("Entries")
    @Expose
    var entries: List<FileInfo>? = ArrayList()
}