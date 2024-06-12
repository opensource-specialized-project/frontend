package com.medikok.frontend.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medikok.frontend.activity.DrugDetailActivity;
import com.medikok.frontend.R;

public class MakePillCard {
    public static LinearLayout makePillCard(Context context, String imageUrl, String medicineName, String medicineCount, String medicineEffect) {
        // 카드 뷰 생성
        LinearLayout pillCard = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.today_pill_card, null);

        // 약의 이름 지정
        TextView nameView = pillCard.findViewById(R.id.today_medicine_name);
        nameView.setText(medicineName);
        nameView.setMaxLines(1); // 한 줄만 표기되도록
        nameView.setEllipsize(TextUtils.TruncateAt.END); // 한 줄 넘어가면 ...으로 생략

        // 약의 효과 지정
        TextView effectView = pillCard.findViewById(R.id.today_medicine_ef);
        effectView.setText(medicineEffect);
        effectView.setMaxLines(1);
        effectView.setEllipsize(TextUtils.TruncateAt.END);

        // 약의 복용법 지정
        TextView countView = pillCard.findViewById(R.id.today_medicine_ct);
        countView.setText(medicineCount);

        // 카드 썸네일을 위한 이미지뷰 생성
        ImageView imageView = pillCard.findViewById(R.id.imageView);
        imageView.setTag(imageUrl);
        Log.d("PillCardUtil", imageUrl);
        // url 통해서 이미지 가져오기
        // 이미지뷰에 이미지 설정
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // 이미지가 있는 경우 Glide를 사용하여 이미지를 설정합니다.
            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.no_image) // 이미지 로드 중 오류가 발생할 경우 no_image 리소스를 표시합니다.
                    .into(imageView);
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 상세 페이지로 넘어가기 위한 클릭 리스너 구현
        pillCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent를 통해 상세페이지에 약의 정보들을 전달
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
