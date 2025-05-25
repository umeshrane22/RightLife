package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.R.color.transparent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.jetsynthesys.rightlife.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MealSummaryInfoBottomSheet : BottomSheetDialogFragment() {

    private var mealName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_meal_summary_info_bottom_sheet, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog = BottomSheetDialog(requireContext(), R.style.LoggedBottomSheetDialogTheme)
        dialog.setContentView(R.layout.fragment_frequently_logged)
        dialog.window?.setBackgroundDrawableResource(transparent)
        val title = view.findViewById<TextView>(R.id.title)
        val close = view.findViewById<ImageView>(R.id.close)
        val mealSummary = view.findViewById<TextView>(R.id.tvMealSummary)
        val image = view.findViewById<ImageView>(R.id.image)

        image.setImageResource(R.drawable.meal_summary)
        title.text = "Your Meal Summary"

        close.setOnClickListener {
            dismiss()
        }
        val htmlContent = """
        <h2>What is Your Meal Summary?</h2>
        <p>Your diet plays a crucial role in your <b>energy levels, performance, and long-term health.</b> The <b>Your Meal Summary</b> feature breaks this down into three key areas:</p>
        <ul>
            <li>🔹 <b>Calories Consumed</b> → The energy you take in from food and drinks.</li>
            <li>🔹 <b>Macronutrients</b> → The balance of <b>Protein, Carbs, and Fats</b> in your diet.</li>
            <li>🔹 <b>Micronutrients</b> → The essential vitamins and minerals your body needs to function optimally.</li>
        </ul>

        <p>While <b>calorie tracking is important,</b> it’s <b>not just about how many calories you consume, but where those calories come from</b>. A diet high in <b>processed sugars and unhealthy fats</b> may fit within your calorie limit but can still lead to <b>nutrient deficiencies, metabolic imbalances, and poor long-term health.</b> RightLife helps you go beyond simple calorie counting by ensuring <b>a well-balanced intake of macros and micros for sustained energy, optimal performance, and overall well-being.</b></p>

        <h3>Calories In: What You Eat and Drink</h3>
        <p>Everything you eat and drink provides calories, which your body uses for energy.</p>
        <ul>
            <li>✔ <b>Meals & Snacks</b> → Every bite contributes to your calorie intake.</li>
            <li>✔ <b>Drinks</b> → Some drinks (like sodas, juices, and alcohol) contain calories, while others (like water, black coffee, and tea without sugar) don’t.</li>
            <li>✔ <b>Supplements & Shakes</b> → Protein shakes, meal replacements, and dietary supplements also count toward your intake.</li>
        </ul>

        <p>Tracking your calories is <b>only the first step—choosing the right foods ensures sustained energy and better health outcomes.</b></p>

        <h3>Macronutrients: Protein, Carbs, and Fats</h3>
        <p>Macronutrients fuel your body in different ways:</p>
        <ul>
            <li>✔ <b>Protein</b> → Supports muscle growth, repair, and overall health.</li>
            <li>✔ <b>Carbohydrates</b> → Provide energy for daily activity and exercise.</li>
            <li>✔ <b>Fats</b> → Help with energy storage, hormone regulation, and brain function.</li>
        </ul>

        <p>RightLife helps you <b>track your macro balance and provides personalized recommendations</b> to keep you on track.</p>

        <h3>Micronutrients: Essential Vitamins and Minerals</h3>
        <p>Micronutrients play a <b>critical role in metabolism, immunity, and overall well-being.</b> RightLife tracks key vitamins and minerals, including:</p>
        <ul>
            <li>✔ <b>Vitamins D, B12, Folate, C, A, K</b></li>
            <li>✔ <b>Iron, Calcium, Magnesium, Zinc</b></li>
            <li>✔ <b>Omega-3, Sodium, Cholesterol, Sugar</b></li>
        </ul>

        <p>RightLife pulls its <b>nutritional data from USDA FoodData Central, Indian Food Composition Tables (IFCT), and the European Food Information Resource (EuroFIR)</b>—ensuring <b>high accuracy and culturally relevant food tracking.</b></p>

        <h3>How to Use the Meal Summary in RightLife</h3>
        <p>RightLife simplifies <b>nutrition tracking</b> by integrating <b>smart logging, real-time feedback, and personalized meal recommendations.</b></p>
        <ol>
          <li><b>1Log Your Meals Easily</b>
            <ul>
              <li>Use <b>Log Meal</b> to manually enter ingredients, dishes, or pre-logged and saved recipes.</li>
              <li>Use <b>Snap Meal</b> for <b>AI-powered image recognition,</b> making food logging effortless.</li>
            </ul>
          </li>
          <li><b>2Track Your Nutrition in Real Time</b>
            <ul>
              <li>Monitor your <b>Calories, Macronutrients, and Micronutrients</b> in the <b>Today's Macros & Today's Micros</b> sections.</li>
              <li>Get <b>instant feedback</b> on whether you’re <b>meeting your daily nutrition goals.</b></li>
            </ul>
          </li>
          <li><b>3Follow AI-Powered Meal Plans</b>
            <ul>
              <li>RightLife also suggests <b>goal-aligned meal plans and recipes</b> based on your <b>dietary preferences, allergies, and nutritional targets.</b></li>
            </ul>
          </li>
        </ol>
        
        <h3>What RightLife Does for You</h3>
        <ul>
            <li>1) <b>Dynamically Adjusts Your Caloric Needs</b> – RightLife recalculates your <b>energy expenditure using a variation of the Katch-McArdle/Cunningham equation</b> to ensure <b>precise calorie targets.</b></li>
            <li>2) <b>Smart Deficit Adjustments</b> – Weekly updates <b>based on weight trends help you maintain optimal nutrition without guesswork.</b></li>
            <li>3) <b>AI-Powered Logging –  Snap Meal's machine-learning model</b> provides <b>best-in-class image recognition</b> for accurate food tracking.</li>
            <li>4) <b>Science-Backed Accuracy</b> – Uses data from <b>USDA, IFCT, and EuroFIR</b> to ensure that your macro and micronutrient breakdown is precise.</li>
        </ul>

        <h3>What You Need to Do</h3>
        <ul>
            <li>🔹 <b>Log Meals Regularly</b> – Whether Manually or via <b>Snap-to-Log</b> keeping your food log updated ensures <b>precise calorie tracking</b>.</li>
            <li>🔹 <b>Stay Aware of Your Macros & Micros</b>– Check your <b>daily intake balance</b> and adjust your meals as needed.</li>
            <li>🔹 <b>Follow Your Meal Plan –  Incorporate RightLife’s AI-driven meal recommendations</b> for a <b>more structured and effective diet</b>.</li>
            <li>🔹 <b>Monitor Your Deficit or Surplus</b>– Whether your goal is <b>weight loss, muscle gain, or maintenance</b>, staying on top of your calorie balance helps you progress <b>efficiently</b>.</li>
        </ul>

        <p><b>By consistently using <b>Your Meal Summary</b>, you can make <b>informed, science-backed dietary choices that align with your health goals. 🚀</b></p>
    """.trimIndent()
        mealSummary.text = Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY)
    }

    companion object {
        const val TAG = "LoggedBottomSheet"
        @JvmStatic
        fun newInstance() = MealSummaryInfoBottomSheet().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}

