package com.ratajczykdev.inventoryapp.tools

import android.content.Context
import java.io.File
import java.io.FileOutputStream

/**
 * Helper class that handles storage operations e.g. writing to files, creating directories
 */
object StorageOperations {

    /**
     * Create directory in primary shared/external storage device (app folder)
     *
     * @param directoryName name for new directory
     * @param context context
     * @return [File] object representing newly created or already existing directory,
     * if failed returns non-existing directory
     */
    fun createDirInExternal(directoryName: String, context: Context?): File {
        //  type of files directory - null for the root of the files directory
        val TYPE_FOR_ROOT_DIRECTORY = null
        //  get external storage path
        val externalPath = context?.getExternalFilesDir(TYPE_FOR_ROOT_DIRECTORY)

        val newDirectoryPath = File(externalPath, directoryName)
        if (!newDirectoryPath.exists()) {
            if (!newDirectoryPath.mkdirs()) {
                val nonExistingDirectory = File("")
                return nonExistingDirectory
            }
        }
        return newDirectoryPath
    }

    /**
     * Writes [String] to file
     */
    fun writeStringToFile(directoryPath: File, fileName: String, fileContent: String) {
        if (directoryPath.exists()) {
            //  create file
            val file = File(directoryPath, fileName)
            //  create stream for file
            val stream = FileOutputStream(file)
            //  write String to file
            stream.use {
                it.write(fileContent.toByteArray())
            }
        }
    }
}