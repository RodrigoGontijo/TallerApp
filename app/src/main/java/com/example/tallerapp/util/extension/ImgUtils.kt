package com.example.tallerapp.util.extension

import android.content.Context
import android.content.Intent

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object ImgUtils {
    //Save the file to the specified path
    fun saveImageToGallery(context: Context, bmp: Bitmap): Boolean {
        // First save the picture
        val storePath: String = Environment.getExternalStorageDirectory()
            .absolutePath + File.separator.toString() + "dearxy"
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            //Compress and save pictures by io stream
            val isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos)
            fos.flush()
            fos.close()

            //Insert files into the system Gallery
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //Update the database by sending broadcast notifications after saving pictures
            val uri: Uri = Uri.fromFile(file)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            Toast.makeText(context, "Saved to gallery", Toast.LENGTH_SHORT).show()
            return isSuccess
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }
}