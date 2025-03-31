package com.example.rlapp.ai_package.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rlapp.R
import com.example.rlapp.ai_package.base.BaseActivity
import com.example.rlapp.ai_package.ui.home.HomeBottomTabFragment
import com.example.rlapp.databinding.ActivityMainAiBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAIActivity : BaseActivity() {

    private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }

    private val PERMISSION_REQUEST_CODE = 100

    lateinit var bi: ActivityMainAiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = ActivityMainAiBinding.inflate(layoutInflater)
        setContentView(bi.root)

        val moduleName = intent.getStringExtra("ModuleName")
        supportFragmentManager.beginTransaction().apply {
            val homeBottomTabFragment = HomeBottomTabFragment()
            val args = Bundle()
            args.putString("ModuleName", moduleName)
            homeBottomTabFragment.arguments = args
            replace(R.id.flFragment, homeBottomTabFragment, "homeBottom")
            addToBackStack(null)
            commit()
        }

        // Request all permissions at once
        if (!hasAllPermissions()) {
            requestAllPermissions()
        }
    }

    // Check if all required permissions are granted
    private fun hasAllPermissions(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Request all permissions in one go
    private fun requestAllPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
    }

    // Handle permissions result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            val deniedPermissions = permissions.zip(grantResults.toTypedArray())
                .filter { it.second != PackageManager.PERMISSION_GRANTED }
                .map { it.first }

            if (deniedPermissions.isEmpty()) {
                Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions denied: $deniedPermissions", Toast.LENGTH_LONG).show()
            }
        }
    }
}
