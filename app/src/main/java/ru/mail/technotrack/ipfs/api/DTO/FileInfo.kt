package ru.mail.technotrack.ipfs.api.DTO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FileInfo(name: String, type: Int, size: Long, hash: String) : Serializable {
    @SerializedName("Name")
    @Expose
    var name: String? = name

    @SerializedName("Type")
    @Expose
    var type: Int? = type

    @SerializedName("Size")
    @Expose
    var size: Long? = size

    @SerializedName("Hash")
    @Expose
    var hash: String? = null
}