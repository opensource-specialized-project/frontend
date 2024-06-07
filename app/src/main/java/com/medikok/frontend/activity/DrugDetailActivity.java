package com.medikok.frontend.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.medikok.frontend.R;

public class DrugDetailActivity extends AppCompatActivity {

    private TextView countView;
    private boolean isExpanded = false;
    private static final int MAX_LINES_COLLAPSED = 2;

    private Button button;

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
        TextView effectView = findViewById(R.id.drug_effect);

        // Intent에서 데이터 가져오기
        String name = getIntent().getStringExtra("medicineName");
        String count = getIntent().getStringExtra("medicineCount");
        String effect = getIntent().getStringExtra("medicineEffect");
        String imgurl = getIntent().getStringExtra("medicineImage");

        countView = findViewById(R.id.drug_count);

        countView.setEllipsize(TextUtils.TruncateAt.END);

        // 데이터 설정
        nameView.setText(name);
        countView.setText(count);
        effectView.setText(effect);

        // url을 통해 이미지 불러오기
        if (imgurl != null && !imgurl.isEmpty()) {
            // 이미지가 있는 경우 Glide를 사용하여 이미지를 설정합니다.
            Glide.with(this)
                    .load(imgurl)
                    .error(R.drawable.no_image) // 이미지 로드 중 오류가 발생할 경우 no_image 리소스를 표시합니다.
                    .into(imageView);
        } else {
            // 이미지가 없는 경우 drawable 리소스에서 no_image를 사용합니다.
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.no_image));
        }

        // 뒤로가기 버튼 활성화
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // 텍스트가 두 줄을 넘는지 확인 후 버튼 생성
        countView.post(() -> {
            if (countView.getLineCount() > MAX_LINES_COLLAPSED) {
                createMoreButton();
                Log.d("aaa", "줄의 개수" + countView.getLineCount());
                Log.d("DrugDetailActivity", "버튼이 생성되었음");
                countView.setMaxLines(MAX_LINES_COLLAPSED);
            } else {
                Log.d("aaa", "줄의 개수" + countView.getLineCount());
                Log.d("DrugDetailActivity", "버튼이 생성되지 않음");
            }
        });
    }

    private void createMoreButton() {
        button = new Button(this);
        button.setBackgroundResource(R.drawable.arrow_down);
        button.setOnClickListener(v -> toggleExpansion());

        // 아이콘과 텍스트에 맞게 버튼의 너비를 설정합니다.
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 16, 0); // 마진을 조절할 수 있습니다. 필요에 따라 조절하세요.
        button.setLayoutParams(layoutParams);

        // 레이아웃에 버튼 추가
        LinearLayout buttonLayout = findViewById(R.id.drug_count_layout);
        if (buttonLayout != null) {
            buttonLayout.addView(button);
        } else {
            // Log an error or handle the case where the layout is not found
            Log.e("DrugDetailActivity", "Layout with ID drug_count_layout not found");
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

    private void toggleExpansion() {
        if (isExpanded) {
            button.setBackgroundResource(R.drawable.arrow_down);
            countView.setMaxLines(MAX_LINES_COLLAPSED);
            isExpanded = false;
        } else {
            button.setBackgroundResource(R.drawable.arrow_up);
            countView.setMaxLines(Integer.MAX_VALUE);
            isExpanded = true;
        }
    }
}
