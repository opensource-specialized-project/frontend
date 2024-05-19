package com.medikok.frontend.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.medikok.frontend.R;

public class SearchPillActivity extends AppCompatActivity {
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_pill);


        // 약 검색 리스트뷰의 어댑터 설정
        SearchPillActivity.CustomList adapter = new SearchPillActivity.CustomList(SearchPillActivity.this);
        pillList = (ListView)findViewById(R.id.pillList);
        pillList.setAdapter(adapter);

        searchBar = (TextView) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);

        // 약 검색 버튼 클릭시 실행(텍스트로 검색)
        searchButton.setOnClickListener(new View.OnClickListener() {
            // 변수 선언
            boolean searchSuccess = false;
            int searchItemCount = 30; // 검색되는 약의 최대 개수를 임시로 30으로 설정
            int[] indexOfSearch = new int[searchItemCount];
            @Override
            public void onClick(View v) {
                // 버튼 클릭할 때마다 일부 변수값 초기화
                searchSuccess = false;
                indexOfSearch = new int[searchItemCount];
                indexOfSearch[0] = -1;

                String searchString;

                pillList.setVisibility(View.VISIBLE);
                searchString = searchBar.getText().toString();
                Log.d("onClick", "searchString: " + searchString);

                for (int i = 0, lastIndex = -1; i < pillNames.length; i++) {
                    if(pillNames[i].contains(searchString)) {
                        Log.d("onClick", "pillNames[" + i + "]: " + pillNames[i].toString());
                        lastIndex++;
                        indexOfSearch[lastIndex] = i;
                        searchSuccess = true;
                        Log.d("onClick", "lastIndex: " + lastIndex);
                    }
                    Log.d("onClick", "searchSuccess: " + searchSuccess);
                }

                SearchPillActivity.CustomList newAdapter = new SearchPillActivity.CustomList(SearchPillActivity.this) {
                    @Override
                    public View getView(int position, View view, ViewGroup parent) {
                        LayoutInflater inflater = context.getLayoutInflater();
                        View rowView = inflater.inflate(R.layout.pill_list_item, null);

                        ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
                        TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
                        TextView pillManufacturer = (TextView) rowView.findViewById(R.id.pillManufacturer);

                        Log.d("onClick", "searchSuccess: " + searchSuccess);
                        Log.d("onClick", "position: " + position);

                        if(searchSuccess) {
                            pillList.setVisibility(View.VISIBLE);

                            Log.d("onClick", "position: " + position);
                            Log.d("onClick", "indexOfSearch[0]: " + indexOfSearch[0]);
                            Log.d("onClick", "indexOfSearch[1]: " + indexOfSearch[1]);
                            Log.d("onClick", "indexOfSearch[2]: " + indexOfSearch[2]);
                            if(position <= indexOfSearch[position]) {
                                rowView.setVisibility(View.VISIBLE);

                                pillImage.setImageResource(pillImages[indexOfSearch[position]]);
                                pillName.setText(pillNames[indexOfSearch[position]]);
                                pillManufacturer.setText(pillManufacturers[indexOfSearch[position]]);

                                return rowView;
                            }
                            else {
                                Log.d("onClick", "position: " + position);
                                rowView.setVisibility(View.GONE);

                                return rowView;
                            }
                        }
                        else {
                            pillList.setVisibility(View.GONE);

                            return rowView;
                        }
                    }
                };
                pillList.setAdapter(newAdapter);
            }
        });
    }

    // 리스트뷰에 활용할 어댑터 정의
    public class CustomList extends ArrayAdapter<String> {
        public final Activity context;
        public CustomList(Activity context) {
            super(context, R.layout.pill_list_item, pillNames);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.pill_list_item, null);

            ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
            TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
            TextView pillManufacturer = (TextView) rowView.findViewById(R.id.pillManufacturer);

            pillImage.setImageResource(pillImages[position]);
            pillName.setText(pillNames[position]);
            pillManufacturer.setText(pillManufacturers[position]);

            return rowView;
        }
    }
}
