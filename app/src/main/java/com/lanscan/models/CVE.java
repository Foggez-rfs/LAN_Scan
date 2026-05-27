package com.lanscan.models;

public class CVE {
    private String id;
    private double cvss;
    private String severity;
    private String description;

    public CVE(String id, double cvss, String severity, String description) {
        this.id = id;
        this.cvss = cvss;
        this.severity = severity;
        this.description = description;
    }

    public String getId() { return id; }
    public double getCvss() { return cvss; }
    public String getSeverity() { return severity; }
    public String getDescription() { return description; }
}
