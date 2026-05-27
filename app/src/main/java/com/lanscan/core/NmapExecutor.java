package com.lanscan.core;

import java.io.*;

public class NmapExecutor {
    private static final String NMAP_PATH = "/data/local/tmp/nmap";
    private static final String NMAP_ASSET = "binaries/nmap_arm";

    public String execute(String args) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder pb = new ProcessBuilder();
            String[] cmd = (NMAP_PATH + " " + args).split(" ");
            pb.command(cmd);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            p.waitFor();
            reader.close();
        } catch (Exception e) {
            output.append("Error: ").append(e.getMessage());
        }
        return output.toString();
    }
}
