package ru.mail.technotrack.ipfs

import android.content.Context
import android.os.Build
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okio.buffer
import okio.sink
import okio.source
import java.io.*


enum class Command(val value: String) {
    INIT("init"),
    START("daemon &")
}


object IPFSDaemon {

    private val LOG_TAG: String = "ru.mail.technotrack.ipfs.IPFSDaemon"

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

    fun start(context: Context): Boolean {
        REPOSITORY_FILE = File(context.filesDir, "repository")
        BINARY_FILE = File(context.filesDir, "binary")

        if (!BINARY_FILE.exists()) {
            download(context, BINARY_FILE)
        }
        val initProcess = execute(Command.INIT.value)
        initProcess.waitFor()
        process = execute(Command.START.value)
        return true
    }

    fun stop(): Boolean {
        process.destroy()
        return true
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

    private fun execute(instruction: String): Process {
        val command = "${BINARY_FILE.absolutePath} $instruction"
        val envp = arrayOf("$IPFS_PATH=${REPOSITORY_FILE.absoluteFile}")
        return Runtime.getRuntime().exec(command, envp)
    }
}
