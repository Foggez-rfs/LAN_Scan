package com.lanscan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lanscan.R;
import com.lanscan.database.CVEDatabase;
import java.util.List;

public class CVEAdapter extends RecyclerView.Adapter<CVEAdapter.ViewHolder> {
    private List<CVEDatabase.CVEInfo> cves;

    public CVEAdapter(List<CVEDatabase.CVEInfo> cves) { this.cves = cves; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cve, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        CVEDatabase.CVEInfo cve = cves.get(pos);
        h.id.setText(cve.id);
        h.score.setText(String.format("CVSS: %.1f (%s)", cve.cvss, cve.severity));
        h.desc.setText(cve.description);
    }

    @Override
    public int getItemCount() { return cves.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, score, desc;
        ViewHolder(View v) {
            super(v);
            id = v.findViewById(R.id.cveId);
            score = v.findViewById(R.id.cveScore);
            desc = v.findViewById(R.id.cveDesc);
        }
    }
}
