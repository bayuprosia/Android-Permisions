package com.example.permisions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.ArraySet
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.permisions.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding ?= null
    private val takePhoto = registerForActivityResult(MyTakePicture(this)){
        binding?.ivPicture?.setImageBitmap(it)
    }
    private val permissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ it->
        val isCameraGranted: Boolean? = it[android.Manifest.permission.CAMERA]
        val isStorageGranted: Boolean? = it[android.Manifest.permission.READ_EXTERNAL_STORAGE]

//        if((isCameraGranted == true && isStorageGranted == true) || isCameraGranted == true ) {
//            takePhoto.launch(null)
//        }
        val arrOfPerm = HashSet<String>()
        if(isCameraGranted == true) {
            arrOfPerm.add(android.Manifest.permission.CAMERA)
        }
        if(isStorageGranted == true) {
            arrOfPerm.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (arrOfPerm.isEmpty())
            Toast.makeText(this, "Выдайте разрешения в настройках", Toast.LENGTH_SHORT).show()
        else
            takePhoto.launch(arrOfPerm)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        binding?.btnAddPicture?.setOnClickListener {
            permissions.launch(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}