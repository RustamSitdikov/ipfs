package ru.mail.technotrack.ipfs

import android.content.Context
import android.os.Build
import okio.buffer
import okio.sink
import okio.source
import java.io.*


object IPFSDaemon {

    private val LOG_TAG: String = "ru.mail.technotrack.ipfs.IPFSDaemon"

    private const val INIT_COMMAND = "init"
    private const val START_COMMAND = "daemon"
    private const val IPFS_PATH = "IPFS_PATH"

    lateinit var REPOSITORY_FILE: File
    lateinit var BINARY_FILE: File
    private val ABI: String by lazy {
        val abi = Build.CPU_ABI
        when {
            abi.toLowerCase().startsWith("x86") -> "x86"
            abi.toLowerCase().startsWith("arm") -> "arm"
            else -> throw Exception("Unsupported architecture $ABI")
        }
    }
    lateinit var process: Process

    fun create(context: Context): String {
        REPOSITORY_FILE = File(context.filesDir, "repository")
        BINARY_FILE = File(context.filesDir, "binary")

        if (!BINARY_FILE.exists()) {
            download(context, BINARY_FILE)
        }
        return execute(INIT_COMMAND)
    }

    fun start(): String {
        return execute(START_COMMAND)
    }

    fun stop(): String {
        return process.destroy().toString()
    }

    private fun download(context: Context, file: File) {
        with(context) {
            val source = assets.open(ABI).source().buffer()
            val sink = file.sink().buffer()
            while (!source.exhausted()) {
                source.read(sink.buffer, 1024)
            }
            source.close()
            sink.close()
        }
        file.setExecutable(true)
    }

    private fun execute(command: String): String {
        process = run(command)
        val result = process.inputStream.bufferedReader().readText()
        process.waitFor()
        return result
    }

    private fun run(instruction: String): Process {
        val command = "${BINARY_FILE.absolutePath} $instruction"
//        val environment = arrayOf("$IPFS_PATH=${REPOSITORY_FILE.absoluteFile}")
//        return Runtime.getRuntime().exec(command, environment)
        val processBuilder = ProcessBuilder(command)
        return processBuilder.start()
    }
}
