package com.bigtime.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.*



object FileUtils {

    fun createTempImageFile(context: Context): File {
        return createTempFile(context, AppConstants.TEMP_PHOTO_FILE)
    }



    private fun createTempFile(context: Context, fileName: String): File {

        lateinit var fileTemp: File

        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            fileTemp = File(context.getExternalFilesDir(null), fileName)
        } else {
            fileTemp = File(context.filesDir, fileName)
        }

        if (!fileTemp.exists()) {
            try {
                fileTemp.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return fileTemp
    }


    fun overwriteFile(sourceuri: Uri, des: File) {
        val sourceFilename = sourceuri.path
        //String destinationFilename = previewFilePath;

        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null

        try {
            bis = BufferedInputStream(FileInputStream(sourceFilename))
            bos = BufferedOutputStream(FileOutputStream(des, false))
            val buf = ByteArray(1024)
            bis.read(buf)
            do {
                bos.write(buf)
            } while (bis.read(buf) != -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }
}
