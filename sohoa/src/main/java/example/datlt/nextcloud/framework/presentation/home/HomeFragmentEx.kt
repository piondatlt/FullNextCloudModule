package example.datlt.nextcloud.framework.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import example.datlt.nextcloud.BuildConfig
import example.datlt.nextcloud.R
import example.datlt.nextcloud.framework.presentation.crop.CropFragment
import example.datlt.nextcloud.util.getAllCreatedFile
import example.datlt.nextcloud.util.setPreventDoubleClick
import example.datlt.nextcloud.util.setPreventDoubleClickScaleView
import kotlin.system.exitProcess

fun HomeFragment.backEvent() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
}

fun HomeFragment.onBackPressed() {
    exitProcess(0)
}

fun HomeFragment.checkPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.isExternalStorageManager()
    } else {
        val isHaveRead = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val isHaveWrite = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        isHaveRead && isHaveWrite
    }
}

fun HomeFragment.requestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val intent = Intent(
            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
        )
        try {
            resultRequestManageStoragePermission.launch(intent)
        }catch (e : Exception){
            Toast.makeText(context , "Request permission error" , Toast.LENGTH_SHORT).show()
        }
    } else {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        requestPermissions(permissions, 100)
    }
}

fun HomeFragment.photoToPdfEvent(){
    binding.btnPhotoToPdf.setPreventDoubleClickScaleView {
        safeNav(R.id.homeFragment , R.id.action_homeFragment_to_selectImageFragment)
    }
}

fun HomeFragment.getAllCreatedFile(){
    binding.rcvConvertedFile.layoutManager = LinearLayoutManager(context)
    binding.rcvConvertedFile.adapter = adapter
    context?.let {
        if (checkPermission()){
            //get all create folder
            it.getAllCreatedFile().let {listCreated ->
                val newList = mutableListOf<String>()
                newList.addAll(listCreated)
                adapter.submitList(newList)
            }
        }
    }
}

fun HomeFragment.onClickItemCreated(pathFile : String){
    safeNav(R.id.homeFragment , HomeFragmentDirections.actionHomeFragmentToReadFileFragment(filePath = pathFile))
}

fun HomeFragment.cameraToPdfEvent(){
    binding.btnCameraToPdf.setPreventDoubleClickScaleView {
        if (checkCameraPermission()){
            safeNav(R.id.homeFragment , R.id.action_homeFragment_to_cameraFragment)
        }else{
            requestPermissions(arrayOf(Manifest.permission.CAMERA) , 100)
        }
    }
}

fun HomeFragment.checkCameraPermission() : Boolean{
    return ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}

fun HomeFragment.nextCloudEvent(){
    binding.btnGoToNextCloud.setPreventDoubleClickScaleView {
        safeNav(R.id.homeFragment , R.id.action_homeFragment_to_nextCloudFragment)
    }
}