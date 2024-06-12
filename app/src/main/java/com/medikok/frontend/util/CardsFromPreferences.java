package com.medikok.frontend.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medikok.frontend.R;
import com.medikok.frontend.activity.DrugDetailActivity;

import java.util.Map;

public class CardsFromPreferences {

    public static void saveCardToPreferences(LinearLayout pillCard, String alarmTime, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CardPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        TextView drugName = pillCard.findViewById(R.id.today_medicine_name);
        TextView drugEffect = pillCard.findViewById(R.id.today_medicine_ef);
        TextView drugCount = pillCard.findViewById(R.id.today_medicine_ct);
        String imageUrl = pillCard.findViewById(R.id.imageView).getTag().toString();


        String cardKey = alarmTime;
        editor.putString(cardKey + "_drugName", drugName.getText().toString());
        editor.putString(cardKey + "_drugEffect", drugEffect.getText().toString());
        editor.putString(cardKey + "_drugCount", drugCount.getText().toString());
        editor.putString(cardKey + "_imageUrl", imageUrl);

        editor.apply();
    }

    public static void removeCardFromPreferences(LinearLayout pillCard, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CardPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        TextView drugName = pillCard.findViewById(R.id.today_medicine_name);
        TextView drugEffect = pillCard.findViewById(R.id.today_medicine_ef);
        TextView drugCount = pillCard.findViewById(R.id.today_medicine_ct);
        String imageUrl = pillCard.findViewById(R.id.imageView).getTag().toString();

        // 알람 시간 키 가져오기
        String alarmTime = null;
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            if (entry.getKey().endsWith("_drugName")) {
                alarmTime = entry.getKey().substring(0, entry.getKey().lastIndexOf("_drugName"));
                break;
            }
        }

        if (alarmTime != null) {
            editor.remove(alarmTime + "_drugName");
            editor.remove(alarmTime + "_drugEffect");
            editor.remove(alarmTime + "_drugCount");
            editor.remove(alarmTime + "_imageUrl");
            editor.apply();
        }
    }
    public static void loadCardsFromPreferences(LinearLayout cardContainer, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CardPreferences", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        cardContainer.removeAllViews();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.endsWith("_drugName")) {
                String alarmTime = key.substring(0, key.lastIndexOf("_drugName"));
                String drugName = (String) entry.getValue();
                String drugEffect = sharedPreferences.getString(alarmTime + "_drugEffect", "");
                String drugCount = sharedPreferences.getString(alarmTime + "_drugCount", "");
                String imageUrl = sharedPreferences.getString(alarmTime + "_imageUrl", "");

                LinearLayout newPillCard = (LinearLayout) inflater.inflate(R.layout.today_pill_card, null);

                TextView Name = newPillCard.findViewById(R.id.today_medicine_name);
                Name.setText(drugName);
                Name.setMaxLines(1); // 한 줄만 표기되도록
                Name.setEllipsize(TextUtils.TruncateAt.END); // 한 줄 넘어가면 ...으로 생략

                TextView Effect = newPillCard.findViewById(R.id.today_medicine_ef);
                Effect.setText(drugEffect);
                Effect.setMaxLines(1);
                Effect.setEllipsize(TextUtils.TruncateAt.END);

                ImageView imageView = newPillCard.findViewById(R.id.imageView);
                imageView.setTag(imageUrl);

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    // 이미지가 있는 경우 Glide를 사용하여 이미지를 설정합니다.
                    Glide.with(context)
                            .load(imageUrl)
                            .error(R.drawable.no_image) // 이미지 로드 중 오류가 발생할 경우 no_image 리소스를 표시합니다.
                            .into(imageView);
                }
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                cardContainer.addView(newPillCard);

                newPillCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Intent를 통해 상세페이지에 약의 정보들을 전달
                        Intent intent = new Intent(context, DrugDetailActivity.class);
                        intent.putExtra("medicineName", drugName);
                        intent.putExtra("medicineCount", drugCount);
                        intent.putExtra("medicineEffect", drugEffect);
                        intent.putExtra("medicineImage", imageUrl); // Example drawable resource ID
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}
