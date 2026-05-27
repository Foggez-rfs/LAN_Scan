package com.lanscan.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.lanscan.R;
import com.lanscan.utils.Utils;

public class ResultsActivity extends AppCompatActivity {
    private TextView titleText, contentText;
    private Button exportBtn, shareBtn;
    private String target, output;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_results);

        titleText = findViewById(R.id.titleText);
        contentText = findViewById(R.id.contentText);
        exportBtn = findViewById(R.id.exportBtn);
        shareBtn = findViewById(R.id.shareBtn);

        target = getIntent().getStringExtra("target");
        output = getIntent().getStringExtra("output");

        titleText.setText("Результаты: " + target);
        contentText.setText(output);

        exportBtn.setOnClickListener(v -> {
            String filename = "scan_" + target.replace(".", "_") + ".txt";
            Utils.saveFile(this, filename, output);
            Toast.makeText(this, "Сохранено: " + filename, Toast.LENGTH_SHORT).show();
        });

        shareBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, output);
            startActivity(Intent.createChooser(intent, "Поделиться"));
        });

        contentText.setOnLongClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("scan", output));
            Toast.makeText(this, "Скопировано", Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}
