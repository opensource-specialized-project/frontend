package com.medikok.frontend.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medikok.frontend.R;
import com.medikok.frontend.model.DrugInfo;
import com.medikok.frontend.util.ServerConnector;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<DrugInfo> drugInfoList;

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

        // DrugInfo 리스트 초기화
        drugInfoList = new ArrayList<>();

        // 데이터 가져오기
        ServerConnector.connectToServer(new ServerConnector.ServerResponseListener() {
            @Override
            public void onSuccess(List<DrugInfo> responseData) {
                // 가져온 데이터를 drugInfoList에 저장
                drugInfoList.clear();
                drugInfoList.addAll(responseData);

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

    // Fragment 초기화 메서드
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
