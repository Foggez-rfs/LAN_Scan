package com.lanscan.models;

public class Port {
    private int number;
    private String protocol;
    private String service;
    private String version;

    public Port(int number, String protocol, String service, String version) {
        this.number = number;
        this.protocol = protocol;
        this.service = service;
        this.version = version;
    }

    public int getNumber() { return number; }
    public String getProtocol() { return protocol; }
    public String getService() { return service; }
    public String getVersion() { return version; }

    public String toString() {
        return String.format("%d/%s %s %s", number, protocol, service, version);
    }
}
