package com.example.rlapp.ui.new_design

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

class RulerAdapter(// Updated to handle float values
    private val numbers: List<Float>, private val listener: (Any) -> Unit
) :
    RecyclerView.Adapter<RulerAdapter.RulerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulerViewHolder {
        // Inflate the layout item for each ruler number
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.ruler_item, parent, false)
        return RulerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RulerViewHolder, position: Int) {
        val number = numbers[position]

        // Set the number text
        holder.numberText.text = number.toString()

        // Customize the line height
        if (number % 1 == 0f) { // Whole numbers get big lines
            holder.ruler_line_small.visibility = View.GONE
            holder.rulerLine.visibility = View.VISIBLE
            holder.numberText.visibility = View.VISIBLE
        } else {
            // Small line for decimal numbers
            holder.ruler_line_small.visibility = View.VISIBLE
            holder.rulerLine.visibility = View.GONE
            holder.numberText.visibility = View.GONE
        }

        // Set click listener to show number
        // Set click listener to show number
        holder.itemView.setOnClickListener {
            listener(number) // Use the lambda function to handle the selected number
        }
    }

    override fun getItemCount(): Int {
        return numbers.size
    }

    interface OnNumberSelectedListener {
        fun onNumberSelected(number: Float)
    }

    class RulerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var numberText: TextView = itemView.findViewById<TextView>(R.id.number_text)
        var rulerLine: View = itemView.findViewById<View>(R.id.ruler_line)
        var ruler_line_small: View = itemView.findViewById<View>(R.id.ruler_line_small)
    }
} /*
package com.example.pickerview;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class RulerAdapter extends RecyclerView.Adapter<RulerAdapter.RulerViewHolder> {

    private final List<Integer> numbers;
    private final OnNumberSelectedListener listener;

    public RulerAdapter(List<Integer> numbers, OnNumberSelectedListener listener) {
        this.numbers = numbers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RulerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout item for each ruler number
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ruler_item, parent, false);
        return new RulerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RulerViewHolder holder, int position) {
        int number = numbers.get(position);

        // Set the number text
        holder.numberText.setText(String.valueOf(number));

        // Customize the line height
        if (number % 5 == 0) {
            // Big line for multiples of 5
            //holder.rulerLine.setLayoutParams(new LinearLayout.LayoutParams(10, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.ruler_line_small.setVisibility(View.GONE);
            holder.rulerLine.setVisibility(View.VISIBLE);
            holder.numberText.setVisibility(View.VISIBLE);
        } else {
            // Small line for other numbers
            //holder.rulerLine.setLayoutParams(new LinearLayout.LayoutParams(3, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.ruler_line_small.setVisibility(View.VISIBLE);
            holder.rulerLine.setVisibility(View.GONE);
            holder.numberText.setVisibility(View.GONE);
        }

        // Set click listener to show number
        holder.itemView.setOnClickListener(v -> listener.onNumberSelected(number));
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public interface OnNumberSelectedListener {
        void onNumberSelected(int number);
    }

    static class RulerViewHolder extends RecyclerView.ViewHolder {
        TextView numberText;
        View rulerLine,ruler_line_small;

        RulerViewHolder(@NonNull View itemView) {
            super(itemView);
            numberText = itemView.findViewById(R.id.number_text);
            rulerLine = itemView.findViewById(R.id.ruler_line);
            ruler_line_small = itemView.findViewById(R.id.ruler_line_small);
        }
    }
}
*/
