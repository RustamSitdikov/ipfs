package ru.mail.technotrack.ipfs.database

import android.content.Context
import androidx.room.*
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "files")
data class FileInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "size") val size: Long,
    @ColumnInfo(name = "path") val path: String = "/",
    @NotNull @ColumnInfo(name = "hash") val hash: String  // may be this will be used as id ?
) : Serializable

@Dao
interface ValuesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFile(value: FileInfo)

    @Update
    fun updateFile(value: FileInfo)

    @Delete
    fun deleteFile(value: FileInfo)

    @Query("SELECT * FROM files WHERE name == :name")
    fun getFileByName(name: String): List<FileInfo>

    @Query("SELECT * FROM files")
    fun getAllFiles(): List<FileInfo>

    @Query("SELECT * FROM files WHERE path == :path")
    fun getFilesByPath(path: String): List<FileInfo>

    @Query("SELECT Count(*) FROM files")
    fun size(): Long

    @Query("DELETE FROM files WHERE path LIKE :folderPath")
    fun deleteFilesInFolder(folderPath: String)

    @Query("DELETE FROM files WHERE path LIKE :folderPath + '\' + :folderName OR path == :folderPath")
    fun deleteFolder(folderPath: String, folderName: String)

    @Insert
    fun addFiles(files: List<FileInfo>)
}


@Database(entities = [FileInfo::class], version = 1)
abstract class ValuesDatabase : RoomDatabase() {
    abstract fun valuesDao(): ValuesDao

    companion object {
        var INSTANCE: ValuesDatabase? = null
        fun getAppDataBase(context: Context): ValuesDatabase? {
            if (INSTANCE == null) {
                synchronized(ValuesDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ValuesDatabase::class.java, "files")
                        .allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}