package com.jetsynthesys.rightlife.ui.healthcam;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.apimodel.newreportfacescan.HealthCamItem;
import com.jetsynthesys.rightlife.databinding.HealthCamVitalsItemBinding;
import com.jetsynthesys.rightlife.newdashboard.FacialScanReportDetailsActivity;
import com.jetsynthesys.rightlife.ui.NumberUtils;
import com.jetsynthesys.rightlife.ui.utility.Utils;

import java.util.List;

public class HealthCamVitalsAdapter extends RecyclerView.Adapter<HealthCamVitalsAdapter.HealthCamVitalsViewHolder> {

    private final List<HealthCamItem> healthCamItems;
    private final Context context;

    public HealthCamVitalsAdapter(Context context, List<HealthCamItem> healthCamItems) {
        this.healthCamItems = healthCamItems;
        this.context = context;
    }

    @NonNull
    @Override
    public HealthCamVitalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HealthCamVitalsItemBinding binding = HealthCamVitalsItemBinding.inflate(inflater, parent, false);
        return new HealthCamVitalsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthCamVitalsViewHolder holder, int position) {
        HealthCamItem item = healthCamItems.get(position);

        double val = NumberUtils.INSTANCE.smartRound(item.value, 2);

        holder.binding.valueTextView.setText(String.valueOf(val));
        holder.binding.unitTextView.setText(item.unit);
        holder.binding.indicatorTextView.setText(item.indicator);
        holder.binding.parameterTextView.setText(item.parameter);
        holder.binding.rlMainBg.setBackgroundTintList(ColorStateList.valueOf(Utils.getColorFromColorCode(item.colour)));

        holder.itemView.setOnClickListener(view -> {
            context.startActivity(new Intent(context, FacialScanReportDetailsActivity.class));
        });

    }

    private int getReportIconByType(String type) {
        switch (type) {
            case "BMI_CALC":
                return R.drawable.ic_db_report_bmi;
            case "BP_RPP":
                return R.drawable.ic_db_report_cardiak_workload;
            case "BP_SYSTOLIC":
                return R.drawable.ic_db_report_bloodpressure;
            case "BP_CVD":
                return R.drawable.ic_db_report_cvdrisk;
            case "MSI":
                return R.drawable.ic_db_report_stresslevel;
            case "BR_BPM":
                return R.drawable.ic_db_report_respiratory_rate;
            case "HRV_SDNN":
                return R.drawable.ic_db_report_heart_variability;
            default:
                return R.drawable.ic_db_report_heart_rate;
        }
    }

    @Override
    public int getItemCount() {
        return healthCamItems.size();
    }

    public static class HealthCamVitalsViewHolder extends RecyclerView.ViewHolder {
        HealthCamVitalsItemBinding binding;

        public HealthCamVitalsViewHolder(@NonNull HealthCamVitalsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}



/*
package com.example.rlapp.ui.healthcam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.apimodel.newreportfacescan.HealthCamItem;

import java.util.List;

public class HealthCamVitalsAdapter extends RecyclerView.Adapter<HealthCamVitalsAdapter.HealthCamVitalsViewHolder> {

    private List<HealthCamItem> healthCamItems;

    public HealthCamVitalsAdapter(List<HealthCamItem> healthCamItems) {
        this.healthCamItems = healthCamItems;
    }

    @NonNull
    @Override
    public HealthCamVitalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_cam_vitals_item, parent, false);
        return new HealthCamVitalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthCamVitalsViewHolder holder, int position) {
        HealthCamItem item = healthCamItems.get(position);

        holder.fieldName.setText(item.fieldName);
        holder.parameter.setText(item.parameter);
        holder.value.setText(String.valueOf(item.value));
        holder.unit.setText(item.unit);
        holder.indicator.setText(item.indicator);
        holder.definition.setText(item.deffination); // Definition
        holder.implication.setText(item.implication); // Implication

    }

    @Override
    public int getItemCount() {
        return healthCamItems.size();
    }

    public static class HealthCamVitalsViewHolder extends RecyclerView.ViewHolder {
        TextView fieldName;
        TextView parameter;
        TextView value;
        TextView unit;
        TextView indicator;
        TextView definition; // Definition
        TextView implication; // Implication


        public HealthCamVitalsViewHolder(@NonNull View itemView) {
            super(itemView);
            fieldName = itemView.findViewById(R.id.fieldName);
            parameter = itemView.findViewById(R.id.parameter);
            value = itemView.findViewById(R.id.value);
            unit = itemView.findViewById(R.id.unit);
            indicator = itemView.findViewById(R.id.indicator);
            definition = itemView.findViewById(R.id.definition); // Initialize
            implication = itemView.findViewById(R.id.implication); // Initialize

        }
    }
}*/
