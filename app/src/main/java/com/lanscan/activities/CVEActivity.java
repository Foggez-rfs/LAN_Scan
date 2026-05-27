package com.lanscan.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.lanscan.R;
import com.lanscan.database.CVEDatabase;
import java.util.List;

public class CVEActivity extends AppCompatActivity {
    private EditText searchInput;
    private Button searchBtn;
    private TextView resultText;
    private CVEDatabase cveDB;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_cve);

        searchInput = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchBtn);
        resultText = findViewById(R.id.resultText);
        cveDB = new CVEDatabase(this);

        searchBtn.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (query.isEmpty()) return;

            if (query.startsWith("CVE-")) {
                CVEDatabase.CVEInfo info = cveDB.getCVE(query);
                if (info != null) {
                    resultText.setText(String.format("%s\nCVSS: %.1f %s\nVector: %s\n\n%s",
                        info.id, info.cvss, info.severity, info.vector, info.description));
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
        });
    }
}
