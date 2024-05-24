package com.medikok.frontend;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView; // 카드뷰 추가

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Context; // Context 추가
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.medikok.frontend.R;
import com.medikok.frontend.activity.DrugDetailActivity;
import com.medikok.frontend.activity.MainActivity;
import com.medikok.frontend.model.DrugInfo;
import com.medikok.frontend.util.ServerConnector;

import java.util.List;

public class AlarmFragment extends Fragment {

    private LinearLayout alarmContainer;

    public AlarmFragment() {
        // Required empty public constructor
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ServerConnector serverConnector;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 알람 표시 동적 구현(하드코딩) - 사재헌
        // 일단은, 플로팅 버튼을 누르면 알람 카드가 늘어나도록 만듦
        // 준혁이 형이 만들고 있는 검색창 완성되면, 그걸로 약 이름을 선택하고
        // 시간, 요일 선택하는 XML 더 만들어서 구현할 예정
        // 문제: 인플레이팅이 이상하게 됨 -> inflater변수를 배열로 만들어 여러개 동작하도록 반복문으로 구현해야 하나?
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        alarmContainer = view.findViewById(R.id.alarmContainer);
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);

        // 플로팅 버튼 클릭 이벤트 처리
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 LayoutInflater 인스턴스 생성
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // 알람 카드를 담을 새로운 LinearLayout 생성
                LinearLayout newAlarmCard = (LinearLayout) inflater.inflate(R.layout.alarm, null);

                // 알람 시간 설정 다이얼로그 표시
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        TextView alarmTime = newAlarmCard.findViewById(R.id.alarmPillDateTime);
                        alarmTime.setText(hourOfDay + ":" + minute);
                        alarmTime.setTextSize(2, 35);
                    }
                }, 0, 0, true);
                timePickerDialog.show();

                // 새로운 알람 카드를 알람 컨테이너에 추가
                alarmContainer.addView(newAlarmCard);
            }
        });

//        fab.setOnClickListener(v -> {
//            // 새로운 LayoutInflater 인스턴스 생성
//            LayoutInflater newInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            // 알람 카드를 담을 새로운 LinearLayout 생성
//            LinearLayout newAlarmCard = (LinearLayout) newInflater.inflate(R.layout.alarm, null);
//
//            // 알람 시간 설정 다이얼로그 표시
//            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, (view1, hourOfDay, minute) -> {
//                TextView alarmTime = newAlarmCard.findViewById(R.id.alarmPillDateTime);
//                alarmTime.setText(hourOfDay + ":" + minute);
//                alarmTime.setTextSize(2, 35);
//            }, 0, 0, true);
//            timePickerDialog.show();
//
//            // 새로운 알람 카드를 알람 컨테이너에 추가
//            alarmContainer.addView(newAlarmCard);
//        });

        ServerConnector.connectToServer(new ServerConnector.ServerResponseListener() {
            @Override
            public void onSuccess(List<DrugInfo> responseData) {
                LinearLayout dynamicLayout = view.findViewById(R.id.layout1);
                for (DrugInfo drugInfo : responseData) {
                    Log.d("AlarmFragment", "Item Name: " + drugInfo.getItemName());
                    Log.d("AlarmFragment", "Effects: " + drugInfo.getEfcyQesitm());
                    Log.d("AlarmFragment", "Use Method: " + drugInfo.getUseMethodQesitm());
                    Log.d("AlarmFragment", "Item Image: " + drugInfo.getItemImage());

                    String name = drugInfo.getItemName();
                    String effect = drugInfo.getEfcyQesitm();
                    String method = drugInfo.getUseMethodQesitm();
                    String imageUrl = drugInfo.getItemImage();

                    CardView pillCard = makePillCard(getContext(), imageUrl, name, method, effect);
                    dynamicLayout.addView(pillCard);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the error
            }
        });
        return view;
    }




    private CardView makePillCard(Context context, String imageUrl, String medicineName, String medicineCount, String medicineEffect) {
        // 카드 뷰 생성
        CardView pillCard = new CardView(getContext());

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
        linearLayout.setPadding(20,20,20,20); // 내부 패딩 설정

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // 카드 썸네일을 위한 이미지뷰 생성
        ImageView imageView = new ImageView(context);
        Log.d("MainActivity", imageUrl);
        // url 통해서 이미지 가져오기
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);

        imageView.setLayoutParams(layoutParams);

        // 이미지 경로 설정
        //imageView.setImageDrawable(imageDrawable);
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
                Intent intent = new Intent(context, DrugDetailActivity.class);
                intent.putExtra("medicineName", medicineName);
                intent.putExtra("medicineCount", medicineCount);
                intent.putExtra("medicineEffect", medicineEffect);
                intent.putExtra("medicineImage", imageUrl); // Example drawable resource ID
                context.startActivity(intent);
            }
        });

        return pillCard;
    }
}
