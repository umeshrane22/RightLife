package com.jetsynthesys.rightlife.ui.jounal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.apimodel.rlpagemodels.journal.Journals;
import com.jetsynthesys.rightlife.ui.utility.DateTimeUtils;

import java.util.List;

public class JournalRecyclerViewAdapter extends RecyclerView.Adapter<JournalRecyclerViewAdapter.ViewHolder> {
    List<Journals> journalsList;
    private LayoutInflater inflater;
    private Context ctx;


    public JournalRecyclerViewAdapter(JournalingListActivity journalingListActivity, List<Journals> journalsList) {
        this.inflater = LayoutInflater.from(journalingListActivity);
        this.ctx = journalingListActivity;
        this.journalsList = journalsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_journal_list, parent, false);
        return new JournalRecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Journals journal = journalsList.get(position);
        holder.tvTimestamp.setText(DateTimeUtils.convertAPIDateMonthFormatWithTime(journal.getUpdatedAt()));
        holder.tvTitle.setText(journal.getTitle());
        holder.tvDescription.setText(journal.getJournal());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(ctx, MyJournalActivity.class);
            intent.putExtra("Journal", journal);
            ctx.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return journalsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimestamp, tvTitle, tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
