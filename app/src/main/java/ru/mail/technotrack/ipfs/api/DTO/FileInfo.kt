package ru.mail.technotrack.ipfs.api.DTO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FileInfo {
    @SerializedName("Name")
    @Expose
    var name: String? = null

    @SerializedName("Type")
    @Expose
    var type: Int? = null

    @SerializedName("Size")
    @Expose
    var size: Long? = null

    @SerializedName("Hash")
    @Expose
    var hash: String? = null
}