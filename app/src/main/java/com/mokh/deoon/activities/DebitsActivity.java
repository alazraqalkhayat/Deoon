package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.mokh.deoon.R;

public class DebitsActivity extends AppCompatActivity {


    Button add_new_debit_button, search_for_debit_button,amount_of_all_debites_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debits__activity);

        initViews();

        add_new_debit_button.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), AddNewDebiteActivity.class));
        });


        search_for_debit_button.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), SearchForDebitsActivity.class));
        });

        amount_of_all_debites_button.setOnClickListener(v -> {
            Intent intent=new Intent(getBaseContext(),AmountOfAllDebitsAndDebenturesActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("activty","debites_activity");
            intent.putExtras(bundle);
            startActivity(intent);

        });

    }


    private void initViews(){

        add_new_debit_button =(Button) findViewById(R.id.debits_activity_new_debite_button);
        search_for_debit_button =(Button) findViewById(R.id.debits_activity_search_for_debite_button);
        amount_of_all_debites_button=(Button) findViewById(R.id.debits_activity_amount_of_all_debites_button);

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