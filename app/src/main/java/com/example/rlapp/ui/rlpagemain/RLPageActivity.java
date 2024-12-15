package com.example.rlapp.ui.rlpagemain;

import android.content.Intent;
import android.os.Bundle;

import com.example.rlapp.apimodel.exploremodules.affirmations.ExploreAffirmationsListActivity;
import com.example.rlapp.ui.HomeActivity;
import com.example.rlapp.ui.exploremodule.ExploreModuleListActivity;
import com.example.rlapp.ui.exploremodule.ExploreSleepSoundsActivity;
import com.example.rlapp.ui.healthpagemain.HealthPageMainActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rlapp.R;
import com.example.rlapp.databinding.ActivityRlpageBinding;

public class RLPageActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomSheetBehavior<View> bottomSheetBehavior;

    private FloatingActionButton add_fab;
LinearLayout rlmenu,ll_homemenuclick,bottom_sheet,
        ll_journal,ll_affirmations,ll_sleepsounds;
    private ImageView img_homemenu,img_healthmenu,quicklinkmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_rlpage);

        rlmenu = findViewById(R.id.rlmenu);
        rlmenu.setOnClickListener(this);

        //img_homemenu = findViewById(R.id.img_homemenu);
        //img_homemenu.setOnClickListener(this);

        rlmenu = findViewById(R.id.rlmenu);
        rlmenu.setOnClickListener(this);


                quicklinkmenu = findViewById(R.id.quicklinkmenu);
        quicklinkmenu.setOnClickListener(this);

        ll_homemenuclick = findViewById(R.id.ll_homemenuclick);
        ll_homemenuclick.setOnClickListener(this);

        ll_journal = findViewById(R.id.ll_journal);
        ll_journal.setOnClickListener(this);
        ll_affirmations = findViewById(R.id.ll_affirmations);
        ll_affirmations.setOnClickListener(this);
        ll_sleepsounds = findViewById(R.id.ll_sleepsounds);
        ll_sleepsounds.setOnClickListener(this);


        bottom_sheet = findViewById(R.id.bottom_sheet);
       // bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // Initially hide the bottom sheet
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        /*binding = ActivityRlpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());*/


       /* Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.rlmenu) {
            //Toast.makeText(HealthPageMainActivity.this, "RLpage clicked", Toast.LENGTH_SHORT).show();
            // Start new activity here
            Intent intent = new Intent(RLPageActivity.this, RLPageActivity.class);
            // Optionally pass data
            //intent.putExtra("key", "value");
            //startActivity(intent);
        }else if (viewId == R.id.ll_homemenuclick) {
            Intent intent = new Intent(RLPageActivity.this, HomeActivity.class);
            // Optionally pass data
            //intent.putExtra("key", "value");
            startActivity(intent);
            finish();
        } else if (viewId == R.id.quicklinkmenu) {
           /* if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }*/
            if (bottom_sheet.getVisibility()==View.VISIBLE){
             bottom_sheet.setVisibility(View.GONE);
            }else {
                bottom_sheet.setVisibility(View.VISIBLE);
            }
            view.setSelected(!view.isSelected());
        } else if (viewId == R.id.ll_journal) {
            Toast.makeText(RLPageActivity.this, "journal clicked", Toast.LENGTH_SHORT).show();

        } else if (viewId == R.id.ll_affirmations) {
            Toast.makeText(RLPageActivity.this, "Affirmations clicked", Toast.LENGTH_SHORT).show();
             startActivity(new Intent(RLPageActivity.this, ExploreAffirmationsListActivity.class));
        } else if (viewId == R.id.ll_sleepsounds) {
            Toast.makeText(RLPageActivity.this, "sleepsounds clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RLPageActivity.this, ExploreSleepSoundsActivity.class));
        }
    }
}