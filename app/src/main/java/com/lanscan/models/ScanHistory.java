package com.lanscan.models;

public class ScanHistory {
    private long id;
    private String target;
    private String result;
    private long timestamp;

    public ScanHistory(long id, String target, String result, long timestamp) {
        this.id = id;
        this.target = target;
        this.result = result;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public String getTarget() { return target; }
    public String getResult() { return result; }
    public long getTimestamp() { return timestamp; }
}
