package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mokh.deoon.R;

public class SubscribeActivity extends AppCompatActivity {

    Button subscribe_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_activity);

        initViews();


        subscribe_button.setOnClickListener(v -> {
            String phone = "+967 777 120 755";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        });
    }

    private void initViews(){
        subscribe_button =(Button)findViewById(R.id.subscribe_activity_call_me_button);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}