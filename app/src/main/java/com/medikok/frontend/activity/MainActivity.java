package com.medikok.frontend.activity;

import android.content.Intent;
import android.graphics.Bitmap; // 비트맵 데이터(이미지 전송 목적) 추가
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView; // 텍스트뷰 추가
import android.widget.ImageView; // 이미지뷰 추가
import android.widget.LinearLayout; // 리이너레이아웃 추가
import androidx.cardview.widget.CardView; // 카드뷰 추가

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.drawable.Drawable; // Drawable 추가
import android.content.Context; // Context 추가

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
                for (DrugInfo drugInfo : responseData) {
                    Log.d("MainActivity", "Item Name: " + drugInfo.getItemName());
                    Log.d("MainActivity", "Effects: " + drugInfo.getEfcyQesitm());
                    Log.d("MainActivity", "Usage Method: " + drugInfo.getUseMethodQesitm());
                    Log.d("MainActivity", "Precautions Before Use: " + drugInfo.getAtpnWarnQesitm());
                    Log.d("MainActivity", "Precautions: " + drugInfo.getAtpnQesitm());
                    Log.d("MainActivity", "Interactions: " + drugInfo.getIntrcQesitm());
                    Log.d("MainActivity", "Storage Precautions: " + drugInfo.getDepositMethodQesitm());
                    Log.d("MainActivity", "Image URL: " + drugInfo.getItemImage());
                    Log.d("MainActivity", "\n");
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

        dynamicLayout = findViewById(R.id.layout1);
        Drawable imageDrawble = getResources().getDrawable(R.drawable.pill);
        Drawable imageDrawble1 = getResources().getDrawable(R.drawable.pill1);

        CardView pillCard = makePillCard(this, imageDrawble, "항암제", "1일 100회", "맛있음");
        CardView pillCard1 = makePillCard(this, imageDrawble1, "영양제", "1일 100회", "맛있음");
        CardView pillCard2 = makePillCard(this, imageDrawble, "발모제", "1일 100회", "맛있음");
        CardView pillCard3 = makePillCard(this, imageDrawble, "비타민", "1일 100회", "맛있음");
        CardView pillCard4 = makePillCard(this, imageDrawble1, "독극물", "1일 100회", "맛있음");
        CardView pillCard5 = makePillCard(this, imageDrawble1, "마약", "1일 100회", "맛있음");

        dynamicLayout.addView(pillCard);
        dynamicLayout.addView(pillCard1);
        dynamicLayout.addView(pillCard2);
        dynamicLayout.addView(pillCard3);
        dynamicLayout.addView(pillCard4);
        dynamicLayout.addView(pillCard5);
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
        nameView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        nameView.setText(medicineName);
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        nameView.setTypeface(null, Typeface.BOLD);

        // 약품 복용 횟수 TextView 생성
        TextView countView = new TextView(context);
        countView.setId(View.generateViewId());
        countView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        countView.setText(medicineCount);
        countView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        countView.setTypeface(null, Typeface.BOLD);

        // 약품 효능 TextView 생성
        TextView effectView = new TextView(context);
        effectView.setId(View.generateViewId());
        effectView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        effectView.setText(medicineEffect);
        effectView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        effectView.setTypeface(null, Typeface.BOLD);

        // 카드 뷰에 내용 추가
        linearLayout.addView(imageView);
        linearLayout.addView(nameView);
        linearLayout.addView(countView);
        linearLayout.addView(effectView); // 이 부분을 medicineCount 밑으로 이동하여 수정하였습니다.
        pillCard.addView(linearLayout); // linearLayout을 카드에 추가

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