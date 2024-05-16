package com.medikok.frontend.util;

import android.os.AsyncTask;
import android.util.Log;

import com.medikok.frontend.model.DrugInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServerConnector {
    public interface ServerResponseListener {
        void onSuccess(List<DrugInfo> responseData);
        void onFailure(String errorMessage);
    }

    private static final String SERVER_URL = "http://192.168.123.81:8080/mysql-request/drug-info-list/get";

    public static void connectToServer(ServerResponseListener listener) {
        new AsyncTask<Void, Void, List<DrugInfo>>() {
            @Override
            protected List<DrugInfo> doInBackground(Void... voids) {
                List<DrugInfo> responseData = new ArrayList<>();
                try {
                    URL url = new URL(SERVER_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        // 서버 응답을 처리하여 DrugInfo 객체로 변환
                        responseData = processResponse(response.toString());
                    } else {
                        listener.onFailure("Server returned non-OK response: " + responseCode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFailure("Failed to connect to server.");
                }
                return responseData;
            }

            @Override
            protected void onPostExecute(List<DrugInfo> responseData) {
                super.onPostExecute(responseData);
                if (!responseData.isEmpty()) {
                    listener.onSuccess(responseData);
                    // 추가된 부분: 로그에 DrugInfo 객체 정보 출력
                    for (DrugInfo drugInfo : responseData) {
                        Log.d("ServerConnector", "Item Name: " + drugInfo.getItemName());
                        Log.d("ServerConnector", "Effects: " + drugInfo.getEfcyQesitm());
                        Log.d("ServerConnector", "Usage Method: " + drugInfo.getUseMethodQesitm());
                        Log.d("ServerConnector", "Precautions Before Use: " + drugInfo.getAtpnWarnQesitm());
                        Log.d("ServerConnector", "Precautions: " + drugInfo.getAtpnQesitm());
                        Log.d("ServerConnector", "Interactions: " + drugInfo.getIntrcQesitm());
                        Log.d("ServerConnector", "Storage Precautions: " + drugInfo.getDepositMethodQesitm());
                        Log.d("ServerConnector", "Image URL: " + drugInfo.getItemImage());
                    }
                }
            }
        }.execute();
    }

    private static List<DrugInfo> processResponse(String response) {
        List<DrugInfo> drugInfoList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                DrugInfo drugInfo = new DrugInfo();
                drugInfo.setItemName(jsonObject.getString("itemName"));
                // 나머지 필드들에 대해서도 파싱하여 설정
                JSONArray effectsArray = jsonObject.getJSONArray("efcyQesitm");
                List<String> effects = new ArrayList<>();
                for (int j = 0; j < effectsArray.length(); j++) {
                    effects.add(effectsArray.getString(j));
                }
                drugInfo.setEfcyQesitm(effects);
                drugInfo.setUseMethodQesitm(jsonObject.getString("useMethodQesitm"));
                drugInfo.setAtpnWarnQesitm(jsonObject.optString("atpnWarnQesitm", ""));
                drugInfo.setAtpnQesitm(jsonObject.optString("atpnQesitm", ""));
                drugInfo.setIntrcQesitm(jsonObject.optString("intrcQesitm", ""));
                drugInfo.setDepositMethodQesitm(jsonObject.optString("depositMethodQesitm", ""));
                drugInfo.setItemImage(jsonObject.optString("itemImage", ""));
                drugInfoList.add(drugInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drugInfoList;
    }
}
