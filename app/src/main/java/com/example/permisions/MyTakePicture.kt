package com.example.permisions

import android.app.Activity
import android.content.Context

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.ArraySet
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import kotlin.coroutines.coroutineContext


class MyTakePicture(con: Context) : ActivityResultContract<HashSet<String>?, Bitmap?>(){
    private val context = con
    override fun createIntent(context: Context, input: HashSet<String>?): Intent {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val galleryIntent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        var chooserIntent : Intent ?= null

        if((input?.size == 1) && input.contains(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            chooserIntent  = galleryIntent
        } else if ((input?.size == 1) && input.contains(android.Manifest.permission.CAMERA)) {
            chooserIntent = cameraIntent
        }
        else {
            chooserIntent = Intent.createChooser(galleryIntent, "Выбери приложение")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }
        return chooserIntent!!
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun parseResult(resultCode: Int, intent: Intent?): Bitmap? {
        if(intent?.data != null && resultCode == Activity.RESULT_OK) {
            val source = ImageDecoder.createSource(context.contentResolver, intent.data!!)
            return ImageDecoder.decodeBitmap(source);
        }
        return intent.takeIf { resultCode == Activity.RESULT_OK}?.getParcelableExtra("data")
    }
    override fun getSynchronousResult(
        context: Context,
        input: HashSet<String>?
    ): SynchronousResult<Bitmap?>? = null
}