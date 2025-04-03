package com.jetsynthesys.rightlife.ui.drawermenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PreferencePagerAdapter extends FragmentStateAdapter {

    private PreferenceData preferenceData;

    public PreferencePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        OnboardingPrompt onboardingPrompt = preferenceData.getOnboardingPrompt().get(position);
        return PreferenceFragment.newInstance(onboardingPrompt);
    }

    @Override
    public int getItemCount() {
        if (preferenceData != null) {
            return preferenceData.getOnboardingPrompt().size();
        } else return 0;
    }

    public void setPreferenceData(PreferenceData preferenceData) {
        this.preferenceData = preferenceData;
    }
}
