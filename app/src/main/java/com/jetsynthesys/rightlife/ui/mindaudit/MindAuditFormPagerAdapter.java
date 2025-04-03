package com.jetsynthesys.rightlife.ui.mindaudit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


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
            /*case 2:
                return MindAuditReasonsFragment.newInstance(position);*/
            // Continue for each page up to

            default:
                return  MindAuditFeelingsFragment.newInstance(position);
            //throw new IllegalArgumentException("Invalid page position");
        }
    }

    @Override
    public int getItemCount() {
        return 1; // Number of pages in the form
    }
}



