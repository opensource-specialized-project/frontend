//package com.medikok.frontend.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.chip.Chip;
//import com.google.android.material.chip.ChipGroup;
//import com.medikok.frontend.R;
//import com.medikok.frontend.model.DrugInfo;
//
//import android.os.AsyncTask;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SearchPillActivity extends AppCompatActivity {
//    // DB로부터 받아온 전체 약 리스트 항목 100개를 저장하는 List(고정)
//    List<DrugInfo> drugInfoList;
//
//    ListView pillList;
//    TextView searchBar;
//    Button searchButton;
//
//    ChipGroup symptomGroup;
//    Chip coldChip, digestionChip, diarrheaChip,
//            feverChip, acheChip, inflammationChip,
//            adhdChip, othersChip;
//
//    ArrayList<String> itemRooms = new ArrayList<>();
//
//    // 리스트에 있는 전체 약의 개수
//    static final int PILL_COUNT = 100;
//    // 검색된 리스트 항목 개수
//    int itemCount = PILL_COUNT;
//
//
//
//    static final int TEST_COUNT = 6;
//
//    String[] testNames = new String[] {
//            "타이레놀",
//            "타미플루",
//            "게보린",
//            "활명수",
//            "자양강장",
//            "레모나"
//    };
//
////    public class PillItem {
////        Integer pillImageResource;
////        String pillNameResource;
////        String pillSymptomResource;
////
////        PillItem(Integer pillImageResource, String pillNameResource, String pillSymptomResource) {
////            this.pillImageResource = pillImageResource;
////            this.pillNameResource = pillNameResource;
////            this.pillSymptomResource = pillSymptomResource;
////        }
////
////        public Integer getPillImageResource() {
////            return pillImageResource;
////        }
////
////        public String getPillNameResource() {
////            return pillNameResource;
////        }
////
////        public String getPillSymptomResource() {
////            return pillSymptomResource;
////        }
////
////        public void setPillImageResource(Integer pillImageResource) {
////            this.pillImageResource = pillImageResource;
////        }
////
////        public void setPillNameResource(String pillNameResource) {
////            this.pillNameResource = pillNameResource;
////        }
////
////        public void setPillSymptomResource(String pillSymptomResource) {
////            this.pillSymptomResource = pillSymptomResource;
////        }
////    }
////
////    PillItem exampleItems[] = new PillItem[PILL_COUNT];
//
//
//    // 증상별 필터 ON/OFF 표시를 위한 boolean타입 배열
//    boolean[] symptomSwitch = new boolean[] {
//            false, false, false, false,
//            false, false, false, false
//    };
//
//    // 증상별 고유 번호(symptomSwitch ArrayList의 인덱싱에 쓰임)
//    static final int COLD = 0;
//    static final int DIGESTION = 1;
//    static final int DIARRHEA = 2;
//    static final int FEVER = 3;
//    static final int ACHE = 4;
//    static final int INFLAMMATION = 5;
//    static final int ADHD = 6;
//    static final int OTHERS = 7;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.search_pill);
//
//        searchBar = (TextView) findViewById(R.id.searchBar);
//        searchButton = (Button) findViewById(R.id.searchButton);
//        pillList = (ListView) findViewById(R.id.pillList);
//
////        // 검색 버튼 클릭 리스너 설정
////        setupSearchButtonClickListener(searchButton);
//
//        // ChipGroup 및 Chip 요소 참조
//        symptomGroup = findViewById(R.id.symptomGroup);
//
//        coldChip = findViewById(R.id.cold);
//        digestionChip = findViewById(R.id.digestion);
//        diarrheaChip = findViewById(R.id.diarrhea);
//        feverChip = findViewById(R.id.fever);
//        acheChip = findViewById(R.id.ache);
//        inflammationChip = findViewById(R.id.inflammation);
//        adhdChip = findViewById(R.id.adhd);
//        othersChip = findViewById(R.id.others);
//
//        // Chip 클릭 리스너 설정
//        setupChipClickListener(coldChip);
//        setupChipClickListener(digestionChip);
//        setupChipClickListener(diarrheaChip);
//        setupChipClickListener(feverChip);
//        setupChipClickListener(acheChip);
//        setupChipClickListener(inflammationChip);
//        setupChipClickListener(adhdChip);
//        setupChipClickListener(othersChip);
//
////        // 약 검색 리스트뷰의 어댑터 설정
////        for(int i = 0; i < itemCount; i++) {
////            itemRooms.add("temp");
////        }
//
//        CustomList adapter = new CustomList(SearchPillActivity.this, drugInfoList);
//        pillList.setAdapter(adapter);
//
//        // 약 검색 버튼 클릭시 실행(텍스트로 검색)
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String searchString = searchBar.getText().toString().trim();
//                searchPills(searchString);
//            }
//        });
//
//        // 데이터 받아오기
//        new FetchDataAsyncTask().execute();
//    }
//
////    // 약 검색 버튼 클릭시 실행(텍스트로 검색)
////    public void setupSearchButtonClickListener(Button button) {
////        button.setOnClickListener(new View.OnClickListener() {
////            // 변수 선언
////            boolean searchSuccess = false;
////            int searchItemCount = 100; // 검색되는 약의 최대 개수를 임시로 100으로 설정
////            int[] indexOfPill = new int[searchItemCount];
////            @Override
////            public void onClick(View v) {
////                // 버튼 클릭할 때마다 일부 변수값 초기화
////                searchSuccess = false;
////                indexOfPill = new int[searchItemCount];
////                indexOfPill[0] = -1;
////                itemCount = 0;
////
////                String searchString = searchBar.getText().toString();
////
////                pillList.setVisibility(View.VISIBLE);
////
////                for (int i = 0, position = -1; i < PILL_COUNT; i++) {
////                    if(exampleItems[i].getPillNameResource().contains(searchString)) {
////                        position++;
////                        indexOfPill[position] = i;
////                        itemCount++;
////                        searchSuccess = true;
////                    }
////                }
////
////                itemRooms.clear();
////                for(int i = 0; i < itemCount; i++) {
////                    itemRooms.add("temp");
////                }
////
////                CustomList newAdapterByText = new CustomList(SearchPillActivity.this) {
////                    @Override
////                    public View getView(int position, View view, ViewGroup parent) {
////                        LayoutInflater inflater = context.getLayoutInflater();
////                        View rowView = inflater.inflate(R.layout.pill_list_item, null);
////
////                        ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
////                        TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
////                        TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);
////
////                        Log.d("onClick", "searchSuccess: " + searchSuccess);
////                        Log.d("onClick", "position: " + position);
////
////                        if(searchSuccess) {
////                            pillList.setVisibility(View.VISIBLE);
////
////                            if(position <= indexOfPill[position]) {
////                                rowView.setVisibility(View.VISIBLE);
////
////                                pillImage.setImageResource(exampleItems[indexOfPill[position]].getPillImageResource());
////                                pillName.setText(exampleItems[indexOfPill[position]].getPillNameResource());
////                                pillSymptom.setText(exampleItems[indexOfPill[position]].getPillSymptomResource());
////
////                                return rowView;
////                            }
////                            else {
////                                Log.d("onClick", "position: " + position);
////                                rowView.setVisibility(View.GONE);
////
////                                return rowView;
////                            }
////                        }
////                        else {
////                            pillList.setVisibility(View.GONE);
////
////                            return rowView;
////                        }
////                    }
////                };
////                pillList.setAdapter(newAdapterByText);
////            }
////        });
////    }
//
//    // 증상별 필터 클릭시 해당 증상에 대한 약만 표시되도록 리스트뷰 업데이트
//    public void setupChipClickListener(Chip chip) {
//        chip.setOnClickListener(new View.OnClickListener() {
//            int[] indexOfPill = new int[PILL_COUNT];
//            int symptomOnCount;
//
//            @Override
//            public void onClick(View v) {
//                // 버튼 클릭할 때마다 일부 변수값 초기화
//                indexOfPill = new int[PILL_COUNT];
//                indexOfPill[0] = -1;
//                symptomOnCount = 0;
//                itemCount = 0;
//
//                String symptomString = chip.getText().toString();
//
//                // 해당 필터에 대한 boolean값 true/false 토글
//                switch(symptomString) {
//                    case "감기":
//                        symptomSwitch[COLD] = toggleSymptom(symptomSwitch[COLD]);
//                        break;
//                    case "소화":
//                        symptomSwitch[DIGESTION] = toggleSymptom(symptomSwitch[DIGESTION]);
//                        break;
//                    case "설사":
//                        symptomSwitch[DIARRHEA] = toggleSymptom(symptomSwitch[DIARRHEA]);
//                        break;
//                    case "열":
//                        symptomSwitch[FEVER] = toggleSymptom(symptomSwitch[FEVER]);
//                        break;
//                    case "통증":
//                        symptomSwitch[ACHE] = toggleSymptom(symptomSwitch[ACHE]);
//                        break;
//                    case "염증":
//                        symptomSwitch[INFLAMMATION] = toggleSymptom(symptomSwitch[INFLAMMATION]);
//                        break;
//                    case "ADHD":
//                        symptomSwitch[ADHD] = toggleSymptom(symptomSwitch[ADHD]);
//                        break;
//                    case "기타":
//                        symptomSwitch[OTHERS] = toggleSymptom(symptomSwitch[OTHERS]);
//                        break;
//                    default:
//                        break;
//                }
//
//                pillList.setVisibility(View.VISIBLE);
//
//                // ON 상태인 증상 개수 세기
//                for (boolean symptom : symptomSwitch) {
//                    if (symptom) {
//                        symptomOnCount++;
//                    }
//                }
//
//                if (symptomOnCount < 1) {
//                    CustomList adapter = new CustomList(SearchPillActivity.this, drugInfoList);
//                    pillList = (ListView)findViewById(R.id.pillList);
//                    pillList.setAdapter(adapter);
//                }
//                else if (symptomOnCount == 1) {
//                    int i = 0, position = 0;
//                    String symptomResource;
//
//                    List<DrugInfo> searchResultsBySymptom = new ArrayList<>();
//
//                    while(i < PILL_COUNT) {
//                        symptomResource = drugInfoList.get(i).getEfcyQesitm();
//
//                        if (symptomSwitch[COLD]) {
//                            if(symptomResource.contains("기침") || symptomResource.contains("콧물")
//                                    || symptomResource.contains("코막힘") || symptomResource.contains("코감기")) {
////                                indexOfPill[position] = i;
////                                position++;
////                                itemCount++;
//
//                                searchResultsBySymptom.add(drugInfoList.get(i));
//                            }
//                            i++;
//                        }
//
//                        if (symptomSwitch[DIGESTION]) {
//                            if(symptomResource.contains("소화불량")) {
////                                indexOfPill[position] = i;
////                                position++;
////                                itemCount++;
//
//                                searchResultsBySymptom.add(drugInfoList.get(i));
//                            }
//                            i++;
//                        }
//
//                        if (symptomSwitch[DIARRHEA]) {
//                            if(symptomResource.contains("설사")) {
////                                indexOfPill[position] = i;
////                                position++;
////                                itemCount++;
//
//                                searchResultsBySymptom.add(drugInfoList.get(i));
//                            }
//                            i++;
//                        }
//
//                        if (symptomSwitch[FEVER]) {
//                            if(symptomResource.contains("발열")) {
////                                indexOfPill[position] = i;
////                                position++;
////                                itemCount++;
//
//                                searchResultsBySymptom.add(drugInfoList.get(i));
//                            }
//                            i++;
//                        }
//
//                        if (symptomSwitch[ACHE]) {
//                            if(symptomResource.contains("두통") || symptomResource.contains("치통")
//                                    || symptomResource.contains("신경통") || symptomResource.contains("관절통")
//                                    || symptomResource.contains("근육통") || symptomResource.contains("요통")
//                                    || symptomResource.contains("염좌통")) {
////                                indexOfPill[position] = i;
////                                position++;
////                                itemCount++;
//
//                                searchResultsBySymptom.add(drugInfoList.get(i));
//                            }
//                            i++;
//                        }
//
//                        if (symptomSwitch[INFLAMMATION]) {
//                            if(symptomResource.contains("위염") || symptomResource.contains("장염")
//                                    || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
////                                indexOfPill[position] = i;
////                                position++;
////                                itemCount++;
//
//                                searchResultsBySymptom.add(drugInfoList.get(i));
//                            }
//                            i++;
//                        }
//
//                        if (symptomSwitch[ADHD]) {
//                            if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
////                                indexOfPill[position] = i;
////                                position++;
////                                itemCount++;
//
//                                searchResultsBySymptom.add(drugInfoList.get(i));
//                            }
//                            i++;
//                        }
//
//                        if (symptomSwitch[OTHERS]) {
//                            if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
//                                    && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
//                                    && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
//                                    && !symptomResource.contains("발열") && !symptomResource.contains("두통")
//                                    && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
//                                    && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
//                                    && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
//                                    && !symptomResource.contains("위염") && !symptomResource.contains("장염")
//                                    && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
//                                    && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
////                                indexOfPill[position] = i;
////                                position++;
////                                itemCount++;
//
//                                searchResultsBySymptom.add(drugInfoList.get(i));
//                            }
//                            i++;
//                        }
//                    }
//
//                    CustomList adapterBySymptom = new CustomList(SearchPillActivity.this, searchResultsBySymptom);
//                    pillList.setAdapter(adapterBySymptom);
//
//
////                    CustomList newAdapterBySymptom = new CustomList(SearchPillActivity.this, searchResultsBySymptom) {
////                        @Override
////                        public View getView(int position, View view, ViewGroup parent) {
////                            LayoutInflater inflater = context.getLayoutInflater();
////                            View rowView = inflater.inflate(R.layout.pill_list_item, null);
////
////                            ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
////                            TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
////                            TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);
////
////
////                            if(position <= indexOfPill[position]) {
////                                rowView.setVisibility(View.VISIBLE);
////
////                                pillImage.setImageResource(exampleItems[indexOfPill[position]].getPillImageResource());
////                                pillName.setText(exampleItems[indexOfPill[position]].getPillNameResource());
////                                pillSymptom.setText(exampleItems[indexOfPill[position]].getPillSymptomResource());
////
////                                return rowView;
////                            }
////                            else {
////                                rowView.setVisibility(View.GONE);
////
////                                return rowView;
////                            }
////                        }
////                    };
////                    pillList.setAdapter(newAdapterBySymptom);
//                }
//                else { // 필터가 2개이상 ON일 경우
//                    int i = 0, position = 0;
//                    String prev = null;
//                    String symptomResource;
//
//                    List<DrugInfo> searchResultsBySymptoms = new ArrayList<>();
//                    if (symptomSwitch[COLD]) {
//                        while (i < PILL_COUNT) {
//                            symptomResource = drugInfoList.get(i).getEfcyQesitm();
//                            if(symptomResource.contains("기침") || symptomResource.contains("콧물")
//                                    || symptomResource.contains("코막힘") || symptomResource.contains("코감기")) {
//                                indexOfPill[position] = i;
//                                position++;
//                                itemCount++;
//                            }
//                            i++;
//                        }
//                        prev = "감기";
//                    }
//
//                    if (symptomSwitch[DIGESTION]) {
//                        if (prev == null) {
//                            while (i < PILL_COUNT) {
//                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
//                                if(symptomResource.contains("소화불량")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//                            prev = "소화";
//                        }
//                        else {
//                            i = 0;
//                            position = 0;
//                            itemCount = 0;
//
//                            if (indexOfPill[i] >= 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("소화불량")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            while (indexOfPill[i] > 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("소화불량")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            if (itemCount == 0) {
//                                searchResultsBySymptoms.clear();
//
//                                CustomList adapter = new CustomList(SearchPillActivity.this, searchResultsBySymptoms);
//                                pillList.setAdapter(adapter);
//
//                                prev = "소화";
//                                return;
//                            }
//                            else {
//                                if (indexOfPill[itemCount] > 0) {
//                                    for (int j = itemCount; j < PILL_COUNT; j++) {
//                                        indexOfPill[j] = 0;
//                                    }
//                                }
//                                prev = "소화";
//                            }
//                        }
//                    }
//
//                    if (symptomSwitch[DIARRHEA]) {
//                        if (prev == null) {
//                            while (i < PILL_COUNT) {
//                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
//                                if(symptomResource.contains("설사")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//                            prev = "설사";
//                        }
//                        else {
//                            i = 0;
//                            position = 0;
//                            itemCount = 0;
//
//                            if (indexOfPill[i] >= 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("설사")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            while (indexOfPill[i] > 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("설사")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            if (itemCount == 0) {
//                                searchResultsBySymptoms.clear();
//
//                                CustomList adapter = new CustomList(SearchPillActivity.this, searchResultsBySymptoms);
//                                pillList.setAdapter(adapter);
//
//                                prev = "설사";
//                                return;
//                            }
//                            else {
//                                if (indexOfPill[itemCount] > 0) {
//                                    for (int j = itemCount; j < PILL_COUNT; j++) {
//                                        indexOfPill[j] = 0;
//                                    }
//                                }
//                                prev = "설사";
//                            }
//                        }
//                    }
//
//                    if (symptomSwitch[FEVER]) {
//                        if (prev == null) {
//                            while (i < PILL_COUNT) {
//                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
//                                if(symptomResource.contains("발열")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//                            prev = "열";
//                        }
//                        else {
//                            i = 0;
//                            position = 0;
//                            itemCount = 0;
//
//                            if (indexOfPill[i] >= 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("발열")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            while (indexOfPill[i] > 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("발열")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            if (itemCount == 0) {
//                                searchResultsBySymptoms.clear();
//
//                                CustomList adapter = new CustomList(SearchPillActivity.this, searchResultsBySymptoms);
//                                pillList.setAdapter(adapter);
//
//                                prev = "열";
//                                return;
//                            }
//                            else {
//                                if (indexOfPill[itemCount] > 0) {
//                                    for (int j = itemCount; j < PILL_COUNT; j++) {
//                                        indexOfPill[j] = 0;
//                                    }
//                                }
//                                prev = "열";
//                            }
//                        }
//                    }
//
//                    if (symptomSwitch[ACHE]) {
//                        if (prev == null) {
//                            while (i < PILL_COUNT) {
//                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
//                                if(symptomResource.contains("두통") || symptomResource.contains("치통")
//                                        || symptomResource.contains("신경통") || symptomResource.contains("관절통")
//                                        || symptomResource.contains("근육통") || symptomResource.contains("요통")
//                                        || symptomResource.contains("염좌통")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//                            prev = "통증";
//                        }
//                        else {
//                            i = 0;
//                            position = 0;
//                            itemCount = 0;
//
//                            if (indexOfPill[i] >= 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("두통") || symptomResource.contains("치통")
//                                        || symptomResource.contains("신경통") || symptomResource.contains("관절통")
//                                        || symptomResource.contains("근육통") || symptomResource.contains("요통")
//                                        || symptomResource.contains("염좌통")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            while (indexOfPill[i] > 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("두통") || symptomResource.contains("치통")
//                                        || symptomResource.contains("신경통") || symptomResource.contains("관절통")
//                                        || symptomResource.contains("근육통") || symptomResource.contains("요통")
//                                        || symptomResource.contains("염좌통")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            if (itemCount == 0) {
//                                searchResultsBySymptoms.clear();
//
//                                CustomList adapter = new CustomList(SearchPillActivity.this, searchResultsBySymptoms);
//                                pillList.setAdapter(adapter);
//
//                                prev = "통증";
//                                return;
//                            }
//                            else {
//                                if (indexOfPill[itemCount] > 0) {
//                                    for (int j = itemCount; j < PILL_COUNT; j++) {
//                                        indexOfPill[j] = 0;
//                                    }
//                                }
//                                prev = "통증";
//                            }
//                        }
//                    }
//
//                    if (symptomSwitch[INFLAMMATION]) {
//                        if (prev == null) {
//                            while (i < PILL_COUNT) {
//                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
//                                if(symptomResource.contains("위염") || symptomResource.contains("장염")
//                                        || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//                            prev = "염증";
//                        }
//                        else {
//                            i = 0;
//                            position = 0;
//                            itemCount = 0;
//
//                            if (indexOfPill[i] >= 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("위염") || symptomResource.contains("장염")
//                                        || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            while (indexOfPill[i] > 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("위염") || symptomResource.contains("장염")
//                                        || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            if (itemCount == 0) {
//                                searchResultsBySymptoms.clear();
//
//                                CustomList adapter = new CustomList(SearchPillActivity.this, searchResultsBySymptoms);
//                                pillList.setAdapter(adapter);
//
//                                prev = "염증";
//                                return;
//                            }
//                            else {
//                                if (indexOfPill[itemCount] > 0) {
//                                    for (int j = itemCount; j < PILL_COUNT; j++) {
//                                        indexOfPill[j] = 0;
//                                    }
//                                }
//                                prev = "염증";
//                            }
//                        }
//                    }
//
//                    if (symptomSwitch[ADHD]) {
//                        if (prev == null) {
//                            while (i < PILL_COUNT) {
//                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
//                                if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//                            prev = "ADHD";
//                        }
//                        else {
//                            i = 0;
//                            position = 0;
//                            itemCount = 0;
//
//                            if (indexOfPill[i] >= 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            while (indexOfPill[i] > 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            if (itemCount == 0) {
//                                searchResultsBySymptoms.clear();
//
//                                CustomList adapter = new CustomList(SearchPillActivity.this, searchResultsBySymptoms);
//                                pillList = (ListView)findViewById(R.id.pillList);
//                                pillList.setAdapter(adapter);
//
//                                prev = "ADHD";
//                                return;
//                            }
//                            else {
//                                if (indexOfPill[itemCount] > 0) {
//                                    for (int j = itemCount; j < PILL_COUNT; j++) {
//                                        indexOfPill[j] = 0;
//                                    }
//                                }
//                                prev = "ADHD";
//                            }
//                        }
//                    }
//
//                    if (symptomSwitch[OTHERS]) {
//                        if (prev == null) {
//                            while (i < PILL_COUNT) {
//                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
//                                if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
//                                        && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
//                                        && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
//                                        && !symptomResource.contains("발열") &&!symptomResource.contains("두통")
//                                        && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
//                                        && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
//                                        && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
//                                        && !symptomResource.contains("위염") && !symptomResource.contains("장염")
//                                        && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
//                                        && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//                        }
//                        else {
//                            i = 0;
//                            position = 0;
//                            itemCount = 0;
//
//                            if (indexOfPill[i] >= 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
//                                        && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
//                                        && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
//                                        && !symptomResource.contains("발열") &&!symptomResource.contains("두통")
//                                        && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
//                                        && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
//                                        && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
//                                        && !symptomResource.contains("위염") && !symptomResource.contains("장염")
//                                        && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
//                                        && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
//                                    indexOfPill[position] = indexOfPill[i];
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            while (indexOfPill[i] > 0) {
//                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
//                                if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
//                                        && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
//                                        && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
//                                        && !symptomResource.contains("발열") &&!symptomResource.contains("두통")
//                                        && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
//                                        && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
//                                        && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
//                                        && !symptomResource.contains("위염") && !symptomResource.contains("장염")
//                                        && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
//                                        && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
//                                    indexOfPill[position] = i;
//                                    position++;
//                                    itemCount++;
//                                }
//                                i++;
//                            }
//
//                            if (itemCount == 0) {
//                                searchResultsBySymptoms.clear();
//
//                                CustomList adapter = new CustomList(SearchPillActivity.this, searchResultsBySymptoms);
//                                pillList = (ListView)findViewById(R.id.pillList);
//                                pillList.setAdapter(adapter);
//
//                                return;
//                            }
//                            else {
//                                if (indexOfPill[itemCount] > 0) {
//                                    for (int j = itemCount; j < PILL_COUNT; j++) {
//                                        indexOfPill[j] = 0;
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    for (int j = 0; j < itemCount; j++) {
//                        searchResultsBySymptoms.add(drugInfoList.get(indexOfPill[j]));
//                    }
//
//                    CustomList adapterBySymptoms = new CustomList(SearchPillActivity.this, searchResultsBySymptoms);
//                    pillList.setAdapter(adapterBySymptoms);
//
////                    CustomList newAdapterBySymptom = new CustomList(SearchPillActivity.this) {
////                        @Override
////                        public View getView(int position, View view, ViewGroup parent) {
////                            LayoutInflater inflater = context.getLayoutInflater();
////                            View rowView = inflater.inflate(R.layout.pill_list_item, null);
////
////                            ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
////                            TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
////                            TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);
////
////                            if(position <= indexOfPill[position]) {
////                                rowView.setVisibility(View.VISIBLE);
////
////                                pillImage.setImageResource(exampleItems[indexOfPill[position]].getPillImageResource());
////                                pillName.setText(exampleItems[indexOfPill[position]].getPillNameResource());
////                                pillSymptom.setText(exampleItems[indexOfPill[position]].getPillSymptomResource());
////
////                                return rowView;
////                            }
////                            else {
////                                rowView.setVisibility(View.GONE);
////
////                                return rowView;
////                            }
////                        }
////                    };
////                    pillList.setAdapter(newAdapterBySymptom);
//                }
//            }
//        });
//    }
//
//    // 증상별 필터 ON/OFF로 전환해주는 Toggle 기능
//    public boolean toggleSymptom(boolean symptom) {
//        return !symptom;
//    }
//
//    private void searchPills(String searchString) {
//        if (drugInfoList != null) {
//            // 검색 결과를 담을 리스트 생성
//            List<DrugInfo> searchResults = new ArrayList<>();
//
//            // 입력된 검색어와 일치하는 약품 찾기
//            for (DrugInfo drugInfo : drugInfoList) {
//                if (drugInfo.getItemName().contains(searchString)) {
//                    searchResults.add(drugInfo);
//                }
//            }
//
//            // 검색 결과를 이용하여 리스트뷰 업데이트
//            CustomList newAdapter = new CustomList(SearchPillActivity.this, searchResults);
//            pillList.setAdapter(newAdapter);
//        }
//    }
//
//    // AsyncTask 정의
//    private class FetchDataAsyncTask extends AsyncTask<Void, Void, List<DrugInfo>> {
//        @Override
//        protected List<DrugInfo> doInBackground(Void... voids) {
//            // 데이터를 받아오는 작업을 수행
//            // 여기서는 간단히 예시 데이터를 생성하여 반환
//            return createSampleData();
//        }
//
//        @Override
//        protected void onPostExecute(List<DrugInfo> result) {
//            // 데이터를 받아오면 drugInfoList에 저장하고 UI 업데이트
//            drugInfoList = result;
//            if (drugInfoList != null) {
//                Log.d("drugInfoList", drugInfoList.toString());
//            }
//        }
//    }
//
//    // 예시 데이터 생성 메서드
//    private List<DrugInfo> createSampleData() {
//        List<DrugInfo> sampleData = new ArrayList<>();
//        for (int i = 0; i < testNames.length; i++) {
//            DrugInfo drugInfo = new DrugInfo();
//            drugInfo.setItemName(testNames[i]);
//
//            // 이미지 설정 등 필요한 데이터 설정
//            sampleData.add(drugInfo);
//        }
//        return sampleData;
//    }
//
//    // 리스트뷰에 활용할 어댑터 정의
//    private class CustomList extends ArrayAdapter<DrugInfo> {
//        public final Activity context;
//        public final List<DrugInfo> itemList;
//
//        public CustomList(Activity context, List<DrugInfo> itemList) {
//            super(context, R.layout.pill_list_item, itemList);
//            this.context = context;
//            this.itemList = itemList;
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup parent) {
//            LayoutInflater inflater = context.getLayoutInflater();
//            View rowView = inflater.inflate(R.layout.pill_list_item, null);
//
//            ImageView pillImage = rowView.findViewById(R.id.pillImage);
//            TextView pillName = rowView.findViewById(R.id.pillName);
//            TextView pillSymptom = rowView.findViewById(R.id.pillSymptom);
//
//            // 현재 position에 해당하는 DrugInfo 객체 가져오기
//            DrugInfo currentItem = itemList.get(position);
//
//            // DrugInfo 객체의 정보를 화면에 설정
//            // 예시 데이터를 이용할 경우, 이미지 및 제조사 정보도 설정해야 합니다.
////            pillImage.setImageResource(currentItem.getItemImage());
//            pillName.setText(currentItem.getItemName());
//            pillSymptom.setText(currentItem.getEfcyQesitm());
//
//            return rowView;
//        }
//    }
//}
//
//
