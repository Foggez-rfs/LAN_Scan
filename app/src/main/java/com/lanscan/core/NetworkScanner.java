package com.lanscan.core;

import android.content.Context;
import com.lanscan.models.ScanResult;
import com.lanscan.models.Port;
import com.lanscan.database.CVEDatabase;
import java.util.*;

public class NetworkScanner {
    private Context context;
    private NmapExecutor executor;

    private static final Map<Integer, String> SCAN_MODES = new HashMap<>();
    static {
        SCAN_MODES.put(1, "-Pn --top-ports 100 --min-rate 1000");
        SCAN_MODES.put(2, "-sV -Pn --version-intensity 5");
        SCAN_MODES.put(3, "-O -Pn --osscan-guess");
        SCAN_MODES.put(4, "-sC -Pn --script=default");
        SCAN_MODES.put(5, "-A -Pn -T4");
        SCAN_MODES.put(6, "-p- -Pn --min-rate 5000");
        SCAN_MODES.put(7, "-sU -Pn --top-ports 100");
        SCAN_MODES.put(8, "-Pn --script vuln --script-args mincvss=5.0");
        SCAN_MODES.put(9, "-sC -sV -A -v -Pn -T4 --script=vuln,exploit,auth,default");
        SCAN_MODES.put(10, "-p- -sC -sV -A -Pn -T5 --script=vuln,exploit --min-rate 10000");
    }

    public NetworkScanner(Context context) {
        this.context = context;
        this.executor = new NmapExecutor();
    }

    public ScanResult scan(String target, int mode) {
        String args = SCAN_MODES.get(mode);
        if (args == null) {
            args = "-sV -Pn";
        }
        String output = executor.execute(args + " " + target);
        ScanResult result = new ScanResult(target, mode, output);
        result.setPorts(parsePorts(output));
        result.setCves(parseCVEs(output));
        return result;
    }

    public ScanResult scanNetwork(String subnet) {
        if (subnet == null || subnet.isEmpty()) {
            subnet = "192.168.1.0/24";
        }
        String output = executor.execute("-sn " + subnet);
        ScanResult result = new ScanResult(subnet, 0, output);
        result.setDevices(parseDevices(output));
        return result;
    }

    private List<Port> parsePorts(String output) {
        List<Port> ports = new ArrayList<>();
        for (String line : output.split("\n")) {
            if ((line.contains("/tcp") || line.contains("/udp")) && line.contains("open")) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    String[] portProto = parts[0].split("/");
                    Port port = new Port(
                        Integer.parseInt(portProto[0]),
                        portProto[1],
                        parts[2],
                        parts.length > 3 ? parts[3] : ""
                    );
                    ports.add(port);
                }
            }
        }
        return ports;
    }

    private List<String> parseCVEs(String output) {
        List<String> cves = new ArrayList<>();
        for (String line : output.split("\n")) {
            if (line.contains("CVE-")) {
                int start = line.indexOf("CVE-");
                int end = Math.min(start + 13, line.length());
                String cve = line.substring(start, end).split(" ")[0];
                cve = cve.replaceAll("[.:,;\"')]}]", "");
                if (cve.startsWith("CVE-") && cve.length() >= 10) {
                    cves.add(cve);
                }
            }
        }
        return new ArrayList<>(new HashSet<>(cves));
    }

    private List<String> parseDevices(String output) {
        List<String> devices = new ArrayList<>();
        for (String line : output.split("\n")) {
            if (line.contains("Nmap scan report for")) {
                String[] parts = line.split(" ");
                devices.add(parts[parts.length - 1]);
            }
        }
        return devices;
    }
}
