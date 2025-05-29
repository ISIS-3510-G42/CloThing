package com.moviles.clothingapp.model

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReceiptRepository(private val context: Context) {

    fun saveReceiptToFile(receiptText: String): Result<String> {
        return try {
            // Generate timestamped filename
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "receipt_$timeStamp.txt"

            // Use app-specific external documents directory
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

            if (dir == null || (!dir.exists() && !dir.mkdirs())) {
                return Result.failure(IOException("Failed to access or create directory"))
            }

            val file = File(dir, fileName)
            file.writeText(receiptText)

            Result.success(file.absolutePath)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    fun getExternalFilesDir(): String {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        return dir
    }
}
