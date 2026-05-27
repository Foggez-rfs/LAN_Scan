package com.lanscan.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.lanscan.R;
import com.lanscan.core.NetworkScanner;
import com.lanscan.database.CVEDatabase;
import com.lanscan.database.HistoryDatabase;
import com.lanscan.models.ScanResult;
import java.util.List;

public class ScanActivity extends AppCompatActivity {
    private TextView resultText, statusText;
    private ProgressBar progressBar;
    private NetworkScanner scanner;
    private CVEDatabase cveDB;
    private HistoryDatabase historyDB;
    private Handler handler;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_scan);

        resultText = findViewById(R.id.resultText);
        statusText = findViewById(R.id.statusText);
        progressBar = findViewById(R.id.progressBar);
        handler = new Handler(Looper.getMainLooper());

        scanner = new NetworkScanner(this);
        cveDB = new CVEDatabase(this);
        historyDB = new HistoryDatabase(this);

        String target = getIntent().getStringExtra("target");
        int mode = getIntent().getIntExtra("mode", 0);

        if (target != null) {
            runScan(target, mode);
        }
    }

    private void runScan(String target, int mode) {
        statusText.setText("Сканирование " + target + "...");
        progressBar.setIndeterminate(true);

        new Thread(() -> {
            ScanResult result = scanner.scan(target, mode);
            
            StringBuilder output = new StringBuilder(result.getRawOutput());
            output.append("\n\n=== CVE АНАЛИЗ ===\n");
            for (String line : result.getRawOutput().split("\n")) {
                if (line.contains("/tcp") && line.contains("open")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 3) {
                        List<CVEDatabase.CVEInfo> cves = cveDB.searchByService(parts[2]);
                        if (!cves.isEmpty()) {
                            output.append("\n").append(parts[2]).append(":\n");
                            for (CVEDatabase.CVEInfo cve : cves) {
                                output.append(String.format("  %s [%.1f %s]\n  %s\n\n",
                                    cve.id, cve.cvss, cve.severity, cve.description));
                            }
                        }
                    }
                }
            }

            String finalOutput = output.toString();
            historyDB.save(result);

            handler.post(() -> {
                resultText.setText(finalOutput);
                statusText.setText("Готово");
                progressBar.setIndeterminate(false);
                progressBar.setProgress(100);
            });
        }).start();
    }
}
