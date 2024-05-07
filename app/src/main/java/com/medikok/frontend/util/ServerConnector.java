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

    private static final String SERVER_URL = "http://172.30.2.234:8080/mysql-request/drug-info-list/get";

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
                // 이하 원하는 필드에 대해 파싱하여 설정
                JSONArray effectsArray = jsonObject.getJSONArray("efcyQesitm");
                List<String> effects = new ArrayList<>();
                for (int j = 0; j < effectsArray.length(); j++) {
                    effects.add(effectsArray.getString(j));
                }
                drugInfo.setEffects(effects);
                drugInfo.setUseMethod(jsonObject.getString("useMethodQesitm"));
                drugInfoList.add(drugInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drugInfoList;
    }
}
