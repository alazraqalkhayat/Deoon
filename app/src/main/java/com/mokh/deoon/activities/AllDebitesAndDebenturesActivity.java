package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.adapters.Debentures_adapter;
import com.mokh.deoon.models.Debentures_model;
import com.mokh.deoon.adapters.Debits_adapter;
import com.mokh.deoon.models.Depits_model;
import com.mokh.deoon.R;

import java.util.ArrayList;

public class AllDebitesAndDebenturesActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Depits_model> depits_items;
    Debits_adapter debits_adapter;

    ArrayList<Debentures_model> debentures_items;
    Debentures_adapter debentures_adapter;


    Database_Connection db;

    Bundle bundle;
    String check_activity,customer_name;

    String ACTIVITY_STATUTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_debites_and_debentures_activity);
        ACTIVITY_STATUTE="onCreate";

        getDateFromBundel();

        db=new Database_Connection(getBaseContext());

       initViews();

       if(check_activity.equalsIgnoreCase("search_for_debites")){
           getAllDebites(customer_name);
           getSupportActionBar().setTitle("كل الديون");
       }else{
           getAllDebenetures(customer_name);
           getSupportActionBar().setTitle("كل السندات");

       }
    }

    private void getDateFromBundel(){
        bundle=getIntent().getExtras();
        check_activity=bundle.getString("activity");
        customer_name=bundle.getString("customer_name");
    }

    private void initViews(){
        recyclerView=(RecyclerView) findViewById(R.id.all_debites_and_debentures_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }


    private void getAllDebites(String customer_name){
        depits_items=db.getAllDebits(customer_name);
        debits_adapter=new Debits_adapter(AllDebitesAndDebenturesActivity.this,depits_items);
        recyclerView.setAdapter(debits_adapter);

        /*if (debits_adapter.getItemCount()==0){
            Toast.makeText(getBaseContext(), "null", Toast.LENGTH_SHORT).show();
        }*/

    }

    private void getAllDebenetures(String customer_name){
        debentures_items=db.getAllDebenture(customer_name);
        debentures_adapter=new Debentures_adapter(AllDebitesAndDebenturesActivity.this,debentures_items);
        recyclerView.setAdapter(debentures_adapter);

      /*  if (debentures_adapter.getItemCount()==0){
            Toast.makeText(getBaseContext(), "null", Toast.LENGTH_SHORT).show();
        }*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_home_and_back_meu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.all_home_and_back_menu_home){
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }else if(id==R.id.all_home_and_back_menu_back){
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