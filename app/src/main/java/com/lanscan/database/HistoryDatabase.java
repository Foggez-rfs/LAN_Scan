package com.lanscan.database;

import android.content.Context;
import android.content.SharedPreferences;
import com.lanscan.models.ScanResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;

public class HistoryDatabase {
    private SharedPreferences prefs;
    private Gson gson = new Gson();

    public HistoryDatabase(Context context) {
        prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE);
    }

    public void save(ScanResult result) {
        List<ScanResult> history = getAll();
        history.add(0, result);
        if (history.size() > 100) history = history.subList(0, 100);
        prefs.edit().putString("scans", gson.toJson(history)).apply();
    }

    public List<ScanResult> getAll() {
        String json = prefs.getString("scans", "[]");
        return gson.fromJson(json, new TypeToken<List<ScanResult>>(){}.getType());
    }

    public void clear() {
        prefs.edit().remove("scans").apply();
    }
}
