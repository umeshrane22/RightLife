package com.example.rlapp.ai_package.ui.thinkright.fragment

import Phq9Item
import android.Manifest
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresPermission
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.rlapp.R
import com.example.rlapp.ai_package.base.BaseFragment
import com.example.rlapp.ai_package.ui.thinkright.Phq9Assessment
import com.example.rlapp.ai_package.ui.thinkright.SeverityLevel
import com.example.rlapp.ai_package.model.ModuleData
import com.example.rlapp.ai_package.model.ModuleResponse
import com.example.rlapp.ai_package.ui.moveright.MindfulnessReviewDialog
import com.example.rlapp.ai_package.data.repository.ApiClient
import com.example.rlapp.ai_package.model.ThinkQuoteResponse
import com.example.rlapp.ai_package.model.ToolsData
import com.example.rlapp.ai_package.model.ToolsResponse
import com.example.rlapp.ai_package.ui.adapter.WorkoutAdapter
import com.example.rlapp.ai_package.ui.eatright.model.MyMealModel
import com.example.rlapp.ai_package.ui.sleepright.fragment.SleepPerformanceFragment
import com.example.rlapp.ai_package.ui.sleepright.fragment.SleepRightLandingFragment
import com.example.rlapp.ai_package.ui.thinkright.adapter.MoreToolsAdapter
import com.example.rlapp.ai_package.ui.thinkright.adapter.Phq9Adapter
import com.example.rlapp.ai_package.ui.thinkright.adapter.ToolAdapter
import com.example.rlapp.ai_package.ui.thinkright.adapter.ToolsAdapter
import com.example.rlapp.databinding.FragmentThinkRightLandingBinding
import com.example.rlapp.ui.affirmation.PractiseAffirmationPlaylistActivity
import com.example.rlapp.ui.affirmation.TodaysAffirmationActivity
import com.example.rlapp.ui.breathwork.BreathworkActivity
import com.example.rlapp.ui.jounal.new_journal.JournalListActivity
import com.example.rlapp.ui.jounal.new_journal.JournalNewActivity
import com.example.rlapp.ui.mindaudit.MASuggestedAssessmentActivity
import com.example.rlapp.ui.mindaudit.MindAuditActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDate
import kotlin.math.abs

class ThinkRightReportFragment : BaseFragment<FragmentThinkRightLandingBinding>() {

    private lateinit var carouselViewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var add_tools_think_right: ImageView
    private lateinit var instruction_your_mindfullness_review: ImageView
    private lateinit var dots: Array<ImageView?>
    private lateinit var tvQuote: TextView
    private lateinit var downloadView: ImageView
    private lateinit var tvAuthor: TextView
    private lateinit var cardAddTools: CardView
    private lateinit var toolsRecyclerView: RecyclerView
    private lateinit var journalingRecyclerView: RecyclerView
    private lateinit var noDataMindFullnessMetric: ConstraintLayout

    private var noData: Boolean = false
    private lateinit var thinkQuoteResponse : ThinkQuoteResponse
    private lateinit var toolRecyclerView: RecyclerView
    private lateinit var toolAdapter: ToolAdapter
    private val toolsList: ArrayList<ModuleData> = arrayListOf()
 //   private val toolsAdapter by lazy { ToolsAdapter(requireContext(), 3) }

    private lateinit var progressDialog: ProgressDialog
    private lateinit var toolsResponse : ToolsResponse
    private lateinit var mainView : LinearLayout
    private var toolsArray : ArrayList<ToolsData> = arrayListOf()
    private lateinit var toolsAdapter: ToolsAdapter
    private val toolsMoreAdapter by lazy { MoreToolsAdapter(requireContext(), 4) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentThinkRightLandingBinding
        get() = FragmentThinkRightLandingBinding::inflate
    var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchToolList()
        fetchQuoteData()
        carouselViewPager = view.findViewById(R.id.viewPager)
        tvQuote = view.findViewById(R.id.tv_quote_desc)
        dotsLayout = view.findViewById(R.id.dotsLayout)
        cardAddTools = view.findViewById(R.id.add_tools_think_right)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        toolsRecyclerView = view.findViewById(R.id.rec_journaling_tools)
        tvAuthor = view.findViewById(R.id.tv_quote_author)
        noDataMindFullnessMetric = view.findViewById(R.id.noDataMindFullnessMetric)
        instruction_your_mindfullness_review =
            view.findViewById(R.id.instruction_your_mindfullness_review)
        // add_tools_think_right = view.findViewById(R.id.add_tools_think_right)
        instruction_your_mindfullness_review.setOnClickListener {
            val dialog = MindfulnessReviewDialog.newInstance()
            dialog.show(parentFragmentManager, "MindfulnessReviewDialog")
            // Use parentFragmentManager for fragments
        }
        toolsRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
       // toolsRecyclerView.adapter = toolsAdapter
        toolsRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        toolsRecyclerView.adapter = toolsMoreAdapter
        downloadView = view.findViewById(R.id.view_download_icon)
        mainView = view.findViewById(R.id.lyt_main_page)
        toolsAdapter = ToolsAdapter(requireContext(), toolsArray, :: onToolsItem)
        journalingRecyclerView = view.findViewById(R.id.rec_add_tools)
        journalingRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        journalingRecyclerView.adapter = toolsAdapter


        toolRecyclerView = view.findViewById(R.id.tool_list_think_right)
        toolAdapter = ToolAdapter(toolsList, :: onToolItem)
        toolRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        toolRecyclerView.adapter = toolAdapter
        /*val cardItems = listOf(
            CardItem("Functional Strength Training", "This is the first card."),
            CardItem("Functional Strength Training", "This is the second card."),
            CardItem("Functional Strength Training", "This is the third card.")
        )
        val adapter = CrousalTabAdapter(cardItems)
        carouselViewPager.adapter = adapter
        addDotsIndicator(cardItems.size)*/
        carouselViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)
            }
        })
        carouselViewPager.setPageTransformer { page, position ->
            val offset = abs(position)
            page.scaleY = 1 - (offset * 0.1f)
        }

        tvQuote.setOnClickListener {
            navigateToFragment(ViewQuoteFragment(),"ViewQuote")
        }
        downloadView.setOnClickListener {
            saveViewAsPdf(requireContext(),mainView,"Journal")
        }

        cardAddTools.setOnClickListener {
            navigateToFragment(AddToolsFragment(), "AddToolsFragment")
        }

        view.findViewById<LinearLayout>(R.id.play_now).setOnClickListener {
            startActivity(Intent(requireContext(), PractiseAffirmationPlaylistActivity::class.java))
        }
        view.findViewById<LinearLayout>(R.id.lyt_journaling).setOnClickListener {
            startActivity(Intent(requireContext(), JournalListActivity::class.java))
        }
        view.findViewById<LinearLayout>(R.id.lyt_add_new).setOnClickListener {
            startActivity(Intent(requireContext(), JournalNewActivity::class.java))
        }
        view.findViewById<LinearLayout>(R.id.lyt_breathing).setOnClickListener {
            startActivity(Intent(requireContext(), BreathworkActivity::class.java))
        }
        view.findViewById<ConstraintLayout>(R.id.lyt_top_header).setOnClickListener {
            startActivity(Intent(requireContext(), MindAuditActivity::class.java))
        }
        view.findViewById<ImageView>(R.id.ivSetting).setOnClickListener {
            startActivity(Intent(requireContext(), TodaysAffirmationActivity::class.java))
        }

        view.findViewById<ImageView>(R.id.img_happy_icon).setOnClickListener {

        }

        view.findViewById<ImageView>(R.id.img_relaxed).setOnClickListener {

        }

        view.findViewById<ImageView>(R.id.img_unsure).setOnClickListener {

        }

        view.findViewById<ImageView>(R.id.img_stressed).setOnClickListener {

        }

        view.findViewById<ImageView>(R.id.img_sad).setOnClickListener {
            //showImageDialog(R.drawable.sad_icon)
        }

        view.findViewById<LinearLayout>(R.id.llGAD7).setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    MASuggestedAssessmentActivity::class.java
                ).apply {
                    putExtra("SelectedAssessment", "GAD-7")
                })
        }
        view.findViewById<LinearLayout>(R.id.llDASS21).setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    MASuggestedAssessmentActivity::class.java
                ).apply {
                    putExtra("SelectedAssessment", "DASS-21")
                })
        }
        view.findViewById<LinearLayout>(R.id.llOHQ).setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    MASuggestedAssessmentActivity::class.java
                ).apply {
                    putExtra("SelectedAssessment", "OHQ")
                })
        }

        if (noData == true) {
            noDataMindFullnessMetric.visibility = View.VISIBLE
        } else {
            noDataMindFullnessMetric.visibility = View.GONE
        }

        // Sample data
        val assessment = Phq9Assessment(
            score = 7,
            dateTaken = LocalDate.now(),
            nextScanDate = LocalDate.now().plusDays(29)
        )

        // Bind data to UI elements
        val scoreTextView = view.findViewById<TextView>(R.id.scoreTextView)
        val nextScanTextView = view.findViewById<TextView>(R.id.nextScanTextView)
        val severityRangeLayout = view.findViewById<LinearLayout>(R.id.severityRangeLayout)

        // Set the score
        scoreTextView.text = "YOUR SCORE: ${assessment.score}"

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               // navigateToFragment(ThinkRightReportFragment(), "ThinkRightLandingFragment")
                activity?.finish()
            }
        })

        fetchToolsData()
    }

    private fun fetchToolsData() {
        progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val call = ApiClient.apiService.thinkTools(token)
        call.enqueue(object : Callback<ToolsResponse> {
            override fun onResponse(call: Call<ToolsResponse>, response: Response<ToolsResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    toolsResponse = response.body()!!
                    for (i in 0 until toolsResponse.data.size) {
                        toolsResponse.data.getOrNull(i)?.let {
                            toolsArray.add(it) }
                    }
                    toolsAdapter.notifyDataSetChanged()
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
            override fun onFailure(call: Call<ToolsResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun addDotsIndicator(count: Int) {
        dots = arrayOfNulls(count)
        for (i in 0 until count) {
            dots[i] = ImageView(requireContext()).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.dot_unselected
                    )
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                layoutParams = params
            }
            dotsLayout.addView(dots[i])
        }

        updateDots(0)
    }

    private fun updateDots(position: Int) {
        for (i in dots.indices) {
            val drawable = if (i == position) {
                R.drawable.think_dot_selected
            } else {
                R.drawable.dot_unselected
            }
            dots[i]?.setImageResource(drawable)
        }
    }

    private fun fetchToolList() {
        // progressDialog.show()
        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdlM2ZiMjdiMzNlZGZkNzRlMDY5OWFjIiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiIiLCJsYXN0TmFtZSI6IiIsImRldmljZUlkIjoiVEUxQS4yNDAyMTMuMDA5IiwibWF4RGV2aWNlUmVhY2hlZCI6ZmFsc2UsInR5cGUiOiJhY2Nlc3MtdG9rZW4ifSwiaWF0IjoxNzQzMDU2OTEwLCJleHAiOjE3NTg3ODE3MTB9.gYLi895fpb4HGitALoGDRwHw3MIDCjYXTyqAKDNjS0A"
        val userId = "67a5fae9197992511e71b1c8"

        val call = com.example.rlapp.ai_package.data.repository.ApiClient.apiService.getToolList(
            token,
            userId
        )
        call.enqueue(object : Callback<ModuleResponse> {
            override fun onResponse(call: Call<ModuleResponse>, response: Response<ModuleResponse>) {
                if (response.isSuccessful) {
                   // progressDialog.dismiss()
                    val toolResponse = response.body()
                    toolResponse?.let {
                        if (it.success) {
                            val tools = it.data
                            setToolListData(tools)
                        } else {
                            Toast.makeText(activity, "Request failed with status: ${it.statusCode}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                   /* Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()*/
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModuleResponse>, t: Throwable) {
               /* Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()*/
                Toast.makeText(activity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun setToolListData(tools: List<ModuleData>) {
        toolsList.clear()
        toolsList.addAll(tools)
        toolAdapter.notifyDataSetChanged()
    }

    private fun setupSeverityRangeBar(layout: LinearLayout, assessment: Phq9Assessment) {
        SeverityLevel.values().forEach { level ->
            val rangeView = View(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    level.range.count().toFloat() // Weight based on range size
                )
                setBackgroundColor(resources.getColor(level.colorResId))
            }

            // Add a label for the current severity level
            if (assessment.score in level.range) {
                val label = TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.CENTER
                    }
                    text = level.name
                    textSize = 12f
                    setTextColor(android.graphics.Color.BLACK)
                    setTypeface(null, android.graphics.Typeface.BOLD)
                }
                val container = LinearLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        level.range.count().toFloat()
                    )
                    gravity = android.view.Gravity.CENTER
                    setBackgroundColor(resources.getColor(level.colorResId))
                    addView(label)
                }
                layout.addView(container)
            } else {
                layout.addView(rangeView)
            }
        }
    }

    private fun showImageDialog(imageRes: Int) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_image_zoom)

        val imageView: ImageView = dialog.findViewById(R.id.dialogImageView)
        imageView.setImageResource(imageRes)

        dialog.show()
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    private fun onToolItem(toolsData: ModuleData, position: Int, isRefresh: Boolean) {

        if (toolsData.moduleName.contentEquals("Breathing")){
            startActivity(Intent(requireContext(), BreathworkActivity::class.java))
        }else if (toolsData.moduleName.contentEquals("Journalling")){
            startActivity(Intent(requireContext(), JournalListActivity::class.java))
        }else if (toolsData.moduleName.contentEquals("Affirmation")){
            startActivity(Intent(requireContext(), TodaysAffirmationActivity::class.java))
        }
    }
    private fun fetchQuoteData() {
       // progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val call = ApiClient.apiService.quoteOfDay(token)
        call.enqueue(object : Callback<ThinkQuoteResponse> {
            override fun onResponse(call: Call<ThinkQuoteResponse>, response: Response<ThinkQuoteResponse>) {
                if (response.isSuccessful) {
                   // progressDialog.dismiss()
                    thinkQuoteResponse = response.body()!!
                    tvQuote.setText(thinkQuoteResponse.data.getOrNull(0)?.quote)
                    tvAuthor.setText("-" + thinkQuoteResponse.data.getOrNull(0)?.authorName)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                   // progressDialog.dismiss()
                }
            }
            override fun onFailure(call: Call<ThinkQuoteResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                //progressDialog.dismiss()
            }
        })
    }
    private fun onToolsItem(toolsData: ToolsData, position: Int, isRefresh: Boolean) {

        if (toolsData.moduleName.contentEquals("Breathing")){
            startActivity(Intent(requireContext(), BreathworkActivity::class.java))
        }else if (toolsData.moduleName.contentEquals("Journalling")){
            startActivity(Intent(requireContext(), JournalListActivity::class.java))
        }else if (toolsData.moduleName.contentEquals("Affirmation")){
            startActivity(Intent(requireContext(), TodaysAffirmationActivity::class.java))
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun saveViewAsPdf(context: Context, view: View, fileName: String) {
        val bitmap = getBitmapFromView(view)
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        var fileUri: Uri? = null
        var success = false
        var outputStream: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.pdf")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    fileUri = uri
                    outputStream = resolver.openOutputStream(uri)
                }
            } else {
                val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(directory, "$fileName.pdf")
                fileUri = Uri.fromFile(file)
                outputStream = file.outputStream()
            }

            outputStream?.use {
                document.writeTo(it)
                success = true
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            document.close()
            outputStream?.close()
        }

        if (success && fileUri != null) {
            showDownloadNotification(context, fileName, fileUri)
        }
    }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showDownloadNotification(context: Context, fileName: String, fileUri: Uri) {
        val channelId = "download_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Downloads",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Download Notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent to open the PDF file
        val openPdfIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, openPdfIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle("Download Complete")
            .setContentText("$fileName.pdf saved to Downloads")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent) // Open PDF on click
            .setAutoCancel(true) // Dismiss when tapped
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }
}