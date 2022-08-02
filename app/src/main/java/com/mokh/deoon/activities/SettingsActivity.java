package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

public class SettingsActivity extends AppCompatActivity {

    Button creat_pass_word_button, edit_password_button,delete_password_button;

    LinearLayout create_password_linear_layout,edit_password_linear_layout,delete_password_linear_layout;

    String ACTIVITY_STATUTE;

    Intent delete_password_intent;
    Bundle delete_password_bundle;

    int REQUEST_CODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ACTIVITY_STATUTE="onCreate";

        REQUEST_CODE=1;

        initViews();

        if(Shared_Helper.getkey(getBaseContext(),"internal_pass").equalsIgnoreCase("")){
            edit_password_linear_layout.setVisibility(View.GONE);
            delete_password_linear_layout.setVisibility(View.GONE);
        }else{
            create_password_linear_layout.setVisibility(View.GONE);
        }

        creat_pass_word_button.setOnClickListener(v -> {
                startActivity(new Intent(getBaseContext(), CreateInternalPasswordActivity.class));
         });

        edit_password_button.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), EditInternalPassActivity.class));

        });

        delete_password_button.setOnClickListener(v -> {

            delete_password_intent=new Intent(getBaseContext(), CheckPasswordActivity.class);
            delete_password_bundle=new Bundle();

            delete_password_bundle.putString("activty","delete_password");
            delete_password_intent.putExtras(delete_password_bundle);
            startActivityForResult(delete_password_intent,REQUEST_CODE);

        });
    }


    private void initViews(){

        creat_pass_word_button =(Button) findViewById(R.id.settings_activity_create_internal_password_button);
        edit_password_button =(Button) findViewById(R.id.settings_activity_edit_internal_password_button);
        delete_password_button =(Button) findViewById(R.id.settings_activity_delete_internal_password_button);

        create_password_linear_layout=(LinearLayout) findViewById(R.id.settings_activity_create_internal_password_linearlayout);
        edit_password_linear_layout=(LinearLayout) findViewById(R.id.settings_activity_edit_internal_password_linear_layout);
        delete_password_linear_layout=(LinearLayout) findViewById(R.id.settings_activity_delete_internal_password_linear_layout);
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
    protected void onPause() {
        super.onPause();
        ACTIVITY_STATUTE="onPause";
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ACTIVITY_STATUTE.equalsIgnoreCase("onPause")){
            recreate();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}