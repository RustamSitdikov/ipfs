package ru.mail.technotrack.ipfs.database.converters

import ru.mail.technotrack.ipfs.api.DTO.FileInfo as FileInfoDto
import ru.mail.technotrack.ipfs.database.FileInfo as FileInfoEntity

fun fromDtoToModelFileInfo(files: List<FileInfoDto>, path: String): List<FileInfoEntity> {
    return files.map { it ->
        FileInfoEntity(name = it.name!!, size = it.size!!, type = it.type!!, hash = it.hash!!, path = path)
    }
}

fun fromModelToDtoFileInfo(files: List<FileInfoEntity>): List<FileInfoDto> {
    return files.map {it ->
        FileInfoDto(it.name, it.type, it.size, it.hash)
    }
}