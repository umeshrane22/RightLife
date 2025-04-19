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
import com.jetsynthesys.rightlife.ui.utility.ConversionUtils;
import com.jetsynthesys.rightlife.ui.utility.Utils;

import java.util.List;

public class HealthCamVitalsAdapter extends RecyclerView.Adapter<HealthCamVitalsAdapter.HealthCamVitalsViewHolder> {

    private List<HealthCamItem> healthCamItems;
    private Context context;

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
        String formatedValue = getFormatedValue(item.fieldName,String.valueOf(item.value));
        holder.binding.valueTextView.setText(String.valueOf(item.value));
        holder.binding.unitTextView.setText(item.unit);
        holder.binding.indicatorTextView.setText(item.indicator);
        holder.binding.parameterTextView.setText(item.parameter);
        holder.binding.rlMainBg.setBackgroundTintList(ColorStateList.valueOf(Utils.getColorFromColorCode(item.colour)));

        holder.itemView.setOnClickListener(view -> {
            context.startActivity(new Intent(context, FacialScanReportDetailsActivity.class));
        });

        // Set Indicator Image and Color based on item.indicator
   /*     if (item.indicator.equals("Normal")) {
            holder.binding.indicatorImageView.setImageResource(R.drawable.ic_indicator_normal); // Replace with your normal indicator image
            holder.binding.indicatorTextView.setTextColor(Color.parseColor("#008000")); // Green color
        } else if (item.indicator.equals("High")) {
            holder.binding.indicatorImageView.setImageResource(R.drawable.ic_indicator_high); // Replace with your high indicator image
            holder.binding.indicatorTextView.setTextColor(Color.parseColor("#FF0000")); // Red color
        } else if (item.indicator.equals("Low")) {
            holder.binding.indicatorImageView.setImageResource(R.drawable.ic_indicator_low); // Replace with your low indicator image
            holder.binding.indicatorTextView.setTextColor(Color.parseColor("#FFA500")); // Orange color
        } else if (item.indicator.equals("Optimal")) {
            holder.binding.indicatorImageView.setImageResource(R.drawable.ic_indicator_optimal); // Replace with your optimal indicator image
            holder.binding.indicatorTextView.setTextColor(Color.parseColor("#F5EE08")); // Yellow color
        }*/
        // ... set other indicator conditions and colors as needed

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


    private String getFormatedValue(String key, String value) {
        try {
            double val = Double.parseDouble(value);

            switch (key) {
                case "HRV_SDNN":
                case "PHYSIO_SCORE":
                case "BMI_CALC":
                case "BP_CVD":
                    return ConversionUtils.decimalFormat1Decimal.format(val);

                case "heartRateVariability":
                case "waistToHeight":

                case "systolic":
                case "BP_RPP":
                case "BP_SYSTOLIC":
                    return ConversionUtils.decimalFormat2Decimal.format(val);

                case "IHB_COUNT":
                case "MENTAL_SCORE":
                case "BP_DIASTOLIC":
                    return ConversionUtils.decimalFormat0Decimal.format(val);

                default:
                    return ConversionUtils.decimalFormat1Decimal.format(val);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return value;
        }
    }

}
