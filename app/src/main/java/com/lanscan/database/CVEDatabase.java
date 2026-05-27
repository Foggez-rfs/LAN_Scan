package com.lanscan.database;

import android.content.Context;
import java.io.*;
import java.util.*;

public class CVEDatabase {
    private Map<String, CVEInfo> cveMap = new HashMap<>();
    private List<CVEInfo> allCVEs = new ArrayList<>();

    public static class CVEInfo {
        public String id;
        public String description;
        public double cvss;
        public String severity;
        public String vector;
    }

    public CVEDatabase(Context context) {
        try {
            InputStream in = context.getAssets().open("databases/cve_recent.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length >= 4) {
                    CVEInfo info = new CVEInfo();
                    info.id = parts[0];
                    info.description = parts[1];
                    info.cvss = parseDouble(parts[2]);
                    info.severity = parts[3];
                    info.vector = parts.length > 4 ? parts[4] : "";
                    cveMap.put(info.id, info);
                    allCVEs.add(info);
                }
            }
            reader.close();
        } catch (Exception ignored) {}
    }

    public CVEInfo getCVE(String id) {
        return cveMap.get(id);
    }

    public List<CVEInfo> searchByService(String service) {
        List<CVEInfo> result = new ArrayList<>();
        String lower = service.toLowerCase();
        for (CVEInfo info : allCVEs) {
            if (info.description.toLowerCase().contains(lower)) {
                result.add(info);
            }
        }
        return result;
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (Exception e) { return 0.0; }
    }
}
