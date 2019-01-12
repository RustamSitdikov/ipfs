package ru.mail.technotrack.ipfs.database

import android.content.Context

class Model(context: Context) {

    private val room = ValuesDatabase.getAppDataBase(context)

    fun size(): Long {
        return getAllFiles().size.toLong()
    }

    fun editValue(file: FileInfo) {
        room?.valuesDao()?.updateFile(file)
    }

    fun getFiles(path: String): List<FileInfo> {
        return room?.valuesDao()?.getFilesByPath(path) ?: emptyList()
    }

    fun getAllFiles(): List<FileInfo> {
        return room?.valuesDao()?.getAllFiles() ?: emptyList()
    }

    fun deleteFilesByPath(path: String) {
        room?.valuesDao()?.deleteFilesInFolder(path)
    }

    //TODO add try catch
    fun updateFiles(folderPath: String, files: List<FileInfo>): List<FileInfo> {
        room?.valuesDao()?.deleteFilesInFolder(folderPath)
        room?.valuesDao()?.addFiles(files)
        return files
    }
}