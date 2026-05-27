package com.lanscan;

import android.content.Context;
import java.io.*;
import java.util.*;

public class CVEDatabase {
    private Map<String, CVEInfo> cveMap = new HashMap<>();
    
    public static class CVEInfo {
        public String id;
        public String description;
        public double cvss;
        public String severity;
        public String vector;
    }
    
    public CVEDatabase(Context ctx) {
        try {
            InputStream in = ctx.getAssets().open("cve.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    CVEInfo info = new CVEInfo();
                    info.id = parts[0];
                    info.description = parts[1];
                    info.cvss = Double.parseDouble(parts[2]);
                    info.severity = parts[3];
                    info.vector = parts.length > 4 ? parts[4] : "";
                    cveMap.put(info.id, info);
                }
            }
            reader.close();
        } catch (Exception e) {
            // база не загрузилась
        }
    }
    
    public CVEInfo getCVE(String id) {
        return cveMap.get(id);
    }
    
    public List<CVEInfo> searchByService(String service) {
        List<CVEInfo> result = new ArrayList<>();
        for (CVEInfo info : cveMap.values()) {
            if (info.description.toLowerCase().contains(service.toLowerCase())) {
                result.add(info);
            }
        }
        return result;
    }
    
    public List<CVEInfo> searchByVersion(String service, String version) {
        List<CVEInfo> result = new ArrayList<>();
        String search = service + " " + version;
        for (CVEInfo info : cveMap.values()) {
            if (info.description.toLowerCase().contains(search.toLowerCase())) {
                result.add(info);
            }
        }
        return result;
    }
}
