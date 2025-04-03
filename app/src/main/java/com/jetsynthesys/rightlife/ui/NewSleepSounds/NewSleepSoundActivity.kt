package com.jetsynthesys.rightlife.ui.NewSleepSounds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityNewSleepSoundBinding
import com.jetsynthesys.rightlife.ui.NewSleepSounds.newsleepmodel.AddPlaylistResponse
import com.jetsynthesys.rightlife.ui.NewSleepSounds.newsleepmodel.Service
import com.jetsynthesys.rightlife.ui.NewSleepSounds.newsleepmodel.SleepCategory
import com.jetsynthesys.rightlife.ui.NewSleepSounds.newsleepmodel.SleepCategoryResponse
import com.jetsynthesys.rightlife.ui.NewSleepSounds.newsleepmodel.SleepCategorySoundListResponse
import com.jetsynthesys.rightlife.ui.NewSleepSounds.userplaylistmodel.NewReleaseResponse
import com.jetsynthesys.rightlife.ui.NewSleepSounds.userplaylistmodel.SleepSoundPlaylistResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.jetsynthesys.rightlife.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewSleepSoundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewSleepSoundBinding
    private lateinit var categoryAdapter: SleepCategoryAdapter
    private val categoryList = mutableListOf<SleepCategory>()
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var sleepCategoryResponse: SleepCategoryResponse? = null
    private var selectedCategoryForTitle: SleepCategory? = null
    private var sleepSoundPlaylistResponse: SleepSoundPlaylistResponse? = null
    private var useplaylistdata: ArrayList<Service> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewSleepSoundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        //back button
        binding.iconBack.setOnClickListener {
            if (binding.layoutVerticalCategoryList.visibility == View.VISIBLE) {
                binding.layoutVerticalCategoryList.visibility = View.GONE
                binding.layouthorizontalMusicList.visibility = View.VISIBLE
                binding.recyclerViewHorizontalList.visibility = View.VISIBLE
                binding.recyclerViewVerticalList.visibility = View.GONE
                fetchSleepSoundsByCategoryId(categoryList[1]._id, true)
            } else
                onBackPressedDispatcher.onBackPressed()
        }
        setupCategoryRecyclerView()
        fetchCategories()
        getUserCreatedPlaylist()
        getNewReleases()
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = SleepCategoryAdapter(categoryList) { selectedCategory ->
            // 🔥 Handle selected category here
            selectedCategoryForTitle = selectedCategory;
            Log.d("SleepCategory", "Selected: ${selectedCategory.title}")
            Toast.makeText(this, "Selected: ${selectedCategory.title}", Toast.LENGTH_SHORT).show()
            // You can perform an action, like loading content specific to the category!
            fetchSleepSoundsByCategoryId(selectedCategory._id, false)
        }

        binding.recyclerCategory.apply {
            layoutManager = LinearLayoutManager(
                this@NewSleepSoundActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = categoryAdapter
        }
    }


    private fun fetchCategories() {
        Utils.showLoader(this)
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getSleepCategories(sharedPreferenceManager.accessToken)

        call.enqueue(object : Callback<SleepCategoryResponse> {
            override fun onResponse(
                call: Call<SleepCategoryResponse>,
                response: Response<SleepCategoryResponse>
            ) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                if (response.isSuccessful && response.body() != null) {
                    sleepCategoryResponse = response.body()

                    categoryList.clear()
                    sleepCategoryResponse?.let { categoryList.addAll(it.data) }
                    categoryAdapter.notifyDataSetChanged()
                    fetchSleepSoundsByCategoryId(categoryList.get(1)._id, true)
                } else {
                    showToast("Server Error: " + response.code())
                }
            }

            override fun onFailure(call: Call<SleepCategoryResponse>, t: Throwable) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                showToast("Network Error: " + t.message)
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun fetchSleepSoundsByCategoryId(categoryId: String, isForHome: Boolean) {
        Utils.showLoader(this)
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val call = apiService.getSleepSoundsById(
            sharedPreferenceManager.accessToken,
            categoryId,
            0,
            10,
            "catagory"
        );


        call.enqueue(object : Callback<SleepCategorySoundListResponse> {
            override fun onResponse(
                call: Call<SleepCategorySoundListResponse>,
                response: Response<SleepCategorySoundListResponse>
            ) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                if (response.isSuccessful && response.body() != null) {
                    val soundData = response.body()
                    Log.d("SleepSound", "Data: ${soundData?.data?.services}")
                    // Pass soundData.data.services to adapter
                    if (isForHome) {
                        binding.layouthorizontalMusicList.visibility = View.VISIBLE
                        binding.layoutVerticalCategoryList.visibility = View.GONE
                        setupHorizontalRecyclerView(soundData?.data?.services)
                    } else {
                        binding.layouthorizontalMusicList.visibility = View.GONE
                        binding.layoutVerticalCategoryList.visibility = View.VISIBLE
                        setupVerticleRecyclerView(soundData?.data?.services)
                    }

                } else {
                    showToast("Server Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SleepCategorySoundListResponse>, t: Throwable) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                showToast("Network Error: ${t.message}")
            }
        })
    }

    private fun setupHorizontalRecyclerView(services: ArrayList<Service>?) {
        val adapter = services?.let { serviceList ->
            SleepHorizontalListAdapter(
                serviceList,
                onItemClick = { selectedList, position ->
                    // Handle item click (open player screen)
                    startActivity(Intent(this, SleepSoundPlayerActivity::class.java).apply {
                        putExtra("SOUND_LIST", selectedList)
                        putExtra("SELECTED_POSITION", position)
                        putExtra("ISUSERPLAYLIST", false)
                    })
                },
                onAddToPlaylistClick = { service, position ->
                    // Handle add to playlist click here
                    addToPlaylist(service._id, position)
                    Toast.makeText(this, "Added to playlist in Activity", Toast.LENGTH_SHORT).show()
                }
            )

        }

        binding.recyclerViewVerticalList.visibility = View.GONE

        binding.layoutVerticalCategoryList.visibility = View.GONE
        binding.layouthorizontalMusicList.visibility = View.VISIBLE
        binding.recyclerViewHorizontalList.visibility = View.VISIBLE

        binding.recyclerViewHorizontalList.apply {
            layoutManager = LinearLayoutManager(
                this@NewSleepSoundActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this.adapter = adapter
        }
    }

    private fun setupYourPlayListRecyclerView(services: ArrayList<Service>?) {
        val adapter = services?.let { serviceList ->
            SleepHorizontalListFullAdapter(
                serviceList,
                "Playlist",
                onItemClick = { selectedList, position ->
                    // Handle item click (open player screen)
                    startActivity(Intent(this, SleepSoundPlayerActivity::class.java).apply {
                        putExtra("SOUND_LIST", selectedList)
                        putExtra("SELECTED_POSITION", position)
                        putExtra("ISUSERPLAYLIST", true)
                    })
                },
                onAddToPlaylistClick = { service, position ->
                    // Handle add to playlist click here
                    //addToPlaylist(service._id, position)
                    //Toast.makeText(this, "Added to playlist in Activity", Toast.LENGTH_SHORT).show()
                }
            )

        }

        binding.recyclerViewPlayList.apply {
            layoutManager = LinearLayoutManager(
                this@NewSleepSoundActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this.adapter = adapter
        }
    }

    private fun setupNewReleaseRecyclerView(services: ArrayList<Service>?) {
        val adapter = services?.let { serviceList ->
            SleepHorizontalListFullAdapter(
                serviceList,
                onItemClick = { selectedList, position ->
                    // Handle item click (open player screen)
                    startActivity(Intent(this, SleepSoundPlayerActivity::class.java).apply {
                        putExtra("SOUND_LIST", selectedList)
                        putExtra("SELECTED_POSITION", position)
                        putExtra("ISUSERPLAYLIST", false)
                    })
                },
                onAddToPlaylistClick = { service, position ->
                    // Handle add to playlist click here
                    addToPlaylist(service._id, position)
                    Toast.makeText(this, "Added to playlist in Activity", Toast.LENGTH_SHORT).show()
                }
            )

        }

        binding.recyclerViewNewRelease.apply {
            layoutManager = LinearLayoutManager(
                this@NewSleepSoundActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this.adapter = adapter
        }
    }


    private fun setupVerticleRecyclerView(services: ArrayList<Service>?) {
        val adapter = services?.let { serviceList ->
            SleepSoundGridAdapter(
                serviceList,
                onItemClick = { selectedList, position ->
                    // Handle item click (open player screen)
                    startActivity(Intent(this, SleepSoundPlayerActivity::class.java).apply {
                        putExtra("SOUND_LIST", selectedList)
                        putExtra("SELECTED_POSITION", position)
                        putExtra("ISUSERPLAYLIST", false)
                    })
                },
                onAddToPlaylistClick = { service, position ->
                    // Handle add to playlist click here
                    addToPlaylist(service._id, position)
                    Toast.makeText(this, "Added to playlist in Activity", Toast.LENGTH_SHORT).show()
                }
            )

        }

        binding.categorytTitle.text = selectedCategoryForTitle?.title
        binding.categorytTitle.visibility = View.VISIBLE
        binding.recyclerViewVerticalList.visibility = View.VISIBLE
        binding.categorytTitleDesciption.text = selectedCategoryForTitle?.subtitle
        binding.categorytTitleDesciption.visibility = View.VISIBLE

        binding.layoutVerticalCategoryList.visibility = View.VISIBLE
        binding.layouthorizontalMusicList.visibility = View.GONE

        binding.recyclerViewVerticalList.apply {
            layoutManager = GridLayoutManager(this@NewSleepSoundActivity, 2)
            this.adapter = adapter
        }
    }


    // Add Sleep sound to using playlist api
    private fun addToPlaylist(songId: String, position: Int) {
        Utils.showLoader(this)
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.addToPlaylist(sharedPreferenceManager.accessToken, songId)

        call.enqueue(object : Callback<AddPlaylistResponse> {
            override fun onResponse(
                call: Call<AddPlaylistResponse>,
                response: Response<AddPlaylistResponse>
            ) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                if (response.isSuccessful && response.body() != null) {
                    showToast(response.body()?.successMessage ?: "Added to Playlist!")
                } else {
                    showToast("Failed to add to playlist: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AddPlaylistResponse>, t: Throwable) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                showToast("Network Error: ${t.message}")
            }
        })
    }


    // get user play list from api
    private fun getUserCreatedPlaylist() {
        Utils.showLoader(this)
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getUserCreatedPlaylist(sharedPreferenceManager.accessToken)

        call.enqueue(object : Callback<SleepSoundPlaylistResponse> {
            override fun onResponse(
                call: Call<SleepSoundPlaylistResponse>,
                response: Response<SleepSoundPlaylistResponse>
            ) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                if (response.isSuccessful && response.body() != null) {
                    sleepSoundPlaylistResponse = response.body()

                    useplaylistdata = sleepSoundPlaylistResponse?.data as ArrayList<Service>
                    if (sleepSoundPlaylistResponse?.data?.isNotEmpty() == true) {
                        setupYourPlayListRecyclerView(useplaylistdata)
                    } else {
                        showToast("No playlist data available")
                    }
                } else {
                    showToast("Server Error: " + response.code())
                }
            }

            override fun onFailure(call: Call<SleepSoundPlaylistResponse>, t: Throwable) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                showToast("Network Error: " + t.message)
            }

        })
    }

    // get New Release from api
    private fun getNewReleases() {
        Utils.showLoader(this)
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getNewReleases(sharedPreferenceManager.accessToken, "recommended")

        call.enqueue(object : Callback<NewReleaseResponse> {
            override fun onResponse(
                call: Call<NewReleaseResponse>,
                response: Response<NewReleaseResponse>
            ) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()?.data?.services?.isNotEmpty() == true) {
                        setupNewReleaseRecyclerView(response.body()?.data?.services?.let { ArrayList(it) })
                    } else {
                        showToast("No Releases data available")
                        binding.layoutNewRelease.visibility = View.GONE
                    }
                } else {
                    showToast("Server Error: " + response.code())
                }
            }

            override fun onFailure(call: Call<NewReleaseResponse>, t: Throwable) {
                Utils.dismissLoader(this@NewSleepSoundActivity)
                showToast("Network Error: " + t.message)
            }

        })
    }

}
