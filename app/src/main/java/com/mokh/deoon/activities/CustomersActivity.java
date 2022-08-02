package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.mokh.deoon.R;

public class CustomersActivity extends AppCompatActivity {

    Toolbar emp_tool_bar;

    Button add_emp_button, search_for_emp_button, all_emps_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_activity);

        initViews();



        add_emp_button.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),AddNewCustomerActivity.class)));

        search_for_emp_button.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), SearchForCustomerActivity.class)));

        all_emps_button.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), AllCustomersActivity.class)));
    }

    private void initViews(){

        add_emp_button =(Button) findViewById(R.id.emp_activity_add_emp_button);
        search_for_emp_button =(Button) findViewById(R.id.emp_activity_search_for_emp_button);
        all_emps_button =(Button) findViewById(R.id.emp_activity_all_emps_button);

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