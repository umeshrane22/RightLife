package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.jetsynthesys.rightlife.ai_package.model.NutritionResponse
import com.jetsynthesys.rightlife.databinding.FragmentSnapMealBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Base64
import com.jetsynthesys.rightlife.ai_package.model.ScanMealNutritionResponse
import com.jetsynthesys.rightlife.ui.utility.Utils
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SnapMealFragment : BaseFragment<FragmentSnapMealBinding>() {

    private lateinit var currentPhotoPath: String
    private lateinit var imageFood : ImageView
    private lateinit var imageCap : ImageView
    private var imagePath : String = ""
    private lateinit var progressDialog: ProgressDialog

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSnapMealBinding
        get() = FragmentSnapMealBinding::inflate
    var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Fetching food info")
        progressDialog.setMessage("Food is loading, please wait")
        progressDialog.setCancelable(false)
        val proceed = view.findViewById<LinearLayoutCompat>(R.id.layout_proceed)
         imageFood = view.findViewById(R.id.imageFood)
        imageCap = view.findViewById(R.id.imageCap)

        imageCap.setOnClickListener {
            openCameraForImage()
        }

        imageFood.setOnClickListener {
            openCameraForImage()
        }

        proceed.setOnClickListener {
            if (imagePath != ""){
              //  uploadFoodPath(imagePath)
                uploadFoodImagePath(imagePath)
            }else{
                Toast.makeText(context, "Please capture food",Toast.LENGTH_SHORT).show()
            }
        }
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
                        imageCap.visibility = View.GONE
                        imageFood.visibility = View.VISIBLE
                        imageFood.setImageBitmap(rotatedBitmap)
                        // Save image details
                        imagePath = currentPhotoPath
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

    private fun uploadFoodPath(filePath: String) {
        progressDialog.show()
        // Prepare the file to be uploaded
        val file = File(filePath)
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        //val apiKey = "f0e1c21987214e76b497ce49a2ff5f29"
        val apiKey = "7a7a9fd3dec24f22b821243970afb26c"

        // Prepare additional form fields if any (e.g., a description)
        val description = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "File upload description")

        // Make the API call
        val call = ApiClient.apiServiceFoodCaptureApi.uploadFoodFile(filePart, description, apiKey)
        call.enqueue(object : Callback<NutritionResponse> {
            override fun onResponse(call: Call<NutritionResponse>, response: Response<NutritionResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    println("File upload successful: ${response.body()?.status}")
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
                } else {
                    println("File upload failed: ${response.code()}")
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<NutritionResponse>, t: Throwable) {
                println("Error: ${t.message}")
                progressDialog.dismiss()
            }
        })
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
}