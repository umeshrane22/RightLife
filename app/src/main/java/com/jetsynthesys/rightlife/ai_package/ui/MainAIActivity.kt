package com.jetsynthesys.rightlife.ai_package.ui

import android.os.Bundle
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseActivity
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.SnapMealFragment
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.ActivityMainAiBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAIActivity : BaseActivity() {

    lateinit var bi: ActivityMainAiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = ActivityMainAiBinding.inflate(layoutInflater)
        setContentView(bi.root)

        val moduleName = intent.getStringExtra("ModuleName")
        val bottomSeatName = intent.getStringExtra("BottomSeatName")
        if (bottomSeatName.contentEquals("SnapMealTypeEat")){
            supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = SnapMealFragment()
                val args = Bundle()
                args.putString("ModuleName", "HomeDashboard")
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }else{
            supportFragmentManager.beginTransaction().apply {
                val homeBottomTabFragment = HomeBottomTabFragment()
                val args = Bundle()
                args.putString("ModuleName", moduleName)
                args.putString("BottomSeatName", bottomSeatName)
                homeBottomTabFragment.arguments = args
                replace(R.id.flFragment, homeBottomTabFragment, "homeBottom")
                addToBackStack(null)
                commit()
            }
        }
    }
}
