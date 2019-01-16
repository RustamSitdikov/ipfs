package ru.mail.technotrack.ipfs.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Info(
    @SerializedName("ID") @Expose val peerId: String,
    @SerializedName("PublicKey") @Expose val publicKey: String,
    @SerializedName("Addresses") @Expose val addresses: List<String>,
    @SerializedName("AgentVersion") @Expose val agentVersion: String,
    @SerializedName("ProtocolVersion") @Expose val protocolVersion: String
)