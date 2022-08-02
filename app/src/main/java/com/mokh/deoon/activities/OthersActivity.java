package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mokh.deoon.BuildConfig;
import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

import java.io.File;

public class OthersActivity extends AppCompatActivity {

    Button about_us_button, subscribe_button,share_button,evaluate_button,login_button;
    TextView version;
    LinearLayout login_button_linear_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_activity);

        initViews();

        if(!Shared_Helper.getkey(this,"user_name").equalsIgnoreCase("free")){
            login_button_linear_layout.setVisibility(View.GONE);
        }
        
        about_us_button.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(),AboutUsActivity.class));
        });

        subscribe_button.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(),SubscribeActivity.class));
        });

        share_button.setOnClickListener(v -> {
            shareApp();
        });

        evaluate_button.setOnClickListener(v -> {
               evaluationAppOnAppStore();
        });

        login_button.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(),Login_activity.class));
            Shared_Helper.putKey(getBaseContext(),"user_name","");

        });
    }

    private void initViews(){

        about_us_button =(Button) findViewById(R.id.others_activity_about_us_button);
        subscribe_button =(Button) findViewById(R.id.others_activity_subscribe_button);
        share_button =(Button) findViewById(R.id.others_activity_share_button);
        evaluate_button =(Button) findViewById(R.id.others_activity_evaluate_button);
        login_button=(Button)findViewById(R.id.others_activity_login_button);
        login_button_linear_layout=(LinearLayout)findViewById(R.id.others_activity_login_button_linearLayout);
        version=(TextView) findViewById(R.id.about_us_activity_version_number_text_view);

        version.setText(" رقم الإصدار  "+ BuildConfig.VERSION_NAME);
    }

    private void evaluationAppOnAppStore(){
        try {
            Intent open_play_store=new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName()));
            startActivity(open_play_store);

        }catch (ActivityNotFoundException e){
            Intent open_play_store_with_error=new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store.apps/details?id="+getPackageName()));
            startActivity(open_play_store_with_error);
        }

    }

    private void shareApp(){
        try {
            ApplicationInfo apk = getApplicationContext().getApplicationInfo();
            String apkpath = apk.sourceDir;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("application/void.android.package.archive");
            i.putExtra(Intent.EXTRA_SUBJECT, "Year TITLE");
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
            startActivity(Intent.createChooser(i, "Share app"));
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.just_back, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if(id==R.id.just_back_menu_header_back){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}