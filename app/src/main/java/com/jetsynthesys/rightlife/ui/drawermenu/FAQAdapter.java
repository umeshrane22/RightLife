package com.jetsynthesys.rightlife.ui.drawermenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;

import java.util.ArrayList;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {
    private FAQData faqData;
    private Context context;

    public FAQAdapter(Context context, FAQData faqData) {
        this.context = context;
        this.faqData = faqData;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_faq_list, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        Faq item = faqData.getFaqs().get(position);
        holder.tvHeader.setText(item.getTitle());
        ArrayList<Detail> questionAns = (ArrayList<Detail>) item.getDetail();
        if (questionAns.size() == 1) {
            holder.llFAQ1.setVisibility(View.VISIBLE);
            holder.llFAQ2.setVisibility(View.GONE);
            holder.llFAQ3.setVisibility(View.GONE);
            holder.tvQuestion1.setText(questionAns.get(0).getQuestion());
            holder.tvAns1.setText(questionAns.get(0).getAnswer());
        } else if (questionAns.size() == 2) {
            holder.llFAQ1.setVisibility(View.VISIBLE);
            holder.llFAQ2.setVisibility(View.VISIBLE);
            holder.llFAQ3.setVisibility(View.GONE);
            holder.tvQuestion1.setText(questionAns.get(0).getQuestion());
            holder.tvAns1.setText(questionAns.get(0).getAnswer());
            holder.tvQuestion2.setText(questionAns.get(1).getQuestion());
            holder.tvAns2.setText(questionAns.get(1).getAnswer());
        }
        else if (questionAns.size() == 3) {
            holder.llFAQ1.setVisibility(View.VISIBLE);
            holder.llFAQ2.setVisibility(View.VISIBLE);
            holder.llFAQ3.setVisibility(View.VISIBLE);
            holder.tvQuestion1.setText(questionAns.get(0).getQuestion());
            holder.tvAns1.setText(questionAns.get(0).getAnswer());
            holder.tvQuestion2.setText(questionAns.get(1).getQuestion());
            holder.tvAns2.setText(questionAns.get(1).getAnswer());
            holder.tvQuestion3.setText(questionAns.get(2).getQuestion());
            holder.tvAns3.setText(questionAns.get(2).getAnswer());
        }


        if (item.isExpanded()) {
            holder.expandedView.setVisibility(View.VISIBLE);
            holder.tvHeader.setTextColor(context.getColor(R.color.rightlife));
        } else {
            holder.expandedView.setVisibility(View.GONE);
            holder.tvHeader.setTextColor(context.getColor(R.color.black));
        }

        holder.cardView.setOnClickListener(view -> {
            item.setExpanded(!item.isExpanded());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return faqData.getFaqs().size();
    }

    static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader, tvQuestion1, tvQuestion2, tvQuestion3, tvAns1, tvAns2, tvAns3;
        RelativeLayout expandedView;
        LinearLayout cardView, llFAQ1, llFAQ2, llFAQ3;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion1 = itemView.findViewById(R.id.tv_question1);
            tvQuestion2 = itemView.findViewById(R.id.tv_question2);
            tvQuestion3 = itemView.findViewById(R.id.tv_question3);

            tvAns1 = itemView.findViewById(R.id.tv_ans1);
            tvAns2 = itemView.findViewById(R.id.tv_ans2);
            tvAns3 = itemView.findViewById(R.id.tv_ans3);

            llFAQ1 = itemView.findViewById(R.id.ll_faq1);
            llFAQ2 = itemView.findViewById(R.id.ll_faq2);
            llFAQ3 = itemView.findViewById(R.id.ll_faq3);

            tvHeader = itemView.findViewById(R.id.tv_header);
            expandedView = itemView.findViewById(R.id.expanded_view);
            cardView = itemView.findViewById(R.id.card_layout);
        }
    }
}
