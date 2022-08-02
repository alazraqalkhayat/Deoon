package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;

public class AmountOfAllDebitsAndDebenturesActivity extends AppCompatActivity {

    Database_Connection db;
    EditText amount_edit_text;
    Bundle bundle;
    String check_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amount_of_all_debits_and_debentures_activity);
        db=new Database_Connection(getBaseContext());

        amount_edit_text=(EditText) findViewById(R.id.amout_of_all_debites_edit_text);
        bundle=getIntent().getExtras();
        check_activity=bundle.getString("activty");
        if(check_activity.equalsIgnoreCase("debites_activity")){
            amount_edit_text.setText(String.valueOf(db.getSumOfAllDebits())+" ريال ");
            amount_edit_text.setTextColor(getResources().getColor(R.color.red));
        }else{
            amount_edit_text.setText(String.valueOf(db.getSumOfAllDebenture())+" ريال ");
            amount_edit_text.setTextColor(getResources().getColor(R.color.light_green));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}