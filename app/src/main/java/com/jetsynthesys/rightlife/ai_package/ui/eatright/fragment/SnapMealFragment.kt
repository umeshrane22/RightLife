package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.FileProvider
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.AnalysisRequest
import com.jetsynthesys.rightlife.databinding.FragmentSnapMealBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Base64
import android.widget.EditText
import android.widget.TextView
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jetsynthesys.rightlife.ai_package.model.ScanMealNutritionResponse
import com.jetsynthesys.rightlife.ui.utility.Utils
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SnapMealFragment : BaseFragment<FragmentSnapMealBinding>() {

    private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.CAMERA
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA
        )
    }

    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var currentPhotoPath: String
    private lateinit var takePhotoInfoLayout : LinearLayoutCompat
    private lateinit var enterMealDescriptionLayout : LinearLayoutCompat
    private lateinit var proceedLayout : LinearLayoutCompat
    private lateinit var skipTV : TextView
    private lateinit var mealDescriptionET : EditText
    private lateinit var imageFood : ImageView
    private lateinit var videoView : VideoView
    private var imagePath : String = ""
    private var isProceedResult : Boolean = false

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSnapMealBinding
        get() = FragmentSnapMealBinding::inflate
    var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        proceedLayout = view.findViewById(R.id.layout_proceed)
        imageFood = view.findViewById(R.id.imageFood)
        videoView = view.findViewById(R.id.videoView)
        takePhotoInfoLayout = view.findViewById(R.id.takePhotoInfoLayout)
        enterMealDescriptionLayout = view.findViewById(R.id.enterMealDescriptionLayout)
        skipTV = view.findViewById(R.id.skipTV)
        mealDescriptionET = view.findViewById(R.id.mealDescriptionET)

        skipTV.setOnClickListener {
            takePhotoInfoLayout.visibility = View.VISIBLE
            enterMealDescriptionLayout.visibility = View.GONE
            skipTV.visibility = View.GONE
            videoView.visibility = View.VISIBLE
            imageFood.visibility = View.GONE
            isProceedResult = false
            proceedLayout.isEnabled = true
            proceedLayout.setBackgroundResource(R.drawable.green_meal_bg)
            videoPlay()
        }

        videoPlay()

        proceedLayout.setOnClickListener {
            // Request all permissions at once
            if (!hasAllPermissions()) {
                requestAllPermissions()
            }else{
                if (isProceedResult){
                    if (imagePath != ""){
                        uploadFoodImagePath(imagePath)
                    }else{
                        Toast.makeText(context, "Please capture food",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    openCameraForImage()
                }
            }
//            requireActivity().supportFragmentManager.beginTransaction().apply {
//                val snapMealFragment = MealScanResultFragment()
//                val args = Bundle()
//                args.putString("ModuleName", arguments?.getString("ModuleName").toString())
//                args.putString("ImagePath", imagePath)
//                args.putParcelable("foodDataResponses", null)
//                snapMealFragment.arguments = args
//                replace(R.id.flFragment, snapMealFragment, "Steps")
//                addToBackStack(null)
//                commit()
//            }
        }

        mealDescriptionET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 0){
                    proceedLayout.isEnabled = true
                    proceedLayout.setBackgroundResource(R.drawable.green_meal_bg)
                    isProceedResult = true
                }else{
                    proceedLayout.isEnabled = false
                    proceedLayout.setBackgroundResource(R.drawable.light_green_bg)
                    isProceedResult = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun openCameraForImage() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile: File = createImageFile()
            if (photoFile.exists()) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.fileprovider",
                    photoFile
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            } else {
                Log.e("Camera", "Failed to create file")
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath  // Save the path for later use
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val file = File(currentPhotoPath)
                    if (file.exists()) {
                        val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                        // Rotate the image if required
                        val imageUri = FileProvider.getUriForFile(requireContext(),
                            "${requireContext().packageName}.fileprovider", file)
                        val rotatedBitmap = rotateImageIfRequired(requireContext(), bitmap, imageUri)
                        // Set the image in the UI
                        videoView.visibility = View.GONE
                        imageFood.visibility = View.VISIBLE
                        imageFood.setImageBitmap(rotatedBitmap)
                        // Save image details
                        imagePath = currentPhotoPath
                        takePhotoInfoLayout.visibility = View.GONE
                        enterMealDescriptionLayout.visibility = View.VISIBLE
                        skipTV.visibility = View.VISIBLE
                        proceedLayout.isEnabled = false
                        proceedLayout.setBackgroundResource(R.drawable.light_green_bg)
                        isProceedResult = false
                        mealDescriptionET.text.clear()
                    } else {
                        Log.e("ImageCapture", "File does not exist at $currentPhotoPath")
                    }
                }
            }
        }
    }

    private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, imageUri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val exifInterface = inputStream?.let { ExifInterface(it) }
        val orientation = exifInterface?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        inputStream?.close()

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            else -> bitmap
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 101
    }

    private fun uploadFoodImagePath(imagePath: String) {
        Utils.showLoader(requireActivity())
        val base64Image = encodeImageToBase64(imagePath)
        val apiKey = "d6JBKPaLTVeyAJtIrKOK"
        val request = AnalysisRequest(apiKey, base64Image)
        val call = ApiClient.apiServiceFoodCaptureImageApi.analyzeFoodImage(
            "analysis", request)
        call.enqueue(object : Callback<ScanMealNutritionResponse> {
            override fun onResponse(call: Call<ScanMealNutritionResponse>, response: Response<ScanMealNutritionResponse>) {
                if (response.isSuccessful) {
                    Utils.dismissLoader(requireActivity())
                    println("Success: ${response.body()}")
                    if (response.body()?.data != null){
                        if (response.body()?.data!!.isNotEmpty()){
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                val snapMealFragment = MealScanResultFragment()
                                val args = Bundle()
                                args.putString("ModuleName", arguments?.getString("ModuleName").toString())
                                args.putString("ImagePath", imagePath)
                                args.putParcelable("foodDataResponses", response.body())
                                snapMealFragment.arguments = args
                                replace(R.id.flFragment, snapMealFragment, "Steps")
                                addToBackStack(null)
                                commit()
                            }
                        }else{
                            Toast.makeText(context, "Data not find out please try again", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(context, "Data not find out please try again", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    println("Error: ${response.errorBody()?.string()}")
                    Utils.dismissLoader(requireActivity())
                    Toast.makeText(context, response.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ScanMealNutritionResponse>, t: Throwable) {
                println("Failure: ${t.message}")
                Utils.dismissLoader(requireActivity())
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun encodeImageToBase64(imagePath: String): String {
        val file = File(imagePath)
        val inputStream = FileInputStream(file)
        val bytes = inputStream.readBytes()
        inputStream.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    // Check if all required permissions are granted
    private fun hasAllPermissions(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Request all permissions in one go
    private fun requestAllPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
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
                openCameraForImage()
                Toast.makeText(context, "All permissions granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permissions denied: $deniedPermissions", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun videoPlay(){
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/${R.raw.mealsnap_v31}")
        videoView.setVideoURI(videoUri)
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
//        videoView.setMediaController(null)
//        videoView.start()
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true  // ✅ This enables auto-continuous playback
            videoView.setMediaController(null)
            videoView.start()
        }
    }
}