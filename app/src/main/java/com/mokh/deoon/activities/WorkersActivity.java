package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.mokh.deoon.R;

public class WorkersActivity extends AppCompatActivity {

    Button add_worker_button, all_workers_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workers_activity);

        initViews();

        add_worker_button.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), AddNewWorkerActivity.class)));

        all_workers_button.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), AllWorkersActivity.class)));


    }

    private void initViews(){

        add_worker_button =(Button) findViewById(R.id.workers_activity_add_worker_button);
        all_workers_button =(Button) findViewById(R.id.workers_activity_all_workers_button);
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