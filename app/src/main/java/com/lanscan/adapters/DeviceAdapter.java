package com.lanscan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lanscan.R;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private List<String> devices;

    public DeviceAdapter(List<String> devices) { this.devices = devices; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        h.ip.setText(devices.get(pos));
    }

    @Override
    public int getItemCount() { return devices.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ip, mac;
        ViewHolder(View v) {
            super(v);
            ip = v.findViewById(R.id.deviceIp);
            mac = v.findViewById(R.id.deviceMac);
        }
    }
}
