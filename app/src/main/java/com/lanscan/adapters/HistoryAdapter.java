package com.lanscan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lanscan.R;
import com.lanscan.models.ScanResult;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<ScanResult> history;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(ScanResult result);
    }

    public HistoryAdapter(List<ScanResult> history, OnItemClickListener listener) {
        this.history = history;
        this.listener = listener;
    }

    public void updateList(List<ScanResult> newList) {
        this.history = newList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        ScanResult r = history.get(pos);
        h.target.setText(r.getTarget());
        h.date.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date(r.getTimestamp())));
        h.itemView.setOnClickListener(v -> listener.onClick(r));
    }

    @Override
    public int getItemCount() { return history.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView target, date;
        ViewHolder(View v) {
            super(v);
            target = v.findViewById(R.id.historyTarget);
            date = v.findViewById(R.id.historyDate);
        }
    }
}
