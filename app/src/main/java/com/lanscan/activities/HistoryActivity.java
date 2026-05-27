package com.lanscan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lanscan.R;
import com.lanscan.adapters.HistoryAdapter;
import com.lanscan.database.HistoryDatabase;
import com.lanscan.models.ScanResult;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button clearBtn;
    private HistoryDatabase historyDB;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        clearBtn = findViewById(R.id.clearBtn);
        historyDB = new HistoryDatabase(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ScanResult> history = historyDB.getAll();
        adapter = new HistoryAdapter(history, result -> {
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("target", result.getTarget());
            intent.putExtra("output", result.getRawOutput());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        clearBtn.setOnClickListener(v -> {
            historyDB.clear();
            adapter.updateList(historyDB.getAll());
            Toast.makeText(this, "История очищена", Toast.LENGTH_SHORT).show();
        });
    }
}
