package com.example.rlapp.ui.voicescan;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rlapp.R;

public class FeelingReasonsFragment extends Fragment {

    private static final String ARG_PAGE_INDEX = "page_index";
    private static final String ARG_FEELINGS = "FEELINGS";
    private int pageIndex;
    private String feelings;

    private TextView txtFeelingsTitle, tvWork, tvSchool, tvFamily, tvHealth, tvMoney, tvFriends, tvRelationships;
    private ImageView imgFeelings;
    private boolean isWorkSelected = false, isSchoolSelected = false, isFamilySelected = false,
            isHealthSelected = false, isMoneySelected = false, isFriendsSelected = false, isRelationshipSelected = false;


    public static FeelingReasonsFragment newInstance(int pageIndex, String feelings) {
        FeelingReasonsFragment fragment = new FeelingReasonsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        args.putString(ARG_FEELINGS, feelings);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
            feelings = getArguments().getString(ARG_FEELINGS);
            Log.d("AAAAA", "Feelings = " + feelings);
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_feelings_reasons, container, false);

        txtFeelingsTitle = view.findViewById(R.id.txt_title_feeling);
        imgFeelings = view.findViewById(R.id.icon_feelings);
        txtFeelingsTitle.setText(feelings);


        switch (feelings) {
            case "Sad":
                imgFeelings.setImageResource(R.drawable.vociesscan_sad);
                break;
            case "Stressed":
                imgFeelings.setImageResource(R.drawable.voicescan_stressed);
                break;
            case "Unsure":
                imgFeelings.setImageResource(R.drawable.voicescan_unsure);
                break;
            case "Relaxed":
                imgFeelings.setImageResource(R.drawable.voicescan_relaxed);
                break;
            case "Happy":
                imgFeelings.setImageResource(R.drawable.voicescan_happy);
                break;
        }

        tvWork = view.findViewById(R.id.tv_work);
        tvSchool = view.findViewById(R.id.tv_school);
        tvFamily = view.findViewById(R.id.tv_family);
        tvHealth = view.findViewById(R.id.tv_health);
        tvMoney = view.findViewById(R.id.tv_money);
        tvFriends = view.findViewById(R.id.tv_friends);
        tvRelationships = view.findViewById(R.id.tv_relationship);

        tvWork.setOnClickListener(view1 -> {
            if (isWorkSelected) {
                isWorkSelected = false;
                tvWork.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape));
                tvWork.setTextColor(Color.BLACK);
            } else {
                isWorkSelected = true;
                tvWork.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape_selected));
                tvWork.setTextColor(Color.WHITE);
            }
        });

        tvSchool.setOnClickListener(view1 -> {
            if (isSchoolSelected) {
                isSchoolSelected = false;
                tvSchool.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape));
                tvSchool.setTextColor(Color.BLACK);
            } else {
                isSchoolSelected = true;
                tvSchool.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape_selected));
                tvSchool.setTextColor(Color.WHITE);
            }
        });

        tvFamily.setOnClickListener(view1 -> {
            if (isFamilySelected) {
                isFamilySelected = false;
                tvFamily.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape));
                tvFamily.setTextColor(Color.BLACK);
            } else {
                isFamilySelected = true;
                tvFamily.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape_selected));
                tvFamily.setTextColor(Color.WHITE);
            }
        });

        tvHealth.setOnClickListener(view1 -> {
            if (isHealthSelected) {
                isHealthSelected = false;
                tvHealth.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape));
                tvHealth.setTextColor(Color.BLACK);
            } else {
                isHealthSelected = true;
                tvHealth.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape_selected));
                tvHealth.setTextColor(Color.WHITE);
            }
        });

        tvMoney.setOnClickListener(view1 -> {
            if (isMoneySelected) {
                isMoneySelected = false;
                tvMoney.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape));
                tvMoney.setTextColor(Color.BLACK);
            } else {
                isMoneySelected = true;
                tvMoney.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape_selected));
                tvMoney.setTextColor(Color.WHITE);
            }
        });

        tvFriends.setOnClickListener(view1 -> {
            if (isFriendsSelected) {
                isFriendsSelected = false;
                tvFriends.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape));
                tvFriends.setTextColor(Color.BLACK);
            } else {
                isFriendsSelected = true;
                tvFriends.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape_selected));
                tvFriends.setTextColor(Color.WHITE);
            }
        });

        tvRelationships.setOnClickListener(view1 -> {
            if (isRelationshipSelected) {
                isRelationshipSelected = false;
                tvRelationships.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape));
                tvRelationships.setTextColor(Color.BLACK);
            } else {
                isRelationshipSelected = true;
                tvRelationships.setBackground(requireActivity().getDrawable(R.drawable.roundedcornershape_selected));
                tvRelationships.setTextColor(Color.WHITE);
            }
        });

        return view;
    }
}
