package com.lanscan.models;

import java.util.*;

public class ScanResult {
    private String target;
    private int mode;
    private String rawOutput;
    private long timestamp;
    private List<Port> ports = new ArrayList<>();
    private List<String> cves = new ArrayList<>();
    private List<String> devices = new ArrayList<>();

    public ScanResult(String target, int mode, String rawOutput) {
        this.target = target;
        this.mode = mode;
        this.rawOutput = rawOutput;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTarget() { return target; }
    public int getMode() { return mode; }
    public String getRawOutput() { return rawOutput; }
    public long getTimestamp() { return timestamp; }
    public List<Port> getPorts() { return ports; }
    public List<String> getCves() { return cves; }
    public List<String> getDevices() { return devices; }

    public void setPorts(List<Port> ports) { this.ports = ports; }
    public void setCves(List<String> cves) { this.cves = cves; }
    public void setDevices(List<String> devices) { this.devices = devices; }
}
