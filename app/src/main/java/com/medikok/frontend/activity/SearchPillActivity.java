package com.medikok.frontend.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.medikok.frontend.R;
import com.medikok.frontend.model.DrugInfo;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;

public class SearchPillActivity extends AppCompatActivity {
    List<DrugInfo> drugInfoList;

    ListView pillList;
    TextView searchBar;
    Button searchButton;

    // 약품 이미지 예시
    Integer[] pillImages = {
            R.drawable.pill,
            R.drawable.pill1,
            R.drawable.pill2,
            R.drawable.pill3,
            R.drawable.pill4,
            R.drawable.pill5,
    };

    // 약품 이름 예시
    String[] pillNames = {
            "감기약",
            "소화제",
            "설사약",
            "해열제",
            "불면증 약",
            "불안 억제제"
    };

    // 약품 제조사 예시
    String[] pillManufacturers = {
            "삼성",
            "LG",
            "현대",
            "넥슨",
            "네이버",
            "카카오"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_pill);

        searchBar = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchButton);
        pillList = findViewById(R.id.pillList);

        // 약 검색 버튼 클릭시 실행(텍스트로 검색)
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchBar.getText().toString().trim();
                searchPills(searchString);
            }
        });

        // 데이터 받아오기
        new FetchDataAsyncTask().execute();
    }

    private void searchPills(String searchString) {
        if (drugInfoList != null) {
            // 검색 결과를 담을 리스트 생성
            List<DrugInfo> searchResults = new ArrayList<>();

            // 입력된 검색어와 일치하는 약품 찾기
            for (DrugInfo drugInfo : drugInfoList) {
                if (drugInfo.getItemName().contains(searchString)) {
                    searchResults.add(drugInfo);
                }
            }

            // 검색 결과를 이용하여 리스트뷰 업데이트
            CustomList newAdapter = new CustomList(SearchPillActivity.this, searchResults);
            pillList.setAdapter(newAdapter);
        }
    }

    // AsyncTask 정의
    private class FetchDataAsyncTask extends AsyncTask<Void, Void, List<DrugInfo>> {
        @Override
        protected List<DrugInfo> doInBackground(Void... voids) {
            // 데이터를 받아오는 작업을 수행
            // 여기서는 간단히 예시 데이터를 생성하여 반환
            return createSampleData();
        }

        @Override
        protected void onPostExecute(List<DrugInfo> result) {
            // 데이터를 받아오면 drugInfoList에 저장하고 UI 업데이트
            drugInfoList = result;
            if (drugInfoList != null) {
                Log.d("drugInfoList", drugInfoList.toString());
            }
        }
    }

    // 예시 데이터 생성 메서드
    private List<DrugInfo> createSampleData() {
        List<DrugInfo> sampleData = new ArrayList<>();
        for (int i = 0; i < pillNames.length; i++) {
            DrugInfo drugInfo = new DrugInfo();
            drugInfo.setItemName(pillNames[i]);
//            drugInfo.setManufacturer(pillManufacturers[i]);
            // 이미지 설정 등 필요한 데이터 설정
            sampleData.add(drugInfo);
        }
        return sampleData;
    }

    // 리스트뷰에 활용할 어댑터 정의
    private class CustomList extends ArrayAdapter<DrugInfo> {
        private final Activity context;
        private final List<DrugInfo> itemList;

        public CustomList(Activity context, List<DrugInfo> itemList) {
            super(context, R.layout.pill_list_item, itemList);
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.pill_list_item, null);

            ImageView pillImage = rowView.findViewById(R.id.pillImage);
            TextView pillName = rowView.findViewById(R.id.pillName);
            TextView pillManufacturer = rowView.findViewById(R.id.pillManufacturer);

            // 현재 position에 해당하는 DrugInfo 객체 가져오기
            DrugInfo currentItem = itemList.get(position);

            // DrugInfo 객체의 정보를 화면에 설정
            // 예시 데이터를 이용할 경우, 이미지 및 제조사 정보도 설정해야 합니다.
            pillName.setText(currentItem.getItemName());
//            pillManufacturer.setText(currentItem.getManufacturer());

            return rowView;
        }
    }
}


