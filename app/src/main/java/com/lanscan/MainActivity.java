package com.lanscan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private EditText targetInput;
    private Spinner modeSpinner;
    private Button scanBtn, netScanBtn, cveBtn;
    private TextView resultText;
    private Handler handler;
    private static String nmapPath;
    private CVEDatabase cveDB;

    private static final String[] MODES = {
        "1. Быстрое", "2. Версии", "3. ОС", "4. NSE", "5. Агрессивное",
        "6. Все порты", "7. UDP", "8. CVE", "9. МОЩНЫЙ", "10. УЛЬТРА"
    };

    private static final String[] ARGS = {
        "-Pn --top-ports 100 --min-rate 1000",
        "-sV -Pn --version-intensity 5",
        "-O -Pn --osscan-guess",
        "-sC -Pn --script=default",
        "-A -Pn -T4",
        "-p- -Pn --min-rate 5000",
        "-sU -Pn --top-ports 100",
        "-Pn --script vuln",
        "-sC -sV -A -v -Pn -T4",
        "-p- -sC -sV -A -Pn -T5"
    };

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        handler = new Handler(Looper.getMainLooper());

        targetInput = findViewById(R.id.targetInput);
        modeSpinner = findViewById(R.id.modeSpinner);
        scanBtn = findViewById(R.id.scanBtn);
        netScanBtn = findViewById(R.id.netScanBtn);
        cveBtn = findViewById(R.id.cveBtn);
        resultText = findViewById(R.id.resultText);

        modeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MODES));
        
        setupNmap();
        cveDB = new CVEDatabase(this);

        scanBtn.setOnClickListener(v -> {
            String ip = targetInput.getText().toString().trim();
            if (ip.isEmpty()) { toast("Введите IP"); return; }
            runScan(nmapPath + " " + ARGS[modeSpinner.getSelectedItemPosition()] + " " + ip);
        });

        netScanBtn.setOnClickListener(v -> runScan(nmapPath + " -sn 192.168.1.0/24"));
        
        cveBtn.setOnClickListener(v -> {
            String query = targetInput.getText().toString().trim();
            if (query.isEmpty()) { toast("Введите сервис или CVE ID"); return; }
            searchCVE(query);
        });
    }

    private void setupNmap() {
        try {
            InputStream in = getAssets().open("nmap");
            File outFile = new File(getFilesDir(), "nmap");
            FileOutputStream out = new FileOutputStream(outFile);
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            in.close();
            out.close();
            outFile.setExecutable(true);
            nmapPath = outFile.getAbsolutePath();
        } catch (Exception e) {
            toast("Nmap: " + e.getMessage());
        }
    }

    private void runScan(String cmd) {
        resultText.setText("Сканирование...\n");
        new Thread(() -> {
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String l;
                while ((l = r.readLine()) != null) sb.append(l).append("\n");
                p.waitFor();
                
                // Добавляем CVE анализ
                String output = sb.toString();
                StringBuilder result = new StringBuilder(output);
                result.append("\n=== CVE АНАЛИЗ ===\n");
                
                for (String line : output.split("\n")) {
                    if (line.contains("/tcp") && line.contains("open")) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 3) {
                            String service = parts[2];
                            List<CVEDatabase.CVEInfo> cves = cveDB.searchByService(service);
                            if (!cves.isEmpty()) {
                                result.append(String.format("\n%s:\n", service));
                                for (CVEDatabase.CVEInfo cve : cves) {
                                    result.append(String.format("  %s CVSS:%.1f %s\n  %s\n", 
                                        cve.id, cve.cvss, cve.severity, cve.description));
                                }
                            }
                        }
                    }
                }
                
                final String finalResult = result.toString();
                handler.post(() -> resultText.setText(finalResult));
            } catch (Exception e) {
                handler.post(() -> resultText.setText("Ошибка: " + e.getMessage()));
            }
        }).start();
    }
    
    private void searchCVE(String query) {
        if (query.startsWith("CVE-")) {
            CVEDatabase.CVEInfo info = cveDB.getCVE(query);
            if (info != null) {
                resultText.setText(String.format("%s\nCVSS: %.1f %s\n%s\nVector: %s", 
                    info.id, info.cvss, info.severity, info.description, info.vector));
            } else {
                resultText.setText("CVE не найдена в базе");
            }
        } else {
            List<CVEDatabase.CVEInfo> results = cveDB.searchByService(query);
            StringBuilder sb = new StringBuilder("Найдено: " + results.size() + "\n\n");
            for (CVEDatabase.CVEInfo info : results) {
                sb.append(String.format("%s [%.1f %s]\n%s\n\n", 
                    info.id, info.cvss, info.severity, info.description));
            }
            resultText.setText(sb.toString());
        }
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
