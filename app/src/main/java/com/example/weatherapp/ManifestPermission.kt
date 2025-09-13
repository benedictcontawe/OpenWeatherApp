package com.example.weatherapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

public object ManifestPermission {

    private val TAG = ManifestPermission::class.java.getSimpleName()

    const val SETTINGS_PERMISSION_CODE = 1000
    const val TELEPHONY_PERMISSION_CODE  = 1002
    const val CAMERA_PERMISSION_CODE = 1004
    const val LOCATION_PERMISSION_CODE = 1009
    const val NOTIFICATION_PERMISSION_CODE = 1010

    val cameraPermission = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_MEDIA_IMAGES
    )

    val locationPermission : Array<String> =
        arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

    val smsPermission : Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        arrayOf(
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_PHONE_NUMBERS
        )
    } else {
        arrayOf(
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.READ_PHONE_STATE
        )
    }

    val notificationPermission : String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.POST_NOTIFICATIONS
    } else null
    //region Read External Storage Methods
    private fun isReadExternalStorage(context : Context, permission : String) : Boolean {
        Log.d(TAG,"isReadExternalStorage($context,$permission")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && permission.contentEquals(android.Manifest.permission.READ_EXTERNAL_STORAGE, false)) {
            Environment.isExternalStorageManager() //Android is 11(R) or above
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && permission.contentEquals(android.Manifest.permission.READ_EXTERNAL_STORAGE, false)) {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED //Android is below 11(R)
        } else false
    }

    private fun isReadExternalStorage(context : Context, permissions : Array<String>) : Boolean {
        Log.d(TAG,"isReadExternalStorage($context,${permissions.contentToString()}")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && permissions.filter { permission -> permission.contentEquals(android.Manifest.permission.READ_EXTERNAL_STORAGE, false) }.isNotEmpty()) {
            Environment.isExternalStorageManager() //Android is 11(R) or above
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && permissions.filter { permission -> permission.contentEquals(android.Manifest.permission.READ_EXTERNAL_STORAGE, false) }.isNotEmpty()) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED //Android is below 11(R)
        } else false
    }

    private fun requestPermissionReadExternalStorage(activity : Activity, requestCode : Int) {
        Log.d(TAG, "requestPermissionReadExternalStorage($requestCode)")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //Android is 11(R) or above
            try {
                Log.d(TAG, "requestPermission: try")
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", activity.getPackageName(), null)
                intent.data = uri
                activity.startActivityForResult(intent, requestCode) //storageActivityResultLauncher.launch(intent)
            }
            catch (ex : Exception) {
                Log.e(TAG, "requestPermission: ", ex)
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                activity.startActivityForResult(intent, requestCode) //storageActivityResultLauncher.launch(intent)
            }
        }
        else { //Android is below 11(R)
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),requestCode)
        }
    }
    //endregion
    //region Write External Storage Methods
    private fun isWriteExternalStorage(context : Context, permission : String) : Boolean {
        Log.d(TAG,"isWriteExternalStorage($context,$permission")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && permission.contentEquals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
            Environment.isExternalStorageManager() //Android is 11(R) or above
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && permission.contentEquals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED //Android is below 11(R)
        } else false
    }

    private fun isWriteExternalStorage(context : Context, permissions : Array<String>) : Boolean {
        Log.d(TAG,"isWriteExternalStorage($context,${permissions.contentToString()}")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && permissions.filter { permission -> permission.contentEquals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, false) }.isNotEmpty()) {
            Environment.isExternalStorageManager() //Android is 11(R) or above
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && permissions.filter { permission -> permission.contentEquals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, false) }.isNotEmpty()) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED //Android is below 11(R)
        } else false
    }

    private fun requestPermissionWriteExternalStorage(activity : Activity, requestCode : Int) {
        Log.d(TAG, "requestPermissionReadExternalStorage($requestCode)")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //Android is 11(R) or above
            try {
                Log.d(TAG, "requestPermission: try")
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", activity.getPackageName(), null)
                intent.data = uri
                activity.startActivityForResult(intent, requestCode) //storageActivityResultLauncher.launch(intent)
            }
            catch (ex : Exception) {
                Log.e(TAG, "requestPermission: ", ex)
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                activity.startActivityForResult(intent, requestCode) //storageActivityResultLauncher.launch(intent)
            }
        }
        else { //Android is below 11(R)
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),requestCode)
        }
    }
    //endregion
    //region Check Self Permission Methods
    fun checkSelfPermission(context : Context, permission : String) : Boolean {
        Log.d(TAG,"checkSelfPermission($context,$permission, isGranted(), isDenied())")
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun checkSelfPermission(context : Context, permission : String, isGranted : () -> Unit = {}, isDenied : () -> Unit = {}) {
        Log.d(TAG,"checkSelfPermission($context,$permission, isGranted(), isDenied())")
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"isGranted()")
            isGranted()
        } else {
            Log.d(TAG,"denied()")
            isDenied()
        }
    }

    fun checkSelfPermission(context : Context, permissions : Array<String>, isGranted : () -> Unit = {}, isDenied : () -> Unit = {}) {
        Log.d(TAG,"checkSelfPermission($context,${permissions.contentToString()}, isGranted(), isDenied())")
        if (permissions.filter { permission -> ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED }.isEmpty()) {
            Log.d(TAG,"allGranted()")
            isGranted()
        } else {
            Log.d(TAG,"denied()")
            isDenied()
        }
    }
    //endregion
    //region Request Permissions Results Methods
    fun requestPermissions(permissionResultResultLauncher : ActivityResultLauncher<String>, permission : String) {
        Log.d(TAG,"requestPermission($permissionResultResultLauncher,$permission")
        permissionResultResultLauncher.launch(permission)
    }

    fun requestPermissions(permissionResultResultLauncher : ActivityResultLauncher<Array<String>>, permissions : Array<String>) {
        Log.d(TAG,"requestPermission($permissionResultResultLauncher,$permissions")
        permissionResultResultLauncher.launch(permissions)
    }

    fun requestPermissions(activity : Activity, permission : String, requestCode : Int) {
        Log.d(TAG,"requestPermissions($activity,$permission,$requestCode")
        ActivityCompat.requestPermissions(activity, arrayOf(permission),requestCode)
    }

    fun requestPermissions(activity : Activity, permissions : Array<String>, requestCode : Int) {
        Log.d(TAG,"requestPermissions($activity,${permissions.contentToString()},$requestCode")
        ActivityCompat.requestPermissions(activity, permissions,requestCode)
    }
    //endregion
    //region Check Never Ask Again Methods
    fun checkNeverAskAgain(activity : Activity, permission : String, isNeverAskAgain : () -> Unit = {}, isNotNeverAskAgain : () -> Unit = {}) {
        Log.d(TAG,"hasPermissions($activity,$permission,isNeverAskAgain(),isNotNeverAskAgain())")
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission).not() && ActivityCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_DENIED) {
            isNeverAskAgain()
        } else {
            isNotNeverAskAgain()
        }
    }

    fun checkNeverAskAgain(activity : Activity, permissions : Array<String>, isNeverAskAgain : () -> Unit = {}, isNotNeverAskAgain : () -> Unit = {}) {
        Log.d(TAG,"hasPermissions($activity,${permissions.contentToString()},isNeverAskAgain(),isNotNeverAskAgain())")
        if(permissions.filter { permission -> ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED }.none()) {
            Log.d(TAG,"isNeverAskAgain()")
            isNeverAskAgain()
        } else {
            Log.d(TAG,"isNotNeverAskAgain()")
            isNotNeverAskAgain()
        }
    }
    //endregion
    //region Check Permission Result Methods
    fun checkPermissionsResult(activity : Activity, permissions : Array<String>, grantResults : IntArray, isGranted : () -> Unit, isNeverAskAgain : () -> Unit = {}, isDenied : () -> Unit) {
        when {
            grantResults.all { results -> results ==  PackageManager.PERMISSION_GRANTED } -> {
                Log.d(TAG,"isGranted()")
                isGranted()
            }
            permissions.filter { permission -> ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED }.none() -> {
                Log.d(TAG,"isNeverAskAgain()")
                isNeverAskAgain()
            }
            grantResults.filter { results -> results ==  PackageManager.PERMISSION_DENIED }.isNotEmpty() -> {
                Log.d(TAG,"isDenied()")
                isDenied()
            }
        }
    }
    //endregion
    //region Rational Dialog Methods
    public fun showRationaleDialog(activity : Activity, message : String, activityResultLauncher : ActivityResultLauncher<Intent>) {
        Log.d(TAG,"showRationalDialog($activity,$message")
        val builder = activity.let { AlertDialog.Builder(it) }
        builder.setTitle("Manifest Permissions")
        builder.setMessage(message)
        builder.setPositiveButton("SETTINGS") { dialog, which ->
            dialog.dismiss()
            showAppPermissionSettings(activity, activityResultLauncher)
        }
        builder.setNegativeButton("NOT NOW") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
    //endregion
    //region App Permission Settings Methods
    private fun showAppPermissionSettings(activity : Activity, activityResultLauncher : ActivityResultLauncher<Intent>) {
        Log.d("PermissionsResult", "showAppPermissionSettings()")
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        activityResultLauncher.launch(intent)
    }
    //endregion
}