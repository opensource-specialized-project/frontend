package com.medikok.frontend.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.medikok.frontend.R;

public class DrugDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drug_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView = findViewById(R.id.drug_image);
        TextView nameView = findViewById(R.id.drug_name);
        TextView countView = findViewById(R.id.drug_count);
        TextView effectView = findViewById(R.id.drug_effect);

        // Intent에서 데이터 가져오기
        String name = getIntent().getStringExtra("medicineName");
        String count = getIntent().getStringExtra("medicineCount");
        String effect = getIntent().getStringExtra("medicineEffect");
        int imageResId = getIntent().getIntExtra("medicineImage", R.drawable.pill);

        // 데이터 설정
        imageView.setImageResource(imageResId);
        nameView.setText(name);
        countView.setText(count);
        effectView.setText(effect);

        // 뒤로가기 버튼 활성화
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}