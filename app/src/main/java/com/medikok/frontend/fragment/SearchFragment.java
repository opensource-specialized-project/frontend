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

        // 검색 버튼 클릭 리스너 설정
//        setupSearchButtonClickListener(searchButton);

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
//        setupChipClickListener(coldChip);
//        setupChipClickListener(digestionChip);
//        setupChipClickListener(diarrheaChip);
//        setupChipClickListener(feverChip);
//        setupChipClickListener(acheChip);
//        setupChipClickListener(inflammationChip);
//        setupChipClickListener(adhdChip);
//        setupChipClickListener(othersChip);

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

        return view;
    }

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

    // Fragment 초기화 메서드
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // CustomList 내부 클래스 정의
    public class CustomList extends ArrayAdapter<DrugInfo> {
        // 생성자 정의
        public CustomList(Activity context, List<DrugInfo> itemList) {
            super(context, R.layout.pill_list_item, itemList);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View rowView = inflater.inflate(R.layout.pill_list_item, null);

            ImageView pillImage = rowView.findViewById(R.id.pillImage);
            TextView pillName = rowView.findViewById(R.id.pillName);
            TextView pillSymptom = rowView.findViewById(R.id.pillSymptom);

            // 현재 position에 해당하는 DrugInfo 객체 가져오기
            DrugInfo currentItem = getItem(position);

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

            return rowView;
        }
    }
}
