    package com.medikok.frontend.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.medikok.frontend.AlarmFragment;
import com.medikok.frontend.R;
import com.medikok.frontend.SearchFragment;
import com.medikok.frontend.databinding.ActivityNaviBinding;

public class NaviActivity extends AppCompatActivity {

    private static final String TAG_SEARCH = "searchFragment";
    private static final String TAG_ALARM = "alarmFragment";
    private ActivityNaviBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNaviBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_navi);

        setFragment(TAG_ALARM, new AlarmFragment());
        binding.navigationView.setSelectedItemId(R.id.alarmFragment);

        binding.navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.searchFragment) {
                setFragment(TAG_SEARCH, new SearchFragment());
            } else {
                setFragment(TAG_ALARM, new AlarmFragment());
            }
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected void setFragment(String tag, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.mainFrameLayout, fragment, tag);
        }

        Fragment alarm = fragmentManager.findFragmentByTag(TAG_ALARM);
        Fragment search = fragmentManager.findFragmentByTag(TAG_SEARCH);

        if (alarm != null) {
            fragmentTransaction.hide(alarm);
        }

        if (search != null) {
            fragmentTransaction.hide(search);
        }

        if (tag.equals(TAG_SEARCH)) {
            if (search != null) {
                fragmentTransaction.show(search);
            }
        } else if (tag.equals(TAG_ALARM)) {
            if (alarm != null) {
                fragmentTransaction.show(alarm);
            }

        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}