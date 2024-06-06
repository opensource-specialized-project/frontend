package com.medikok.frontend.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.medikok.frontend.R;
import com.medikok.frontend.model.DrugInfo;
import com.medikok.frontend.util.ServerConnector;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<DrugInfo> drugInfoList;

    ListView drugListView;
    EditText searchBar;
    Button searchButton;

    ChipGroup symptomGroup;
    Chip coldChip, digestionChip, diarrheaChip,
            feverChip, acheChip, inflammationChip,
            adhdChip, othersChip;

    static final int PILL_COUNT = 100;
    // 검색된 리스트 항목 개수
    int itemCount = PILL_COUNT;

    boolean[] symptomSwitch = new boolean[] {
            false, false, false, false,
            false, false, false, false
    };

    // 증상별 고유 번호(symptomSwitch ArrayList의 인덱싱에 쓰임)
    static final int COLD = 0;
    static final int DIGESTION = 1;
    static final int DIARRHEA = 2;
    static final int FEVER = 3;
    static final int ACHE = 4;
    static final int INFLAMMATION = 5;
    static final int ADHD = 6;
    static final int OTHERS = 7;

    // Fragment가 생성될 때 호출되는 메서드
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Fragment의 뷰가 생성될 때 호출되는 메서드
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment의 레이아웃을 인플레이트
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchBar = (EditText) view.findViewById(R.id.searchBar);
        searchButton = (Button) view.findViewById(R.id.searchButton);
        drugListView = (ListView) view.findViewById(R.id.drugListView);

<<<<<<< HEAD
=======
        // 검색 버튼 클릭 리스너 설정
//        setupSearchButtonClickListener(searchButton);

>>>>>>> 3fccce1dc54cfbce0dfb47efa1e427c3f1887722
        // ChipGroup 및 Chip 요소 참조
        symptomGroup = view.findViewById(R.id.symptomGroup);

        coldChip = view.findViewById(R.id.cold);
        digestionChip = view.findViewById(R.id.digestion);
        diarrheaChip = view.findViewById(R.id.diarrhea);
        feverChip = view.findViewById(R.id.fever);
        acheChip = view.findViewById(R.id.ache);
        inflammationChip = view.findViewById(R.id.inflammation);
        adhdChip = view.findViewById(R.id.adhd);
        othersChip = view.findViewById(R.id.others);

        // Chip 클릭 리스너 설정
<<<<<<< HEAD
        setupChipClickListener(coldChip);
        setupChipClickListener(digestionChip);
        setupChipClickListener(diarrheaChip);
        setupChipClickListener(feverChip);
        setupChipClickListener(acheChip);
        setupChipClickListener(inflammationChip);
        setupChipClickListener(adhdChip);
        setupChipClickListener(othersChip);
=======
//        setupChipClickListener(coldChip);
//        setupChipClickListener(digestionChip);
//        setupChipClickListener(diarrheaChip);
//        setupChipClickListener(feverChip);
//        setupChipClickListener(acheChip);
//        setupChipClickListener(inflammationChip);
//        setupChipClickListener(adhdChip);
//        setupChipClickListener(othersChip);
>>>>>>> 3fccce1dc54cfbce0dfb47efa1e427c3f1887722

        // DrugInfo 리스트 초기화
        drugInfoList = new ArrayList<>();

        // 데이터 가져오기
        ServerConnector.connectToServer(new ServerConnector.ServerResponseListener() {
            @Override
            public void onSuccess(List<DrugInfo> responseData) {
                // 가져온 데이터를 drugInfoList에 저장
                drugInfoList.clear();
                drugInfoList.addAll(responseData);

                CustomList adapter = new CustomList(requireActivity(), drugInfoList);
                drugListView.setAdapter(adapter);

                // 로그로 데이터 출력
                for (DrugInfo drugInfo : drugInfoList) {
                    Log.d("SearchFragment", "Item Name: " + drugInfo.getItemName());
                    Log.d("SearchFragment", "Effects: " + drugInfo.getEfcyQesitm());
                    Log.d("SearchFragment", "Use Method: " + drugInfo.getUseMethodQesitm());
                    Log.d("SearchFragment", "Precautions Before Use: " + drugInfo.getAtpnWarnQesitm());
                    Log.d("SearchFragment", "Precautions: " + drugInfo.getAtpnQesitm());
                    Log.d("SearchFragment", "Interactions: " + drugInfo.getIntrcQesitm());
                    Log.d("SearchFragment", "Storage Precautions: " + drugInfo.getDepositMethodQesitm());
                    Log.d("SearchFragment", "Image URL: " + drugInfo.getItemImage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("SearchFragment", "Failed to fetch data: " + errorMessage);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchBar.getText().toString().trim();
                searchPills(searchString);
            }
        });

        return view;
    }

<<<<<<< HEAD
    // 증상별 필터 클릭시 해당 증상에 대한 약만 표시되도록 리스트뷰 업데이트
    public void setupChipClickListener(Chip chip) {
        chip.setOnClickListener(new View.OnClickListener() {
            int[] indexOfPill = new int[PILL_COUNT];
            int symptomOnCount;

            @Override
            public void onClick(View v) {
                // 버튼 클릭할 때마다 일부 변수값 초기화
                indexOfPill = new int[PILL_COUNT];
                indexOfPill[0] = -1;
                symptomOnCount = 0;
                itemCount = 0;

                String symptomString = chip.getText().toString();

                // 해당 필터에 대한 boolean값 true/false 토글
                switch(symptomString) {
                    case "감기":
                        symptomSwitch[COLD] = toggleSymptom(symptomSwitch[COLD]);
                        break;
                    case "소화":
                        symptomSwitch[DIGESTION] = toggleSymptom(symptomSwitch[DIGESTION]);
                        break;
                    case "설사":
                        symptomSwitch[DIARRHEA] = toggleSymptom(symptomSwitch[DIARRHEA]);
                        break;
                    case "열":
                        symptomSwitch[FEVER] = toggleSymptom(symptomSwitch[FEVER]);
                        break;
                    case "통증":
                        symptomSwitch[ACHE] = toggleSymptom(symptomSwitch[ACHE]);
                        break;
                    case "염증":
                        symptomSwitch[INFLAMMATION] = toggleSymptom(symptomSwitch[INFLAMMATION]);
                        break;
                    case "ADHD":
                        symptomSwitch[ADHD] = toggleSymptom(symptomSwitch[ADHD]);
                        break;
                    case "기타":
                        symptomSwitch[OTHERS] = toggleSymptom(symptomSwitch[OTHERS]);
                        break;
                    default:
                        break;
                }

                drugListView.setVisibility(View.VISIBLE);

                // ON 상태인 증상 개수 세기
                for (boolean symptom : symptomSwitch) {
                    if (symptom) {
                        symptomOnCount++;
                    }
                }

                if (symptomOnCount < 1) {
                    drugListView = (ListView)getView().findViewById(R.id.drugListView);

                    CustomList adapter = new CustomList(requireActivity(), drugInfoList);
                    drugListView.setAdapter(adapter);
                }
                else if (symptomOnCount == 1) {
                    int i = 0, position = 0;
                    String symptomResource;

                    List<DrugInfo> searchResultsBySymptom = new ArrayList<>();

                    while(i < PILL_COUNT) {
                        symptomResource = drugInfoList.get(i).getEfcyQesitm();

                        if (symptomSwitch[COLD]) {
                            if(symptomResource.contains("기침") || symptomResource.contains("콧물")
                                    || symptomResource.contains("코막힘") || symptomResource.contains("코감기")) {
                                searchResultsBySymptom.add(drugInfoList.get(i));
                            }
                            i++;
                        }

                        if (symptomSwitch[DIGESTION]) {
                            if(symptomResource.contains("소화불량")) {
                                searchResultsBySymptom.add(drugInfoList.get(i));
                            }
                            i++;
                        }

                        if (symptomSwitch[DIARRHEA]) {
                            if(symptomResource.contains("설사")) {
                                searchResultsBySymptom.add(drugInfoList.get(i));
                            }
                            i++;
                        }

                        if (symptomSwitch[FEVER]) {
                            if(symptomResource.contains("발열")) {
                                searchResultsBySymptom.add(drugInfoList.get(i));
                            }
                            i++;
                        }

                        if (symptomSwitch[ACHE]) {
                            if(symptomResource.contains("두통") || symptomResource.contains("치통")
                                    || symptomResource.contains("신경통") || symptomResource.contains("관절통")
                                    || symptomResource.contains("근육통") || symptomResource.contains("요통")
                                    || symptomResource.contains("염좌통")) {
                                searchResultsBySymptom.add(drugInfoList.get(i));
                            }
                            i++;
                        }

                        if (symptomSwitch[INFLAMMATION]) {
                            if(symptomResource.contains("위염") || symptomResource.contains("장염")
                                    || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
                                searchResultsBySymptom.add(drugInfoList.get(i));
                            }
                            i++;
                        }

                        if (symptomSwitch[ADHD]) {
                            if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
                                searchResultsBySymptom.add(drugInfoList.get(i));
                            }
                            i++;
                        }

                        if (symptomSwitch[OTHERS]) {
                            if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
                                    && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
                                    && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
                                    && !symptomResource.contains("발열") && !symptomResource.contains("두통")
                                    && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
                                    && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
                                    && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
                                    && !symptomResource.contains("위염") && !symptomResource.contains("장염")
                                    && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
                                    && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
                                searchResultsBySymptom.add(drugInfoList.get(i));
                            }
                            i++;
                        }
                    }

                    CustomList adapter = new CustomList(requireActivity(), searchResultsBySymptom);
                    drugListView.setAdapter(adapter);
                }
                else { // 필터가 2개이상 ON일 경우
                    int i = 0, position = 0;
                    String prev = null;
                    String symptomResource;

                    List<DrugInfo> searchResultsBySymptoms = new ArrayList<>();
                    if (symptomSwitch[COLD]) {
                        while (i < PILL_COUNT) {
                            symptomResource = drugInfoList.get(i).getEfcyQesitm();
                            if(symptomResource.contains("기침") || symptomResource.contains("콧물")
                                    || symptomResource.contains("코막힘") || symptomResource.contains("코감기")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }
                        prev = "감기";
                    }

                    if (symptomSwitch[DIGESTION]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
                                if(symptomResource.contains("소화불량")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "소화";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("소화불량")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("소화불량")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                searchResultsBySymptoms.clear();

                                CustomList adapter = new CustomList(requireActivity(), searchResultsBySymptoms);
                                drugListView.setAdapter(adapter);

                                prev = "소화";
                                return;
                            }
                            else {
                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "소화";
                            }
                        }
                    }

                    if (symptomSwitch[DIARRHEA]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
                                if(symptomResource.contains("설사")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "설사";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("설사")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("설사")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                searchResultsBySymptoms.clear();

                                CustomList adapter = new CustomList(requireActivity(), searchResultsBySymptoms);
                                drugListView.setAdapter(adapter);

                                prev = "설사";
                                return;
                            }
                            else {
                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "설사";
                            }
                        }
                    }

                    if (symptomSwitch[FEVER]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
                                if(symptomResource.contains("발열")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "열";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("발열")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("발열")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                searchResultsBySymptoms.clear();

                                CustomList adapter = new CustomList(requireActivity(), searchResultsBySymptoms);
                                drugListView.setAdapter(adapter);

                                prev = "열";
                                return;
                            }
                            else {
                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "열";
                            }
                        }
                    }

                    if (symptomSwitch[ACHE]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
                                if(symptomResource.contains("두통") || symptomResource.contains("치통")
                                        || symptomResource.contains("신경통") || symptomResource.contains("관절통")
                                        || symptomResource.contains("근육통") || symptomResource.contains("요통")
                                        || symptomResource.contains("염좌통")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "통증";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("두통") || symptomResource.contains("치통")
                                        || symptomResource.contains("신경통") || symptomResource.contains("관절통")
                                        || symptomResource.contains("근육통") || symptomResource.contains("요통")
                                        || symptomResource.contains("염좌통")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("두통") || symptomResource.contains("치통")
                                        || symptomResource.contains("신경통") || symptomResource.contains("관절통")
                                        || symptomResource.contains("근육통") || symptomResource.contains("요통")
                                        || symptomResource.contains("염좌통")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                searchResultsBySymptoms.clear();

                                CustomList adapter = new CustomList(requireActivity(), searchResultsBySymptoms);
                                drugListView.setAdapter(adapter);

                                prev = "통증";
                                return;
                            }
                            else {
                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "통증";
                            }
                        }
                    }

                    if (symptomSwitch[INFLAMMATION]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
                                if(symptomResource.contains("위염") || symptomResource.contains("장염")
                                        || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "염증";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("위염") || symptomResource.contains("장염")
                                        || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("위염") || symptomResource.contains("장염")
                                        || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                searchResultsBySymptoms.clear();

                                CustomList adapter = new CustomList(requireActivity(), searchResultsBySymptoms);
                                drugListView.setAdapter(adapter);

                                prev = "염증";
                                return;
                            }
                            else {
                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "염증";
                            }
                        }
                    }

                    if (symptomSwitch[ADHD]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
                                if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "ADHD";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                searchResultsBySymptoms.clear();

                                CustomList adapter = new CustomList(requireActivity(), searchResultsBySymptoms);
                                drugListView = (ListView)getView().findViewById(R.id.drugListView);
                                drugListView.setAdapter(adapter);

                                prev = "ADHD";
                                return;
                            }
                            else {
                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "ADHD";
                            }
                        }
                    }

                    if (symptomSwitch[OTHERS]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = drugInfoList.get(i).getEfcyQesitm();
                                if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
                                        && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
                                        && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
                                        && !symptomResource.contains("발열") &&!symptomResource.contains("두통")
                                        && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
                                        && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
                                        && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
                                        && !symptomResource.contains("위염") && !symptomResource.contains("장염")
                                        && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
                                        && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
                                        && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
                                        && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
                                        && !symptomResource.contains("발열") &&!symptomResource.contains("두통")
                                        && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
                                        && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
                                        && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
                                        && !symptomResource.contains("위염") && !symptomResource.contains("장염")
                                        && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
                                        && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = drugInfoList.get(indexOfPill[i]).getEfcyQesitm();
                                if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
                                        && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
                                        && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
                                        && !symptomResource.contains("발열") &&!symptomResource.contains("두통")
                                        && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
                                        && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
                                        && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
                                        && !symptomResource.contains("위염") && !symptomResource.contains("장염")
                                        && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
                                        && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                searchResultsBySymptoms.clear();

                                CustomList adapter = new CustomList(requireActivity(), searchResultsBySymptoms);
                                drugListView = (ListView)getView().findViewById(R.id.drugListView);
                                drugListView.setAdapter(adapter);

                                return;
                            }
                            else {
                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                            }
                        }
                    }

                    for (int j = 0; j < itemCount; j++) {
                        searchResultsBySymptoms.add(drugInfoList.get(indexOfPill[j]));
                    }

                    CustomList adapterBySymptoms = new CustomList(requireActivity(), searchResultsBySymptoms);
                    drugListView.setAdapter(adapterBySymptoms);
                }
            }
        });
    }

    //    // 약 검색 버튼 클릭시 실행(텍스트로 검색)
=======
    // 약 검색 버튼 클릭시 실행(텍스트로 검색)
//    public void setupSearchButtonClickListener(Button button) {
//        button.setOnClickListener(new View.OnClickListener() {
//            // 변수 선언
//            boolean searchSuccess = false;
//            int searchItemCount = 100; // 검색되는 약의 최대 개수를 임시로 100으로 설정
//            int[] indexOfPill = new int[searchItemCount];
//            @Override
//            public void onClick(View v) {
//                // 버튼 클릭할 때마다 일부 변수값 초기화
//                searchSuccess = false;
//                indexOfPill = new int[searchItemCount];
//                indexOfPill[0] = -1;
//                itemCount = 0;
//
//                String searchString = searchBar.getText().toString();
//
//                pillList.setVisibility(View.VISIBLE);
//
//                for (int i = 0, position = -1; i < PILL_COUNT; i++) {
//                    if(exampleItems[i].getPillNameResource().contains(searchString)) {
//                        position++;
//                        indexOfPill[position] = i;
//                        itemCount++;
//                        searchSuccess = true;
//                    }
//                }
//
//                CustomList newAdapterByText = new CustomList(SearchPillActivity.this) {
//                    @Override
//                    public View getView(int position, View view, ViewGroup parent) {
//                        LayoutInflater inflater = context.getLayoutInflater();
//                        View rowView = inflater.inflate(R.layout.pill_list_item, null);
//
//                        ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
//                        TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
//                        TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);
//
//                        Log.d("onClick", "searchSuccess: " + searchSuccess);
//                        Log.d("onClick", "position: " + position);
//
//                        if(searchSuccess) {
//                            pillList.setVisibility(View.VISIBLE);
//
//                            if(position <= indexOfPill[position]) {
//                                rowView.setVisibility(View.VISIBLE);
//
//                                pillImage.setImageResource(exampleItems[indexOfPill[position]].getPillImageResource());
//                                pillName.setText(exampleItems[indexOfPill[position]].getPillNameResource());
//                                pillSymptom.setText(exampleItems[indexOfPill[position]].getPillSymptomResource());
//
//                                return rowView;
//                            }
//                            else {
//                                Log.d("onClick", "position: " + position);
//                                rowView.setVisibility(View.GONE);
//
//                                return rowView;
//                            }
//                        }
//                        else {
//                            pillList.setVisibility(View.GONE);
//
//                            return rowView;
//                        }
//                    }
//                };
//                pillList.setAdapter(newAdapterByText);
//            }
//        });
//    }
>>>>>>> 3fccce1dc54cfbce0dfb47efa1e427c3f1887722

    // Fragment 초기화 메서드
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

<<<<<<< HEAD
    // 증상별 필터 ON/OFF로 전환해주는 Toggle 기능
    public boolean toggleSymptom(boolean symptom) {
        return !symptom;
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
            CustomList newAdapter = new CustomList(requireActivity(), searchResults);
            drugListView.setAdapter(newAdapter);
        }
    }

    // 리스트뷰에 활용할 어댑터 정의
    public class CustomList extends ArrayAdapter<DrugInfo> {
        public final Activity context;
        public final List<DrugInfo> itemList;

        public CustomList(Activity context, List<DrugInfo> itemList) {
            super(context, R.layout.pill_list_item, itemList);
            this.context = context;
            this.itemList = itemList;
=======
    // CustomList 내부 클래스 정의
    public class CustomList extends ArrayAdapter<DrugInfo> {
        // 생성자 정의
        public CustomList(Activity context, List<DrugInfo> itemList) {
            super(context, R.layout.pill_list_item, itemList);
>>>>>>> 3fccce1dc54cfbce0dfb47efa1e427c3f1887722
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
<<<<<<< HEAD
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.pill_list_item, null);

            ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
            TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
            TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);

            // 현재 position에 해당하는 DrugInfo 객체 가져오기
            DrugInfo currentItem = itemList.get(position);

            // DrugInfo 객체의 정보를 화면에 설정
=======
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View rowView = inflater.inflate(R.layout.pill_list_item, null);

            ImageView pillImage = rowView.findViewById(R.id.pillImage);
            TextView pillName = rowView.findViewById(R.id.pillName);
            TextView pillSymptom = rowView.findViewById(R.id.pillSymptom);

            // 현재 position에 해당하는 DrugInfo 객체 가져오기
            DrugInfo currentItem = getItem(position);

>>>>>>> 3fccce1dc54cfbce0dfb47efa1e427c3f1887722
            String imageUrl = currentItem.getItemImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // 이미지가 있는 경우 Glide를 사용하여 이미지를 설정합니다.
                Glide.with(getContext())
                        .load(imageUrl)
                        .error(R.drawable.no_image) // 이미지 로드 중 오류가 발생할 경우 no_image 리소스를 표시합니다.
                        .into(pillImage);
            } else {
                // 이미지가 없는 경우 drawable 리소스에서 no_image를 사용합니다.
                pillImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.no_image));
            }
            pillName.setText(currentItem.getItemName());
            pillSymptom.setText(currentItem.getEfcyQesitm());

<<<<<<< HEAD

=======
>>>>>>> 3fccce1dc54cfbce0dfb47efa1e427c3f1887722
            return rowView;
        }
    }
}
