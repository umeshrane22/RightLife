package com.example.rlapp.ui.voicescan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.rlapp.ui.healthaudit.FormPageFragment;
import com.example.rlapp.ui.healthaudit.FormPagerAdapter;
import com.example.rlapp.ui.healthaudit.FruitListFragment;


public class VoiceScanFormPagerAdapter extends FragmentStateAdapter {

    private String feelings = "Sad";

    public VoiceScanFormPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return FeelingListFragment.newInstance(position);
            case 1:
                return FeelingReasonsFragment.newInstance(position,feelings);
            case 2:
                return VoiceRecordFragment.newInstance(position);
            // Continue for each page up to

            default:
                return  FeelingListFragment.newInstance(position);
            //throw new IllegalArgumentException("Invalid page position");
        }
    }

    public void setFeelings(String feelings) {
        this.feelings = feelings;
    }

    @Override
    public int getItemCount() {
        return 3; // Number of pages in the form
    }
}



