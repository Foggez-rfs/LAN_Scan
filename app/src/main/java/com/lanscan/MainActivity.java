package com.lanscan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class MainActivity extends AppCompatActivity {
    private EditText targetInput;
    private Spinner modeSpinner;
    private Button scanBtn, netScanBtn;
    private TextView resultText;
    private Handler handler;
    private static String nmapPath;

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
        resultText = findViewById(R.id.resultText);

        modeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MODES));
        
        setupNmap();

        scanBtn.setOnClickListener(v -> {
            String ip = targetInput.getText().toString().trim();
            if (ip.isEmpty()) { toast("Введите IP"); return; }
            runCmd(nmapPath + " " + ARGS[modeSpinner.getSelectedItemPosition()] + " " + ip);
        });

        netScanBtn.setOnClickListener(v -> runCmd(nmapPath + " -sn 192.168.1.0/24"));
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
            toast("Nmap готов");
        } catch (Exception e) {
            toast("Ошибка nmap: " + e.getMessage());
        }
    }

    private void runCmd(String cmd) {
        resultText.setText("Сканирование...\n");
        new Thread(() -> {
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String l;
                while ((l = r.readLine()) != null) sb.append(l).append("\n");
                p.waitFor();
                handler.post(() -> resultText.setText(sb.toString()));
            } catch (Exception e) {
                handler.post(() -> resultText.setText("Ошибка: " + e.getMessage()));
            }
        }).start();
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
