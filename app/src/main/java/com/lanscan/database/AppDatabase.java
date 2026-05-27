package com.lanscan.database;

import android.content.Context;

public class AppDatabase {
    private Context context;
    private CVEDatabase cveDB;
    private HistoryDatabase historyDB;

    public AppDatabase(Context context) {
        this.context = context;
        this.cveDB = new CVEDatabase(context);
        this.historyDB = new HistoryDatabase(context);
    }

    public CVEDatabase cve() { return cveDB; }
    public HistoryDatabase history() { return historyDB; }
}
