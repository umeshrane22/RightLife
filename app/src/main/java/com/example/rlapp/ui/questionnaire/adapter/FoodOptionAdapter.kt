import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.databinding.ItemFoodOptionBinding
import com.example.rlapp.ui.questionnaire.pojo.FoodOption

class FoodOptionAdapter(
    private val options: List<FoodOption>,
    private val onItemClick: (FoodOption) -> Unit
) : RecyclerView.Adapter<FoodOptionAdapter.FoodOptionViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class FoodOptionViewHolder(val binding: ItemFoodOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(option: FoodOption, isSelected: Boolean) {
            binding.tvFoodTitle.text = option.title
            binding.tvFoodSubTitle.text = option.subTitle
            binding.ivFoodIcon.setImageResource(option.iconRes)

            // Change background on selection
            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.bg_food_item_selected)
            } else {
                binding.root.setBackgroundResource(R.drawable.bg_food_item)
            }

            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onItemClick(option)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodOptionViewHolder {
        val binding = ItemFoodOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodOptionViewHolder, position: Int) {
        holder.bind(options[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = options.size
}
