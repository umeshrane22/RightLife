package com.example.rlapp.ui.mindaudit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.rlapp.ui.voicescan.FeelingListFragment;
import com.example.rlapp.ui.voicescan.FeelingReasonsFragment;
import com.example.rlapp.ui.voicescan.VoiceRecordFragment;


public class MindAuditFormPagerAdapter extends FragmentStateAdapter {

    public MindAuditFormPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return MindAuditFeelingsFragment.newInstance(position);
            case 1:
                return MindAuditReasonsFragment.newInstance(position);
            /*case 2:
                return MindAuditReasonsFragment.newInstance(position);*/
            // Continue for each page up to

            default:
                return  MindAuditReasonsFragment.newInstance(position);
            //throw new IllegalArgumentException("Invalid page position");
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of pages in the form
    }
}



