package com.medikok.frontend.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DrugInfo {
    private String itemName;
    private List<String> effects;
    private String useMethod;

    public DrugInfo() {
        // 기본 생성자
    }

    public DrugInfo(String itemName, List<String> effects, String useMethod) {
        this.itemName = itemName;
        this.effects = effects;
        this.useMethod = useMethod;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<String> getEffects() {
        return effects;
    }

    public void setEffects(List<String> effects) {
        this.effects = effects;
    }

    public String getUseMethod() {
        return useMethod;
    }

    public void setUseMethod(String useMethod) {
        this.useMethod = useMethod;
    }

    public static List<DrugInfo> parseJson(String jsonString) {
        List<DrugInfo> drugInfoList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String itemName = jsonObject.getString("itemName");
                JSONArray effectsArray = jsonObject.getJSONArray("efcyQesitm");
                List<String> effects = new ArrayList<>();
                for (int j = 0; j < effectsArray.length(); j++) {
                    effects.add(effectsArray.getString(j));
                }
                String useMethod = jsonObject.getString("useMethodQesitm");
                DrugInfo drugInfo = new DrugInfo(itemName, effects, useMethod);
                drugInfoList.add(drugInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drugInfoList;
    }
}
