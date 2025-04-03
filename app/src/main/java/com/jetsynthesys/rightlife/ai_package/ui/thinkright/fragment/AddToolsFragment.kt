package com.jetsynthesys.rightlife.ai_package.ui.thinkright.fragment

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.AddToolRequest
import com.jetsynthesys.rightlife.ai_package.model.SleepStageResponse
import com.jetsynthesys.rightlife.ai_package.model.ThinkQuoteResponse
import com.jetsynthesys.rightlife.ai_package.model.ToolsData
import com.jetsynthesys.rightlife.ai_package.model.ToolsResponse
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentAllToolsListBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ToolsAdapterList(private val tools: ArrayList<ToolsData>, private val onItemClick: (Int,ToolsData) -> Unit) :
    RecyclerView.Adapter<ToolsAdapterList.ToolViewHolder>() {

    class ToolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.tool_icon)
        val name: TextView = itemView.findViewById(R.id.tool_name)
        val description: TextView = itemView.findViewById(R.id.tool_description)
        val selectButton: ImageView = itemView.findViewById(R.id.tool_select_button)
    }
    val imageList = arrayListOf(
        R.drawable.breathing_male,
        R.drawable.write_note_icon
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tools, parent, false)
        return ToolViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        val tool = tools[position]
        //holder.icon.setImageResource(tools.getOrNull(position).)
        holder.name.text = tools.getOrNull(position)?.moduleName
        holder.description.text = tools.getOrNull(position)?.subtitle
        if (tools.getOrNull(position)?.moduleName == "Breathing"){
             holder.icon.setImageResource(R.drawable.breathing_male)
        }else if (tools.getOrNull(position)?.moduleName == "Affirmation"){
            holder.icon.setImageResource(R.drawable.write_note_icon)
        }

        holder.selectButton.setImageResource(if (tool.isSelectedModule == true) R.drawable.green_check_item else R.drawable.add_think_brown_icon)

        holder.selectButton.setOnClickListener {
            onItemClick(position,tool)
        }
    }

    override fun getItemCount(): Int = tools.size
}

class FilterAdapter(private val filters: List<FilterItem>, private val onFilterClick: (Int,FilterItem) -> Unit) :
    RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterText: TextView = itemView.findViewById(R.id.filter_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter = filters[position]
        holder.filterText.text = filter.name
        holder.filterText.setBackgroundResource(if (filter.isSelected) R.drawable.brown_bg else R.drawable.filter_white_bg)
        holder.filterText.setTextColor(if (filter.isSelected) Color.WHITE else Color.BLACK)

        holder.filterText.setOnClickListener {
            filters.forEach { it.isSelected = false }
            filters[position].isSelected = true
            notifyDataSetChanged()
            onFilterClick(position,filter)
        }

    }

    override fun getItemCount(): Int = filters.size
}


class AddToolsFragment: BaseFragment<FragmentAllToolsListBinding>() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var thinkQuoteResponse : ThinkQuoteResponse
    private lateinit var toolsResponse : ToolsResponse
    private lateinit var toolsAdapter: ToolsAdapterList
    private lateinit var filterRecyclerView: RecyclerView
    private lateinit var filterAdapter: FilterAdapter
   // private var tools = mutableListOf<ToolsData>()
    var tools: ArrayList<ToolsData> = ArrayList()
   var toolsId = ""
    var userId =""
    var moduleName = ""
    var moduleId = ""
    var subtitle = ""
    var moduleType = ""
    var categoryId = ""
    var isSelectedModule = false

    private val filters = mutableListOf(
        FilterItem("All", true),
        FilterItem("Breathing", false),
        FilterItem("Journaling", false),
        FilterItem("Affirmations", false)
    )


    private lateinit var sleepStageResponse: SleepStageResponse

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAllToolsListBinding
        get() = FragmentAllToolsListBinding::inflate
    var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        val backBtn = view.findViewById<ImageView>(R.id.img_back)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        backBtn.setOnClickListener {
            navigateToFragment(HomeBottomTabFragment(), "ThinkRightReportFragment")
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(ThinkRightReportFragment(), "ThinkRightReportFragment")
            }
        })
        fetchToolsList("")

        filterRecyclerView = view.findViewById(R.id.filter_recycler_view)
        filterRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterAdapter = FilterAdapter(filters) { position, filterItem ->
            if (filterItem.name == "All") {
                fetchToolsList("")
            }else  {
                tools.clear()
                fetchToolsList(filterItem.name)

            }
        }
        filterRecyclerView.adapter = filterAdapter

        toolsAdapter = ToolsAdapterList(tools) { position,toolsData ->
            tools[position].isSelectedModule = !tools[position].isSelectedModule!!
            toolsAdapter.notifyItemChanged(position)
             toolsId = toolsData.toolsId ?: ""
             userId = toolsData.userId ?: ""
             moduleName = toolsData.moduleName ?: ""
             moduleId = toolsData.moduleId ?: ""
             subtitle = toolsData.subtitle ?: ""
             moduleType = toolsData.moduleType ?: ""
             categoryId = toolsData.categoryId ?: ""
             isSelectedModule = toolsData.isSelectedModule ?: false
            selectTools()
        }
        recyclerView.adapter = toolsAdapter
    }

    private fun fetchToolsList( filterKey:String) {
        progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdlM2ZiMjdiMzNlZGZkNzRlMDY5OWFjIiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiIiLCJsYXN0TmFtZSI6IiIsImRldmljZUlkIjoiVEUxQS4yNDAyMTMuMDA5IiwibWF4RGV2aWNlUmVhY2hlZCI6ZmFsc2UsInR5cGUiOiJhY2Nlc3MtdG9rZW4ifSwiaWF0IjoxNzQzMDU2OTEwLCJleHAiOjE3NTg3ODE3MTB9.gYLi895fpb4HGitALoGDRwHw3MIDCjYXTyqAKDNjS0A"
        val userId = "67a5fae9197992511e71b1c8"
        val filter_key = filterKey
        if (filter_key == ""){
            val call = ApiClient.apiService.fetchToolsListAll(token,userId)
            call.enqueue(object : Callback<ToolsResponse> {
                override fun onResponse(call: Call<ToolsResponse>, response: Response<ToolsResponse>) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()
                        toolsResponse = response.body()!!
                        for (i in 0 until toolsResponse.data.size) {
                            toolsResponse.data.getOrNull(i)?.let {
                                tools.add(it) }
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
        }else{
            val call = ApiClient.apiService.fetchToolsList(token,userId,filter_key)
            call.enqueue(object : Callback<ToolsResponse> {
                override fun onResponse(call: Call<ToolsResponse>, response: Response<ToolsResponse>) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()
                        toolsResponse = response.body()!!
                        for (i in 0 until toolsResponse.data.size) {
                            toolsResponse.data.getOrNull(i)?.let {
                                tools.add(it) }
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

    }

    private fun selectTools() {
        progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdlM2ZiMjdiMzNlZGZkNzRlMDY5OWFjIiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiIiLCJsYXN0TmFtZSI6IiIsImRldmljZUlkIjoiVEUxQS4yNDAyMTMuMDA5IiwibWF4RGV2aWNlUmVhY2hlZCI6ZmFsc2UsInR5cGUiOiJhY2Nlc3MtdG9rZW4ifSwiaWF0IjoxNzQzMDU2OTEwLCJleHAiOjE3NTg3ODE3MTB9.gYLi895fpb4HGitALoGDRwHw3MIDCjYXTyqAKDNjS0A"
        val call = ApiClient.apiService.selectTools(token,AddToolRequest(toolId = toolsId, userId =userId, moduleName = moduleName, moduleId = moduleId, subtitle = subtitle, moduleType = moduleType, categoryId = categoryId, isSelectedModule = isSelectedModule))
        call.enqueue(object : Callback<ToolsResponse> {
            override fun onResponse(call: Call<ToolsResponse>, response: Response<ToolsResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    toolsResponse = response.body()!!
                    if (toolsResponse.data != null) {
                        for (i in 0 until toolsResponse.data.size) {
                            toolsResponse.data.getOrNull(i)?.let { tools.add(it) }
                        }
                    } else {
                        Log.e("AddToolsFragment", "toolsResponse.data is null")
                    }
                    Toast.makeText(requireContext(),"${moduleName} Added Successfully!", Toast.LENGTH_SHORT).show()
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

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
}

data class FilterItem(
    val name: String,
    var isSelected: Boolean
)

