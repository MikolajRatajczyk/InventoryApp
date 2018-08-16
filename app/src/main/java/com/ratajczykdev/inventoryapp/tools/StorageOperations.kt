package com.ratajczykdev.inventoryapp.tools

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
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
            //  use: execute block function and closes resource down correctly
            //  whether an exception is thrown or not
            stream.use {
                //  write String to file
                it.write(fileContent.toByteArray())
            }
        }
    }

    /**
     * Creates empty file
     * @return [File] containing path to empty file, if failed returns non-existing path file
     */
    fun createEmptyFile(directoryPath: File, fileName: String): File {
        return if (directoryPath.exists()) {
            val filePath = File(directoryPath, fileName)
            filePath.createNewFile()
            filePath
        } else {
            File("")
        }

    }

    /**
     * Writes [String] to PDF file
     */
    fun writeToPdfFile(directoryPath: File, pdfFileName: String, contentText: String) {
        val document = Document()
        val pdfFile = StorageOperations.createEmptyFile(directoryPath, "$pdfFileName.pdf")
        PdfWriter.getInstance(document, FileOutputStream(pdfFile))
        document.open()
        document.add(Paragraph(contentText))
        document.close()
    }
}