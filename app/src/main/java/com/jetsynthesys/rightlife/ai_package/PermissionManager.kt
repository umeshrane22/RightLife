package com.jetsynthesys.rightlife.ai_package

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager(
    private val activity: Activity,
    private val launcher: ActivityResultLauncher<Array<String>>,
    private val onPermissionGranted: () -> Unit,
    private val onPermissionDenied: () -> Unit
) {

    companion object {
        private val cameraAndGalleryPermissions: Array<String>
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
    }

    fun checkAndRequestPermissions() {
        val deniedPermissions = cameraAndGalleryPermissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            // All permissions are already granted
            onPermissionGranted()
        } else {
            // Request only the missing ones
            launcher.launch(deniedPermissions.toTypedArray())
        }
    }

    fun handlePermissionResult(result: Map<String, Boolean>) {
        val allGranted = result.all { it.value }

        if (allGranted) {
            onPermissionGranted()
        } else {
            val permanentlyDenied = result.any { (perm, granted) ->
                !granted && !ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)
            }

            if (permanentlyDenied) {
                showGoToSettingsDialog()
            } else {
                onPermissionDenied()
            }
        }
    }

    private fun showGoToSettingsDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Permissions Required")
            .setMessage("Camera and storage permissions are permanently denied. Please enable them in App Settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
                activity.startActivity(intent)
            }
            .setNegativeButton("Cancel") { _, _ ->
                onPermissionDenied()
            }
            .show()
    }
}
