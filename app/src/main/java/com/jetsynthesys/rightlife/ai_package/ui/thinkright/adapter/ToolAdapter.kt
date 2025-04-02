package com.jetsynthesys.rightlife.ai_package.ui.thinkright.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.model.ModuleData

class ToolAdapter(
    private val tools: List<ModuleData>, val onToolItem: (ModuleData, Int, Boolean) -> Unit
) : RecyclerView.Adapter<ToolAdapter.ToolViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tool, parent, false)
        return ToolViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        val tool = tools[position]
        holder.bind(tool)

        holder.mainLayout.setOnClickListener {
            tools?.getOrNull(position)?.let { it1 -> onToolItem(it1, position, true) }
        }
    }

    override fun getItemCount(): Int = tools.size

    class ToolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val moduleName: TextView = itemView.findViewById(R.id.tv_module_name)
        private val moduleType: TextView = itemView.findViewById(R.id.tv_module_type)
        private val subtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val selectedIcon: ImageView = itemView.findViewById(R.id.iv_selected)
        val mainLayout : CardView = itemView.findViewById<CardView>(R.id.mainLayout)

        fun bind(tool: ModuleData) {
            moduleName.text = tool.moduleName
            if (tool.moduleName.equals("Affirmation")) {
                selectedIcon.setImageResource(R.drawable.affirmation_bookmark) // Replace with your actual drawable
            } else {
                selectedIcon.setImageResource(R.drawable.breathwork_icon) // Replace with your fallback drawable
            }
           // moduleType.text = tool.moduleType
           // subtitle.text = tool.subtitle
            //selectedIcon.visibility = if (tool.isSelectedModule) View.VISIBLE else View.GONE
        }
    }
}