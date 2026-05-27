package com.lanscan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lanscan.R;
import com.lanscan.models.Port;
import java.util.List;

public class PortAdapter extends RecyclerView.Adapter<PortAdapter.ViewHolder> {
    private List<Port> ports;

    public PortAdapter(List<Port> ports) { this.ports = ports; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_port, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        Port p = ports.get(pos);
        h.number.setText(p.getNumber() + "/" + p.getProtocol());
        h.service.setText(p.getService());
        h.version.setText(p.getVersion());
    }

    @Override
    public int getItemCount() { return ports.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, service, version;
        ViewHolder(View v) {
            super(v);
            number = v.findViewById(R.id.portNumber);
            service = v.findViewById(R.id.portService);
            version = v.findViewById(R.id.portVersion);
        }
    }
}
