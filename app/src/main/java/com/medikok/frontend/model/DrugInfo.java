package com.medikok.frontend.model;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DrugInfo {
    // 약 이름
    private String itemName;
    // 효능
    private List<String> efcyQesitm;
    // 복용법
    private String useMethodQesitm;
    // 복용 전 주의사항
    private String atpnWarnQesitm;
    // 주의사항
    private String atpnQesitm;
    // 다른 약물과의 상호작용
    private String intrcQesitm;
    // 보관 주의사항
    private String depositMethodQesitm;
    // 이미지 주소
    private String itemImage;


    public DrugInfo() {
        // 기본 생성자
    }
    public DrugInfo(String itemName, List<String> efcyQesitm, String useMethodQesitm, String atpnWarnQesitm,
                    String atpnQesitm, String intrcQesitm, String depositMethodQesitm, String itemImage) {
        this.itemName = itemName;
        this.efcyQesitm = efcyQesitm;
        this.useMethodQesitm = useMethodQesitm;
        this.atpnWarnQesitm = atpnWarnQesitm;
        this.atpnQesitm = atpnQesitm;
        this.intrcQesitm = intrcQesitm;
        this.depositMethodQesitm = depositMethodQesitm;
        this.itemImage = itemImage;
    }

    // DrugInfo 객체를 JSON 문자열로 변환하는 메서드
    public static String convertToJson(List<DrugInfo> drugInfoList) {
        Gson gson = new Gson();
        return gson.toJson(drugInfoList);
    }

    // Getter와 Setter 메서드들
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getEfcyQesitm() {
        return String.join(",", efcyQesitm);
    }

    public void setEfcyQesitm(List<String> efcyQesitm) {
        this.efcyQesitm = efcyQesitm;
    }

    public String getUseMethodQesitm() {
        return useMethodQesitm;
    }

    public void setUseMethodQesitm(String useMethodQesitm) {
        this.useMethodQesitm = useMethodQesitm;
    }

    public String getAtpnWarnQesitm() {
        return atpnWarnQesitm;
    }

    public void setAtpnWarnQesitm(String atpnWarnQesitm) {
        this.atpnWarnQesitm = atpnWarnQesitm;
    }

    public String getAtpnQesitm() {
        return atpnQesitm;
    }

    public void setAtpnQesitm(String atpnQesitm) {
        this.atpnQesitm = atpnQesitm;
    }

    public String getIntrcQesitm() {
        return intrcQesitm;
    }

    public void setIntrcQesitm(String intrcQesitm) {
        this.intrcQesitm = intrcQesitm;
    }

    public String getDepositMethodQesitm() {
        return depositMethodQesitm;
    }

    public void setDepositMethodQesitm(String depositMethodQesitm) {
        this.depositMethodQesitm = depositMethodQesitm;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    // JSON 파싱 메서드
    public static List<DrugInfo> parseJson(String jsonString) {
        List<DrugInfo> drugInfoList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String itemName = jsonObject.getString("itemName");
                JSONArray efcyQesitmArray = jsonObject.getJSONArray("efcyQesitm");
                List<String> efcyQesitm = new ArrayList<>();
                for (int j = 0; j < efcyQesitmArray.length(); j++) {
                    efcyQesitm.add(efcyQesitmArray.getString(j));
                }
                String useMethodQesitm = jsonObject.getString("useMethodQesitm");
                String atpnWarnQesitm = jsonObject.optString("atpnWarnQesitm", "");
                String atpnQesitm = jsonObject.optString("atpnQesitm", "");
                String intrcQesitm = jsonObject.optString("intrcQesitm", "");
                String depositMethodQesitm = jsonObject.optString("depositMethodQesitm", "");
                String itemImage = jsonObject.optString("itemImage", "");

                DrugInfo drugInfo = new DrugInfo(itemName, efcyQesitm, useMethodQesitm, atpnWarnQesitm, atpnQesitm, intrcQesitm, depositMethodQesitm, itemImage);
                drugInfoList.add(drugInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drugInfoList;
    }
}
