package com.medikok.frontend.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.medikok.frontend.R;
import com.medikok.frontend.activity.DrugDetailActivity;
import com.medikok.frontend.model.DrugInfo;
import com.medikok.frontend.util.AddSchedule;
import com.medikok.frontend.util.ServerConnector;
import com.medikok.frontend.util.MakePillCard;
import com.medikok.frontend.util.CardsFromPreferences;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlarmFragment extends Fragment {
    List<String> drugNameList = new ArrayList<>();
    List<DrugInfo> drugInfoList = new ArrayList<>(); // 약에 대한 정보들과 정보 요청 메소드들이 담긴 리스트 선언
    String selectedItem;
    private LinearLayout alarmContainer;
    private LinearLayout cardContainer;
    private TextView noAlarmsTextView; // 추가된 TextView 참조

    public AlarmFragment() {
        // Required empty public constructor
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ServerConnector serverConnector;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        // 알람 표시 동적 구현
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        alarmContainer = view.findViewById(R.id.alarmContainer);
        cardContainer = view.findViewById(R.id.layout1);
        noAlarmsTextView = view.findViewById(R.id.noAlarmsTextView);
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        FloatingActionButton btn_test = view.findViewById(R.id.btn_test);

        // 플로팅 버튼 클릭 이벤트 처리
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDayPickerDialog();
            }
        });

        // Test 버튼 클릭 이벤트 처리
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다른 화면으로 전환하기 위한 Intent 생성
                Intent intent = new Intent(getActivity(), AddSchedule.class);
                startActivity(intent); // Intent를 사용하여 새로운 화면으로 전환
            }
        });

        ServerConnector.connectToServer(new ServerConnector.ServerResponseListener() {
            @Override
            public void onSuccess(List<DrugInfo> responseData) {

                // drugInfoList에 서버로부터 받은 데이터 저장
                drugInfoList = responseData;

                for (DrugInfo drugInfo : responseData) {
                    drugNameList.add(drugInfo.getItemName());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the error
            }
        });

        loadAlarmsFromPreferences();  // Load alarms from SharedPreferences when the fragment is created
        CardsFromPreferences.loadCardsFromPreferences(cardContainer, getContext());
        updateNoAlarmsTextView();


        return view;
    }

    // 플로팅 버튼을 클릭 했을 경우
    private void showTimeDayPickerDialog() {
        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_time_day_picker, null);

        // 약 선택 드롭 다운 어댑터 설정
        Spinner dropDownList = dialogView.findViewById(R.id.dropDownList);
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                drugNameList
        );
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownList.setAdapter(dropDownAdapter);

        dropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때의 동작 (필요 시 구현)
            }
        });

        builder.setView(dialogView);

        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);

        CheckBox[] dayCheckBoxes = new CheckBox[] {
                dialogView.findViewById(R.id.checkMonday),
                dialogView.findViewById(R.id.checkTuesday),
                dialogView.findViewById(R.id.checkWednesday),
                dialogView.findViewById(R.id.checkThursday),
                dialogView.findViewById(R.id.checkFriday),
                dialogView.findViewById(R.id.checkSaturday),
                dialogView.findViewById(R.id.checkSunday)
        };

        // 확인 버튼을 누를 경우
        builder.setPositiveButton("확인", (dialog, which) -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout newAlarmCard = (LinearLayout) inflater.inflate(R.layout.alarm, null);

            TextView alarmTime = newAlarmCard.findViewById(R.id.alarmPillDateTime);

            // 알람 카드에 선택한 약 이름 설정
            TextView alarmPillName = newAlarmCard.findViewById(R.id.alarmPillName);

            alarmTime.setText(String.format("%02d:%02d", hour, minute));
            alarmTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);

            alarmPillName.setText(selectedItem);

            TextView[] dayTextViews = new TextView[] {
                    newAlarmCard.findViewById(R.id.alarmPillDateMon),
                    newAlarmCard.findViewById(R.id.alarmPillDateTue),
                    newAlarmCard.findViewById(R.id.alarmPillDateWed),
                    newAlarmCard.findViewById(R.id.alarmPillDateThu),
                    newAlarmCard.findViewById(R.id.alarmPillDateFri),
                    newAlarmCard.findViewById(R.id.alarmPillDateSat),
                    newAlarmCard.findViewById(R.id.alarmPillDateSun)
            };

            TextView everyDayTextView = newAlarmCard.findViewById(R.id.alarmPillEveryday);

            boolean allChecked = true;
            for (CheckBox checkBox : dayCheckBoxes) {
                if (!checkBox.isChecked()) {
                    allChecked = false;
                    break;
                }
            }

            if (allChecked) {
                for (TextView dayTextView : dayTextViews) {
                    updateDayTextView(dayTextView, true);
                }
                everyDayTextView.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < dayCheckBoxes.length; i++) {
                    updateDayTextView(dayTextViews[i], dayCheckBoxes[i].isChecked());
                }
                everyDayTextView.setVisibility(View.INVISIBLE);
            }

            alarmContainer.addView(newAlarmCard);

            addAlarmCardEventListeners(newAlarmCard);
            saveAlarmToPreferences(newAlarmCard);  // Save the new alarm to SharedPreferences
            updateNoAlarmsTextView();

            // 서버의 약 정보 리스트에서 추가된 알람의 약 이름과 같은 객체를 찾아 카드 뷰 생성
            for (DrugInfo drugInfo : drugInfoList) {
                if (drugInfo.getItemName().equals(alarmPillName.getText().toString())) {
                    String name = drugInfo.getItemName();
                    String effect = drugInfo.getEfcyQesitm();
                    String method = drugInfo.getUseMethodQesitm();
                    String imageUrl = drugInfo.getItemImage();

                    LinearLayout pillCard = MakePillCard.makePillCard(getContext(), imageUrl, name, method, effect);
                    cardContainer.addView(pillCard);

                    CardsFromPreferences.saveCardToPreferences(pillCard, alarmTime.toString(), getContext());
                }
            }
        });

        builder.setNegativeButton("취소", null);
        builder.create().show();
    }

    private void showEditTimeDayPickerDialog(LinearLayout alarmCard) {
        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_time_day_picker, null);

        // 약 선택 드롭 다운 어댑터 설정
        Spinner dropDownList = dialogView.findViewById(R.id.dropDownList);
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                drugNameList
        );
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownList.setAdapter(dropDownAdapter);

        dropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때의 동작 (필요 시 구현)
            }
        });

        builder.setView(dialogView);

        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);

        CheckBox[] dayCheckBoxes = new CheckBox[] {
                dialogView.findViewById(R.id.checkMonday),
                dialogView.findViewById(R.id.checkTuesday),
                dialogView.findViewById(R.id.checkWednesday),
                dialogView.findViewById(R.id.checkThursday),
                dialogView.findViewById(R.id.checkFriday),
                dialogView.findViewById(R.id.checkSaturday),
                dialogView.findViewById(R.id.checkSunday)
        };

        TextView alarmTime = alarmCard.findViewById(R.id.alarmPillDateTime);

        // 알람 카드에 선택한 약 이름 설정
        TextView alarmPillName = alarmCard.findViewById(R.id.alarmPillName);

        String[] timeParts = alarmTime.getText().toString().split(":");
        int currentHour = Integer.parseInt(timeParts[0]);
        int currentMinute = Integer.parseInt(timeParts[1]);
        timePicker.setHour(currentHour);
        timePicker.setMinute(currentMinute);

        TextView[] dayTextViews = new TextView[] {
                alarmCard.findViewById(R.id.alarmPillDateMon),
                alarmCard.findViewById(R.id.alarmPillDateTue),
                alarmCard.findViewById(R.id.alarmPillDateWed),
                alarmCard.findViewById(R.id.alarmPillDateThu),
                alarmCard.findViewById(R.id.alarmPillDateFri),
                alarmCard.findViewById(R.id.alarmPillDateSat),
                alarmCard.findViewById(R.id.alarmPillDateSun)
        };

        TextView everyDayTextView = alarmCard.findViewById(R.id.alarmPillEveryday);

        boolean allChecked = true;
        for (int i = 0; i < dayTextViews.length; i++) {
            boolean isChecked = dayTextViews[i].getVisibility() == View.VISIBLE && dayTextViews[i].getTypeface() != null && dayTextViews[i].getTypeface().isBold();
            dayCheckBoxes[i].setChecked(isChecked);
            if (!isChecked) {
                allChecked = false;
            }
        }

        if (everyDayTextView.getVisibility() == View.VISIBLE) {
            allChecked = true;
            for (CheckBox checkBox : dayCheckBoxes) {
                checkBox.setChecked(true);
            }
        }

        builder.setPositiveButton("확인", (dialog, which) -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            alarmTime.setText(String.format("%02d:%02d", hour, minute));
            alarmPillName.setText(selectedItem);

            boolean allCheckedAfterEdit = true;
            for (CheckBox checkBox : dayCheckBoxes) {
                if (!checkBox.isChecked()) {
                    allCheckedAfterEdit = false;
                    break;
                }
            }

            if (allCheckedAfterEdit) {
                for (TextView dayTextView : dayTextViews) {
                    updateDayTextView(dayTextView, true);
                }
                everyDayTextView.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < dayCheckBoxes.length; i++) {
                    updateDayTextView(dayTextViews[i], dayCheckBoxes[i].isChecked());
                    dayTextViews[i].setVisibility(View.VISIBLE);  // 텍스트뷰를 다시 보이도록 설정
                }
                everyDayTextView.setVisibility(View.INVISIBLE);
            }

            saveAlarmToPreferences(alarmCard);  // Save the updated alarm to SharedPreferences
        });

        builder.setNegativeButton("취소", null);
        builder.create().show();
    }

    private void updateDayTextView(TextView textView, boolean isChecked) {
        if (isChecked) {
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        } else {
            textView.setTypeface(null, Typeface.NORMAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
    }

    // 알람카드 생성 부분에 수정 버튼 이벤트 추가
    private void addAlarmCardEventListeners(LinearLayout alarmCard) {
        Button deleteButton = alarmCard.findViewById(R.id.alarmDelete);
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(alarmCard));

        Button modifyButton = alarmCard.findViewById(R.id.alarmModify);
        modifyButton.setOnClickListener(v -> showEditTimeDayPickerDialog(alarmCard));

        Switch alarmSwitch = alarmCard.findViewById(R.id.alarmPillSwitch);
        alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveAlarmToPreferences(alarmCard);
        });
    }

    private void showDeleteConfirmationDialog(final View alarmCard) {
        new AlertDialog.Builder(getContext())
                .setMessage("삭제 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alarmContainer.removeView(alarmCard);

                        TextView alarmDrugName = alarmCard.findViewById(R.id.alarmPillName);
                        String alarmDrugNameText = alarmDrugName.getText().toString();
                        removeAlarmFromPreferences((LinearLayout) alarmCard); // LinearLayout로 캐스팅

                        // layout1에 추가된 카드뷰들을 반복문으로 순회
                        for (int i = 0; i < cardContainer.getChildCount(); i++) {
                            View cardView = cardContainer.getChildAt(i);

                            if (cardView instanceof LinearLayout) {
                                LinearLayout pillCard = (LinearLayout) cardView;
                                TextView drugName = pillCard.findViewById(R.id.today_medicine_name);

                                if (drugName != null) {
                                    String drugNameText = drugName.getText().toString();
                                    if (drugNameText.equals(alarmDrugNameText)) {
                                        cardContainer.removeView(pillCard);
                                        CardsFromPreferences.removeCardFromPreferences(pillCard, getContext());
                                        updateNoAlarmsTextView();
                                    }
                                }
                            }
                        }
                    }

                })
                .setNegativeButton("아니오", null)
                .show();
    }

    private void saveAlarmToPreferences(LinearLayout alarmCard) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        TextView alarmTime = alarmCard.findViewById(R.id.alarmPillDateTime);
        TextView alarmPillName = alarmCard.findViewById(R.id.alarmPillName);
        Switch alarmSwitch = alarmCard.findViewById(R.id.alarmPillSwitch);

        String key = alarmTime.getText().toString();
        editor.putString(key + "_name", alarmPillName.getText().toString());
        editor.putBoolean(key + "_switch", alarmSwitch.isChecked());

        TextView[] dayTextViews = new TextView[] {
                alarmCard.findViewById(R.id.alarmPillDateMon),
                alarmCard.findViewById(R.id.alarmPillDateTue),
                alarmCard.findViewById(R.id.alarmPillDateWed),
                alarmCard.findViewById(R.id.alarmPillDateThu),
                alarmCard.findViewById(R.id.alarmPillDateFri),
                alarmCard.findViewById(R.id.alarmPillDateSat),
                alarmCard.findViewById(R.id.alarmPillDateSun)
        };

        for (int i = 0; i < dayTextViews.length; i++) {
            editor.putBoolean(key + "_day" + i, dayTextViews[i].getTypeface() != null && dayTextViews[i].getTypeface().isBold());
        }

        editor.apply();
    }

    private void removeAlarmFromPreferences(LinearLayout alarmCard) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        TextView alarmTime = alarmCard.findViewById(R.id.alarmPillDateTime);
        String key = alarmTime.getText().toString();

        editor.remove(key + "_name");
        editor.remove(key + "_switch");

        TextView[] dayTextViews = new TextView[] {
                alarmCard.findViewById(R.id.alarmPillDateMon),
                alarmCard.findViewById(R.id.alarmPillDateTue),
                alarmCard.findViewById(R.id.alarmPillDateWed),
                alarmCard.findViewById(R.id.alarmPillDateThu),
                alarmCard.findViewById(R.id.alarmPillDateFri),
                alarmCard.findViewById(R.id.alarmPillDateSat),
                alarmCard.findViewById(R.id.alarmPillDateSun)
        };

        for (int i = 0; i < dayTextViews.length; i++) {
            editor.remove(key + "_day" + i);
        }

        editor.apply();
    }

    private void loadAlarmsFromPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        alarmContainer.removeAllViews(); // 기존 알람을 모두 제거

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.endsWith("_name")) {
                String timeKey = key.replace("_name", "");
                String name = (String) entry.getValue();
                boolean switchState = sharedPreferences.getBoolean(timeKey + "_switch", false);

                LinearLayout newAlarmCard = (LinearLayout) inflater.inflate(R.layout.alarm, null);

                TextView alarmTime = newAlarmCard.findViewById(R.id.alarmPillDateTime);
                alarmTime.setText(timeKey);
                alarmTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);

                TextView alarmPillName = newAlarmCard.findViewById(R.id.alarmPillName);
                alarmPillName.setText(name);

                Switch alarmSwitch = newAlarmCard.findViewById(R.id.alarmPillSwitch);
                alarmSwitch.setChecked(switchState);

                TextView[] dayTextViews = new TextView[] {
                        newAlarmCard.findViewById(R.id.alarmPillDateMon),
                        newAlarmCard.findViewById(R.id.alarmPillDateTue),
                        newAlarmCard.findViewById(R.id.alarmPillDateWed),
                        newAlarmCard.findViewById(R.id.alarmPillDateThu),
                        newAlarmCard.findViewById(R.id.alarmPillDateFri),
                        newAlarmCard.findViewById(R.id.alarmPillDateSat),
                        newAlarmCard.findViewById(R.id.alarmPillDateSun)
                };

                boolean allChecked = true;
                for (int i = 0; i < dayTextViews.length; i++) {
                    boolean isChecked = sharedPreferences.getBoolean(timeKey + "_day" + i, false);
                    updateDayTextView(dayTextViews[i], isChecked);
                    if (!isChecked) {
                        allChecked = false;
                    }
                }

                TextView everyDayTextView = newAlarmCard.findViewById(R.id.alarmPillEveryday);
                if (allChecked) {
                    everyDayTextView.setVisibility(View.VISIBLE);
                } else {
                    everyDayTextView.setVisibility(View.INVISIBLE);
                }

                alarmContainer.addView(newAlarmCard);
                addAlarmCardEventListeners(newAlarmCard);
            }
        }
    }

    private void updateNoAlarmsTextView() {
        if (alarmContainer.getChildCount() == 0) {
            noAlarmsTextView.setVisibility(View.VISIBLE);
        } else {
            noAlarmsTextView.setVisibility(View.GONE);
        }
    }

}