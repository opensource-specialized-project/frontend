package com.medikok.frontend;

import android.content.Intent;
import android.graphics.Bitmap; // 비트맵 데이터(이미지 전송 목적) 추가
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.TextView; // 텍스트뷰 추가
import android.widget.ImageView; // 이미지뷰 추가
import android.widget.LinearLayout; // 리이너레이아웃 추가
import androidx.cardview.widget.CardView; // 카드뷰 추가

import android.content.res.Resources; // dimens.xml의 value들을 참고하기위함

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.graphics.drawable.Drawable; // Drawable 추가
import android.content.Context; // Context 추가


import android.view.View;
import android.widget.ImageView; // 이미지뷰 추가

import android.content.Intent; // 인텐트 추가
import android.provider.MediaStore; // 미디어 스토어 추가

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinearLayout dynamicLayout;


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSchedule.class);
                startActivity(intent);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void makePillCard(Context context, Drawable imageDrawable, String medicineName, String medicineCount, String medicineEffect)
    {
        // 카드 뷰 생성
        CardView pillCard = new CardView(this);
        CardView.LayoutParams pillCardParams = new CardView.LayoutParams(
                CardView.LayoutParams.WRAP_CONTENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
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
        // 이미지 경로 설정
        imageView.setImageDrawable(imageDrawable);
        // 이미지 alt 설정
        imageView.setContentDescription(context.getString(R.string.card_content_description));

        // 카드 푸터를 위한 컨스트레인트 레이아웃 생성
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        constraintLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // 약품명 TextView 생성
        TextView nameView = new TextView(context);
        nameView.setId(View.generateViewId());
        nameView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        nameView.setText(medicineName);
        nameView.setTextSize(TypeValue.COMPLEX_UNIT_SP, 14);
        nameView.setTypeface(null, Typeface.BOLD);

        // 약품 복용 횟수 TextView 생성
        TextView countView = new TextView(context);
        countView.setId(View.generateViewId());
        countView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
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

    /* 추가 버튼을 누르면 아래 1, 2를 담은 알람 카드를 만들어 3으로 만든 알람 카드를 띄움 */

    // 1. 약을 먹어야 하는 시간(오전/오후, 시간, 요일) 등록
    //   -> alarm.xml ID
    //   -> 오전/오후 텍스트뷰: alarmPillDateAMPM
    //   -> 시간 텍스트뷰: alarmPillDateTime
    //   -> 요일 텍스트뷰: alarmPillDateMon ~ Sun

    // 2. 먹어야 하는 약 목록
    //   -> alarm.xml ID
    //   -> 약 이름 텍스트뷰: alarmPillName

    // 3. 위의 정보를 담아 알람 카드 생성
    //   -> activity_main.xml ID
    //   -> 알람 카드 include: alarmPill

    // private void makeAlarmPillDate(){
    //
    // }

    // private void makeAlarmPillName(){
    //
    // }

    // private void makeAlarmPillSwitch(){
    //
    // }
}