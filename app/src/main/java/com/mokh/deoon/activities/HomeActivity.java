package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mokh.deoon.R;
import com.mokh.deoon.helper.Database_Connection;

public class HomeActivity extends AppCompatActivity {

    LinearLayout employee_card_view,workers_card_view,
                 debits_card_view, debentures_card_view,
                 clear_and_rebort,data_card_view,
                 settings_card_view,others_card_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);




        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        initViews();

        employee_card_view.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), CustomersActivity.class)));

        workers_card_view.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),WorkersActivity.class)));

        debits_card_view.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),DebitsActivity.class)));

        debentures_card_view.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),DebenturesActivity.class)));

        clear_and_rebort.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),ClearAndRebortActivity.class)));

        data_card_view.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),DataActivity.class)));

        settings_card_view.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),SettingsActivity.class)));

        others_card_view.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),OthersActivity.class)));
    }


    private void initViews(){
        employee_card_view=(LinearLayout) findViewById(R.id.home_activity_employee_linear_layout);
        workers_card_view=(LinearLayout) findViewById(R.id.home_activity_workers_linear_layout);
        debits_card_view=(LinearLayout) findViewById(R.id.home_activity_debits_linear_layout);
        debentures_card_view =(LinearLayout) findViewById(R.id.home_activity_debentures_linear_layout);
        clear_and_rebort =(LinearLayout) findViewById(R.id.home_activity_clear_and_rebort_linear_layout);
        data_card_view=(LinearLayout) findViewById(R.id.home_activity_data_linear_layout);
        settings_card_view=(LinearLayout) findViewById(R.id.home_activity_settings_linear_layout);
        others_card_view=(LinearLayout) findViewById(R.id.home_activity_others_linear_layoutw);
    }
}