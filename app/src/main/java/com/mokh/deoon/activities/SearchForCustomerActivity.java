package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SearchForCustomerActivity extends AppCompatActivity {

    AutoCompleteTextView name_of_customer;
    EditText phone_of_customer_edit_text;
    TextView tap_for_calling_text_view;
    ImageView search_image_view;
    LinearLayout phone_number_linear_layouy;

    Map<Integer,String> names_resulte;
    Database_Connection db;
    List<String> names_values_list;
    ArrayAdapter names_adapter;

    String check_customer_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_for_customer_activity);

        db=new Database_Connection(getBaseContext());
//        names_resulte=new HashMap<>();


        initViews();





        search_image_view.setOnClickListener(v -> {
                    if(name_of_customer.getText().toString().isEmpty()){
                        name_of_customer.setError("يلوم تعبئة هذا الحقل");
                    }else{
                        check_customer_name=db.getCustomerName(name_of_customer.getText().toString());
                        if(check_customer_name.isEmpty()){
                            Toasty.custom(this,"عذرا.. هذا العميل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                            phone_number_linear_layouy.setVisibility(View.GONE);
                            tap_for_calling_text_view.setVisibility(View.GONE);
                        }else{
                            getCustomernameAndPhoneNumber(name_of_customer.getText().toString());
                        }


                    }
        });


        tap_for_calling_text_view.setOnClickListener(v -> {
//                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phone_of_customer_edit_text.getText().toString(), null)));
                String phone = phone_of_customer_edit_text.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

        });

    }



    private void initViews(){
        name_of_customer=(AutoCompleteTextView)findViewById(R.id.search_for_customer_activity_customer_name_edit_text);
        getAllCustomers(name_of_customer);

        phone_number_linear_layouy=(LinearLayout)findViewById(R.id.search_for_customer_activity_phone_number_linear_layout);
        phone_number_linear_layouy.setVisibility(View.GONE);
        phone_of_customer_edit_text=(EditText) findViewById(R.id.search_for_customer_activity_phone_number_edit_text);

        tap_for_calling_text_view=(TextView)findViewById(R.id.search_for_customer_tap_on_number_to_call_text_view);
        tap_for_calling_text_view.setVisibility(View.GONE);

        search_image_view=(ImageView) findViewById(R.id.search_for_customer_activity_search_image_view);

    }

    private void getAllCustomers(AutoCompleteTextView textView){

        names_resulte=db.getAllCustomers();
        names_values_list=new ArrayList<String>(names_resulte.values());
        names_adapter=new ArrayAdapter(this,android.R.layout.select_dialog_item,names_values_list);
        textView.setAdapter(names_adapter);

    }

    private void getCustomernameAndPhoneNumber(String nameOfCustomer) {
        String [] get_customer_name_and_phone_number=new String[2];
        get_customer_name_and_phone_number=db.getLimiteduCustomer(nameOfCustomer);

        phone_number_linear_layouy.setVisibility(View.VISIBLE);
        tap_for_calling_text_view.setVisibility(View.VISIBLE);
        if(get_customer_name_and_phone_number[1].equalsIgnoreCase("")){
            phone_of_customer_edit_text.setHint("لا يوجد رقم هاتف لهذا العميل ..!");
            tap_for_calling_text_view.setVisibility(View.GONE);

        }else{
            phone_of_customer_edit_text.setText(get_customer_name_and_phone_number[1]);

        }
        phone_of_customer_edit_text.setEnabled(false);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}