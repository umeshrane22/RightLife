package com.jetsynthesys.rightlife.ui.affirmation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityTodaysAffirmationBinding
import com.jetsynthesys.rightlife.ui.affirmation.adapter.AffirmationCardPagerAdapter
import com.jetsynthesys.rightlife.ui.affirmation.pojo.AffirmationCategoryData
import com.jetsynthesys.rightlife.ui.affirmation.pojo.AffirmationCategoryListResponse
import com.jetsynthesys.rightlife.ui.affirmation.pojo.AffirmationSelectedCategoryData
import com.jetsynthesys.rightlife.ui.affirmation.pojo.AffirmationSelectedCategoryResponse
import com.jetsynthesys.rightlife.ui.affirmation.pojo.CreateAffirmationPlaylistRequest
import com.jetsynthesys.rightlife.ui.affirmation.pojo.GetAffirmationPlaylistResponse
import com.jetsynthesys.rightlife.ui.showBalloon
import com.jetsynthesys.rightlife.ui.showBalloonWithDim
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.jetsynthesys.rightlife.ui.utility.Utils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.abs


class TodaysAffirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodaysAffirmationBinding
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var categoryBottomSheetDialog: BottomSheetDialog
    private lateinit var closeBottomSheetDialog: BottomSheetDialog
    private lateinit var discardBottomSheetDialog: BottomSheetDialog
    private val affirmationPlaylist: ArrayList<AffirmationSelectedCategoryData> = ArrayList()
    private val affirmationPlaylistRequest: ArrayList<String> = ArrayList()
    private var selectedCategoryPosition = 0
    private val categoryList: ArrayList<AffirmationCategoryData> = ArrayList()

    private val affirmationList: ArrayList<AffirmationSelectedCategoryData> = ArrayList()
    private lateinit var affirmationCardPagerAdapter: AffirmationCardPagerAdapter
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodaysAffirmationBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        setupCategoryBottomSheet()
        setupCloseBottomSheet()
        setupDiscardBottomSheet()
        getAffirmationPlaylist()
        getCategoryList()

        if (sharedPreferenceManager.firstTimeUserForAffirmation)
            showInfoDialog()

        binding.llCategorySelection.setOnClickListener {
            categoryBottomSheetDialog.show()
        }

        binding.infoAffirmation.setOnClickListener {
            showInfoDialog()
        }

        binding.shareAffirmation.setOnClickListener {
            val bitmap =
                getBitmapFromView(affirmationCardPagerAdapter.getViewAt(binding.cardViewPager.currentItem)!!)
            val imageUri = saveBitmapToCache(bitmap)
            imageUri?.let {
                shareImage(it)
            }
        }

        binding.ivClose.setOnClickListener {
            if (sharedPreferenceManager.firstTimeUserForAffirmation)
                closeBottomSheetDialog.show()
            else if (affirmationPlaylistRequest.isNotEmpty())
                discardBottomSheetDialog.show()
            else
                finish()
        }

        binding.addAffirmation.setOnClickListener {
            addCardToPlaylist()
        }

        binding.btnCreateAffirmation.setOnClickListener {
            if (affirmationPlaylistRequest.isNotEmpty())
                createAffirmationPlaylist()
        }

        setSelectedCategoryAdapter(affirmationList)

        val gestureDetector =
            GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (e1 == null || e2 == null) return false
                    val deltaY = e2.y - e1.y

                    if (abs(deltaY) > 150) {
                        if (deltaY < 0) {
                            onSwipeUp()
                        } else {
                            onSwipeDown()
                        }
                        return true
                    }
                    return false
                }
            })

        binding.cardViewPager.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }

    }

    private fun onSwipeUp() {
        // Animate both
        if (selectedCategoryPosition < categoryList.size - 1) {
            binding.cardViewPager.animate()
                .translationYBy(-(binding.cardViewPager.height.toFloat() + 100))
                .setDuration(300)
                .withEndAction {
                    selectedCategoryPosition += 1
                    getSelectedCategoryData(categoryList[selectedCategoryPosition].id)
                    binding.tvCategory.text = categoryList[selectedCategoryPosition].title
                    binding.cardViewPager.translationY =
                        binding.cardViewPager.height.toFloat() + 100
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.cardViewPager.animate()
                            .translationY(0f)
                            .setDuration(300)
                            .start()
                    }, 1000)
                }
                .start()
        }
    }

    private fun onSwipeDown() {
        if (selectedCategoryPosition > 0) {
            binding.cardViewPager.animate()
                .translationYBy(binding.cardViewPager.height.toFloat() + 100)
                .setDuration(300)
                .withEndAction {
                    selectedCategoryPosition -= 1
                    getSelectedCategoryData(categoryList[selectedCategoryPosition].id)
                    binding.tvCategory.text = categoryList[selectedCategoryPosition].title
                    binding.cardViewPager.translationY =
                        -(binding.cardViewPager.height.toFloat() + 100)
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.cardViewPager.animate()
                            .translationY(0f)
                            .setDuration(300)
                            .start()
                    }, 1000)
                }
                .start()
        }
    }


    private fun addCardToPlaylist() {
        val yOff = -200
        val xOff = 10
        binding.addAffirmation.isEnabled = false
        affirmationPlaylist.add(affirmationList[binding.cardViewPager.currentItem])
        binding.addAffirmation.setImageResource(R.drawable.playlist_added)
        affirmationList[binding.cardViewPager.currentItem].id?.let {
            affirmationPlaylistRequest.add(
                it
            )
        }
        if (affirmationPlaylist.size >= 3)
            binding.btnCreateAffirmation.isEnabled = true

        if (sharedPreferenceManager.firstTimeUserForAffirmation) {
            when (affirmationPlaylist.size) {
                1 -> {
                    //Toast.makeText(this, "Great choice, keep going.", Toast.LENGTH_SHORT).show()
                    showBalloon(
                        binding.addAffirmation, "Great choice, keep going.",
                        xOff = xOff, yOff = yOff
                    )
                }

                2 -> {
                    /*Toast.makeText(
                        this,
                        "One more and your playlist is ready to go.",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    showBalloon(
                        binding.addAffirmation,
                        "One more and your playlist is ready to go.", xOff = xOff, yOff = yOff
                    )
                }

                3 -> {
                    //Toast.makeText(this, "Playlist Unlocked!", Toast.LENGTH_SHORT).show()
                    showBalloon(
                        binding.addAffirmation,
                        "Playlist Unlocked!",
                        xOff = xOff,
                        yOff = yOff
                    )
                }

                else -> {
                    /*Toast.makeText(
                        this,
                        "${affirmationPlaylist.size} Affirmation Added!",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    showBalloon(
                        binding.addAffirmation,
                        "${affirmationPlaylist.size} Affirmation Added!", xOff = xOff, yOff = yOff
                    )
                }
            }
        } else {
            /*Toast.makeText(
                this,
                "${affirmationPlaylistRequest.size} Affirmation Added!",
                Toast.LENGTH_SHORT
            ).show()*/
            showBalloon(
                binding.addAffirmation,
                "${affirmationPlaylistRequest.size} Affirmation Added!", xOff = xOff, yOff = yOff
            )
        }
    }

    private fun setSelectedCategoryAdapter(affirmationList: ArrayList<AffirmationSelectedCategoryData>) {
        affirmationCardPagerAdapter =
            AffirmationCardPagerAdapter(affirmationList, this, binding.cardViewPager)
        binding.cardViewPager.setPageTransformer(true, AffirmationPageTransformer())
        binding.cardViewPager.adapter = affirmationCardPagerAdapter
        if (affirmationList.isNotEmpty())
            updateAddButtonImage(0)
        binding.cardViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                updateAddButtonImage(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun updateAddButtonImage(position: Int) {
        var flag = false
        for (playlist in affirmationPlaylist) {
            if (playlist.id == affirmationList[position].id) {
                flag = true
            }
        }
        if (flag) {
            binding.addAffirmation.setImageResource(R.drawable.playlist_added)
            binding.addAffirmation.isEnabled = false
        } else {
            binding.addAffirmation.setImageResource(R.drawable.add_affirmation)
            binding.addAffirmation.isEnabled = true
        }

    }

    private fun setupCategoryBottomSheet() {
        // Create and configure BottomSheetDialog
        categoryBottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)

        // Set up RecyclerView in the BottomSheet
        recyclerViewCategory = bottomSheetView.findViewById(R.id.recyclerView)
        recyclerViewCategory.setLayoutManager(LinearLayoutManager(this))
        //recyclerViewCategory.setAdapter(myAdapter)

        categoryBottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        val ivClose = bottomSheetView.findViewById<ImageView>(R.id.ivClose)
        ivClose.setOnClickListener {
            categoryBottomSheetDialog.dismiss()
        }

    }

    private fun setupDiscardBottomSheet() {
        // Create and configure BottomSheetDialog
        discardBottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_discard_bottomsheet, null)


        discardBottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        bottomSheetView.findViewById<Button>(R.id.btnNo).setOnClickListener {
            discardBottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<Button>(R.id.btnYes).setOnClickListener {
            finish()
        }

    }

    private fun setupCloseBottomSheet() {
        // Create and configure BottomSheetDialog
        closeBottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_close_bottomsheet, null)


        closeBottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        bottomSheetView.findViewById<Button>(R.id.btnGoBackAndSave3).setOnClickListener {
            closeBottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<Button>(R.id.btnQuitAnyway).setOnClickListener {
            finish()
        }

    }

    private fun setupCategoryAdapter() {
        val affirmationCategoryListAdapter = AffirmationCategoryListAdapter(
            this, categoryList
        ) { category ->
            selectedCategoryPosition = categoryList.indexOf(category)
            getSelectedCategoryData(category.id)
            if (categoryBottomSheetDialog.isShowing)
                categoryBottomSheetDialog.dismiss()
            binding.tvCategory.text = category.title
        }
        recyclerViewCategory.adapter = affirmationCategoryListAdapter
    }

    private fun getCategoryList() {
        val authToken = sharedPreferenceManager.accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getAffirmationCategoryList(authToken)

        call.enqueue(object : Callback<AffirmationCategoryListResponse> {
            override fun onResponse(
                call: Call<AffirmationCategoryListResponse>,
                response: Response<AffirmationCategoryListResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.data?.let {
                        categoryList.addAll(it)
                        setupCategoryAdapter()
                        if (it.isNotEmpty())
                            getSelectedCategoryData(it[0].id)
                    }
                } else {
                    Toast.makeText(
                        this@TodaysAffirmationActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AffirmationCategoryListResponse>, t: Throwable) {
                Toast.makeText(
                    this@TodaysAffirmationActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun getSelectedCategoryData(id: String?) {
        Utils.showLoader(this)
        val authToken = sharedPreferenceManager.accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getAffirmationSelectedCategoryData(authToken, id)

        call.enqueue(object : Callback<AffirmationSelectedCategoryResponse> {
            override fun onResponse(
                call: Call<AffirmationSelectedCategoryResponse>,
                response: Response<AffirmationSelectedCategoryResponse>
            ) {
                Utils.dismissLoader(this@TodaysAffirmationActivity)
                if (response.isSuccessful && response.body() != null) {
                    affirmationList.clear()
                    response.body()?.data?.let { affirmationList.addAll(it) }
                    setSelectedCategoryAdapter(affirmationList)
                } else {
                    Toast.makeText(
                        this@TodaysAffirmationActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AffirmationSelectedCategoryResponse>, t: Throwable) {
                Utils.dismissLoader(this@TodaysAffirmationActivity)
                Toast.makeText(
                    this@TodaysAffirmationActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun getAffirmationPlaylist() {
        Utils.showLoader(this)
        val authToken = sharedPreferenceManager.accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getAffirmationPlaylist(authToken)

        call.enqueue(object : Callback<GetAffirmationPlaylistResponse> {
            override fun onResponse(
                call: Call<GetAffirmationPlaylistResponse>,
                response: Response<GetAffirmationPlaylistResponse>
            ) {
                Utils.dismissLoader(this@TodaysAffirmationActivity)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.data?.let { affirmationPlaylist.addAll(it) }
                    if (affirmationPlaylist.isNotEmpty()) {
                        binding.btnCreateAffirmation.text = "Save"
                        binding.btnCreateAffirmation.isEnabled = true
                    }
                } else {
                    Toast.makeText(
                        this@TodaysAffirmationActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetAffirmationPlaylistResponse>, t: Throwable) {
                Utils.dismissLoader(this@TodaysAffirmationActivity)
                Toast.makeText(
                    this@TodaysAffirmationActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun createAffirmationPlaylist() {
        Utils.showLoader(this)
        val authToken = sharedPreferenceManager.accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val createAffirmationPlaylistRequest = CreateAffirmationPlaylistRequest()
        createAffirmationPlaylistRequest.list?.addAll(affirmationPlaylistRequest)

        val call = apiService.createAffirmationPlaylist(authToken, createAffirmationPlaylistRequest)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Utils.dismissLoader(this@TodaysAffirmationActivity)
                if (response.isSuccessful && response.body() != null) {

                    if (sharedPreferenceManager.firstTimeUserForAffirmation) {
                        showCreatedUpdatedDialog("Playlist Created")
                        sharedPreferenceManager.firstTimeUserForAffirmation = false
                    } else {
                        showCreatedUpdatedDialog("Changes Saved")
                    }
                } else {
                    Toast.makeText(
                        this@TodaysAffirmationActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Utils.dismissLoader(this@TodaysAffirmationActivity)
                Toast.makeText(
                    this@TodaysAffirmationActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun showInfoDialog() {

        // Create the dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_info_affirmation)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val window = dialog.window

        // Set the dim amount
        val layoutParams = window?.attributes
        layoutParams?.dimAmount = 0.7f // Adjust the dim amount (0.0 - 1.0)
        window?.attributes = layoutParams

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        layoutParams?.width = width

        dialog.setOnCancelListener {
            if (sharedPreferenceManager.firstTimeUserForAffirmation)
                showBalloonWithDim(
                    binding.btnCreateAffirmation,
                    "Select at least 3 affirmations to build your personal affirmation playlist!",
                    "AffirmationCreateButton", xOff = -200, yOff = 20
                ) {
                    showBalloonWithDim(
                        binding.addAffirmation,
                        "Save to your Playlist",
                        "AffirmationAddButton", xOff = 10, yOff = -200
                    )
                }
        }
        dialog.show()
    }

    private fun showCreatedUpdatedDialog(message: String) {

        // Create the dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_playlist_created)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val window = dialog.window

        // Set the dim amount
        val layoutParams = window?.attributes
        layoutParams?.dimAmount = 0.7f // Adjust the dim amount (0.0 - 1.0)
        window?.attributes = layoutParams

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        layoutParams?.width = width

        dialog.findViewById<TextView>(R.id.tvDialogPlaylistCreated).text = message

        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            finish()
        }, 1000)
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveBitmapToCache(bitmap: Bitmap): Uri? {
        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "shared_image.png")

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider", // Must match the authority in your manifest
            file
        )
    }

    private fun shareImage(imageUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }


}