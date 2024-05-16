package com.medikok.frontend.activity;

import android.content.Intent;
import android.graphics.Bitmap; // 비트맵 데이터(이미지 전송 목적) 추가
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView; // 텍스트뷰 추가
import android.widget.ImageView; // 이미지뷰 추가
import android.widget.LinearLayout; // 리이너레이아웃 추가
import androidx.cardview.widget.CardView; // 카드뷰 추가

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.drawable.Drawable; // Drawable 추가
import android.content.Context; // Context 추가

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.medikok.frontend.R;
import com.medikok.frontend.model.DrugInfo;
import com.medikok.frontend.util.ServerConnector;

import java.util.List;
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    // spring boot 서버와 연결하기 위함
    private ServerConnector serverConnector;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinearLayout dynamicLayout;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        /// 서버와 연결
        ServerConnector.connectToServer(new ServerConnector.ServerResponseListener() {
            @Override
            public void onSuccess(List<DrugInfo> responseData) {
                // 서버 응답 데이터 처리
                LinearLayout dynamicLayout = findViewById(R.id.layout1);
                for (DrugInfo drugInfo : responseData) {
                    Log.d("MainActivity", "Item Name: " + drugInfo.getItemName());
                    Log.d("MainActivity", "Effects: " + drugInfo.getEfcyQesitm());
                    Log.d("MainActivity", "Use Method: " + drugInfo.getUseMethodQesitm());

                    String name = drugInfo.getItemName();
                    String effect = drugInfo.getEfcyQesitm();
                    String method = drugInfo.getUseMethodQesitm();

                    Drawable image = ContextCompat.getDrawable(MainActivity.this, R.drawable.none);

                    CardView pillCard = makePillCard(MainActivity.this, image, name, method, effect);
                    dynamicLayout.addView(pillCard);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // 오류 처리
                Log.d("MainActivity", errorMessage);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 알람 표시 동적 구현(하드코딩) - 사재헌
        // 일단은, 플로팅 버튼을 누르면 알람 카드가 늘어나도록 만듦
        // 준혁이 형이 만들고 있는 검색창 완성되면, 그걸로 약 이름을 선택하고
        // 시간, 요일 선택하는 XML 더 만들어서 구현할 예정
        LinearLayout alarmContainer = (LinearLayout)findViewById(R.id.alarmContainer);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater.inflate(R.layout.alarm, alarmContainer, true);
            }
        });
    }


    private CardView makePillCard(Context context, Drawable imageDrawable, String medicineName, String medicineCount, String medicineEffect)
    {
        // 카드 뷰 생성
        CardView pillCard = new CardView(this);

        CardView.LayoutParams pillCardParams = new CardView.LayoutParams(
                CardView.LayoutParams.WRAP_CONTENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        // 좌우 마진 설정
        pillCardParams.setMargins(1, 50, 100, 50); // 여기서 16은 원하는 간격 값입니다.
        pillCard.setLayoutParams(pillCardParams);

        // 카드 모서리 변경 (관련 메소드가 존재하지 않아 수정 필요)
        // pillCard.setCardCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.card_corner_radius));

        pillCard.setCardElevation(context.getResources().getDimensionPixelSize(R.dimen.card_elevation));

        // 카드 안의 content를 위한 리니어레이아웃 생성
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        linearLayout.setPadding(15,15,15,15); // 내부 패딩 설정

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // 카드 썸네일을 위한 이미지뷰 생성
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);

        imageView.setLayoutParams(layoutParams);

        // 이미지 경로 설정
        imageView.setImageDrawable(imageDrawable);
        // 이미지 alt 설정
        imageView.setContentDescription(context.getString(R.string.card_content_description));

        // 약품명 TextView 생성
        TextView nameView = new TextView(context);
        nameView.setId(View.generateViewId());
        // ConstraintLayout.LayoutParams로 변경
        ConstraintLayout.LayoutParams nameViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        nameViewParams.width = 300; // 폭을 400픽셀로 설정
        nameView.setLayoutParams(nameViewParams);
        nameView.setText(medicineName);
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        nameView.setMaxLines(1); // 한 줄만 표기되도록
        nameView.setEllipsize(TextUtils.TruncateAt.END); // 한 줄 넘어가면 ...으로 생략
        nameView.setTypeface(null, Typeface.BOLD);

        // 약품 복용 횟수 TextView 생성
        TextView countView = new TextView(context);
        countView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams countViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        countViewParams.width = 300; // 폭을 300픽셀로 설정
        countView.setLayoutParams(countViewParams);
        countView.setText(medicineCount);
        countView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        countView.setMaxLines(1);
        countView.setEllipsize(TextUtils.TruncateAt.END);
        countView.setTypeface(null, Typeface.BOLD);

        // 약품 효능 TextView 생성
        TextView effectView = new TextView(context);
        effectView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams effectViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        effectViewParams.width = 300; // 폭을 200픽셀로 설정
        effectView.setLayoutParams(effectViewParams);
        effectView.setText(medicineEffect);
        effectView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        effectView.setMaxLines(1);
        effectView.setEllipsize(TextUtils.TruncateAt.END);
        effectView.setTypeface(null, Typeface.BOLD);

        // 카드 뷰에 내용 추가
        linearLayout.addView(imageView);
        linearLayout.addView(nameView);
        linearLayout.addView(countView);
        linearLayout.addView(effectView); // 이 부분을 medicineCount 밑으로 이동하여 수정하였습니다.
        pillCard.addView(linearLayout); // linearLayout을 카드에 추가

        // 클릭 리스너 추가
        pillCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, pillDetailActivity.class);
                intent.putExtra("medicineName", medicineName);
                intent.putExtra("medicineCount", medicineCount);
                intent.putExtra("medicineEffect", medicineEffect);
                intent.putExtra("medicineImage", R.drawable.none); // Example drawable resource ID
                context.startActivity(intent);
            }
        });

        return pillCard;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }
}