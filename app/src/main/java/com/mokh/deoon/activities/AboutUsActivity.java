package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mokh.deoon.BuildConfig;
import com.mokh.deoon.R;

public class AboutUsActivity extends AppCompatActivity {

    ImageView phone_image_view,facebook_image_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_activity);

        initViews();

        phone_image_view.setOnClickListener(v -> {
            String phone = "+967 777 120 755";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);

        });

        facebook_image_view.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/www5wwww"));
                startActivity(intent);
            } catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/www5wwww")));
            }
        });



    }

    private void initViews(){
        phone_image_view=(ImageView) findViewById(R.id.about_all_activity_phone_image_view);
        facebook_image_view=(ImageView) findViewById(R.id.about_all_activity_facebook_image_view);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}