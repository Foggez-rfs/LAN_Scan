package com.lanscan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.lanscan.R;

public class MainActivity extends AppCompatActivity {
    private EditText targetInput;
    private Spinner modeSpinner;
    private Button scanBtn, netScanBtn, cveBtn, historyBtn, settingsBtn;

    private static final String[] MODES = {
        "1. Быстрое", "2. Версии", "3. ОС", "4. NSE", "5. Агрессивное",
        "6. Все порты", "7. UDP", "8. CVE", "9. МОЩНЫЙ", "10. УЛЬТРА"
    };

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        targetInput = findViewById(R.id.targetInput);
        modeSpinner = findViewById(R.id.modeSpinner);
        scanBtn = findViewById(R.id.scanBtn);
        netScanBtn = findViewById(R.id.netScanBtn);
        cveBtn = findViewById(R.id.cveBtn);
        historyBtn = findViewById(R.id.historyBtn);
        settingsBtn = findViewById(R.id.settingsBtn);

        modeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MODES));

        scanBtn.setOnClickListener(v -> {
            String ip = targetInput.getText().toString().trim();
            if (ip.isEmpty()) {
                Toast.makeText(this, "Введите IP", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra("target", ip);
            intent.putExtra("mode", modeSpinner.getSelectedItemPosition() + 1);
            startActivity(intent);
        });

        netScanBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra("target", "192.168.1.0/24");
            intent.putExtra("mode", 0);
            startActivity(intent);
        });

        cveBtn.setOnClickListener(v -> startActivity(new Intent(this, CVEActivity.class)));
        historyBtn.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        settingsBtn.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}
