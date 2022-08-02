package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;

public class AmountOfDebetesAndDebenturesForCustomerActivity extends AppCompatActivity {

    TextView total_of_debites_text_view,total_of_debenetures_text_view,adider_text_view;
    Bundle bundle;

    Database_Connection db;
    String check_activity,customer_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amount_of_debetes_and_debentures_for_customer_activity);

        db=new Database_Connection(getBaseContext());

        getDateFromBundel();

        initViews();

        if(check_activity.equalsIgnoreCase("search_for_debites")){
            getTotalOfDebites();
        }else{
            getTotalOfDebentures();
        }
    }

    private void initViews(){
        total_of_debites_text_view=(TextView) findViewById(R.id.total_of_debites_text_view);
        total_of_debenetures_text_view=(TextView) findViewById(R.id.total_of_debenetures_text_view);
        adider_text_view=(TextView) findViewById(R.id.adider_text_view);
    }

    private void getDateFromBundel(){
        bundle=getIntent().getExtras();
        check_activity=bundle.getString("activity");
        customer_name=bundle.getString("customer_name");

    }

    private void getTotalOfDebites(){
        int sum_of_deserved_amount=db.getSumOfDebits(customer_name);
        int sum_of_debentures=db.getSumOfDebenture(customer_name);
        int sum_of_abider=sum_of_deserved_amount-sum_of_debentures;

        total_of_debites_text_view.setText("إجمالي الديون" + "\t \t "+String.valueOf(sum_of_deserved_amount)+" ريال ");
        total_of_debenetures_text_view.setText("إجمالي المدفوعات" + "\t \t "+String.valueOf(sum_of_debentures)+" ريال ");
        adider_text_view.setText("المتبقي" + "\t \t \t "+String.valueOf(sum_of_abider)+" ريال ");
    }

    private void getTotalOfDebentures(){
        int sum_of_debentures=db.getSumOfDebenture(customer_name);
        total_of_debenetures_text_view.setText("إجمالي المدفوعات" + "\t \t \t "+String.valueOf(sum_of_debentures)+" ريال ");
        total_of_debites_text_view.setVisibility(View.GONE);
        adider_text_view.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}