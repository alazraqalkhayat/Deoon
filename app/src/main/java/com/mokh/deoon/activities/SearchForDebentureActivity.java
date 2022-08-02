package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;
import com.mokh.deoon.models.SearchMethodeAndInternalPasswordModel;
import com.mokh.deoon.adapters.SearchMethodeAndInternalPasswordItemsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SearchForDebentureActivity extends AppCompatActivity {


    Spinner search_methode_spinner;
    AutoCompleteTextView customer_name_edit_text;
    ImageView search_image_view;

    ArrayList<SearchMethodeAndInternalPasswordModel> spinnerArray;
    SearchMethodeAndInternalPasswordItemsAdapter adapter;

    Database_Connection db;
    String search_method_string,check_customer_name;


    Map<Integer,String> names_resulte;
    List<String> names_values_list;
    ArrayAdapter names_adapter;

    Intent intent;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_for_debenture_activity);

        db=new Database_Connection(getBaseContext());
        initViews();

        initSpinner();

        if (search_methode_spinner!=null){

            search_methode_spinner.setAdapter(adapter);
            search_methode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    SearchMethodeAndInternalPasswordModel items=(SearchMethodeAndInternalPasswordModel)adapterView.getSelectedItem();
                    //Toast.makeText(Sp_login_activity.this, items.getName_of_shop(), Toast.LENGTH_SHORT).show();
                    search_method_string=items.getItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        search_image_view.setOnClickListener(v -> {
            if(customer_name_edit_text.getText().toString().isEmpty()){
                customer_name_edit_text.setError("يلزم تعبئه هئا الحقل");
            }else{

                check_customer_name=db.getCustomerName(customer_name_edit_text.getText().toString());
                if(check_customer_name.isEmpty()){
                    Toasty.custom(getBaseContext(),"عذرا.. هذا العميل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                }else{
                    if(search_method_string.equalsIgnoreCase("كل السندات")){
                        goToAllDebitesAndDebenturesActivity();
                    }else if(search_method_string.equalsIgnoreCase("الإجمالي")){
                        goToAmountOfDebetAndDebenturesForCustomerActivity();                    }
                }

            }
        });
    }

    private void initViews(){
        search_methode_spinner=(Spinner) findViewById(R.id.search_for_debenture_activity_search_methode_spinner);
        customer_name_edit_text=(AutoCompleteTextView) findViewById(R.id.search_for_debenture_activity_customer_namd_edit_text);
        getAllCustomers(customer_name_edit_text);
        search_image_view=(ImageView) findViewById(R.id.search_for_debenture_activity_search_image_view);
    }

    private void initSpinner(){

        spinnerArray=new ArrayList<>();
        spinnerArray.add(new SearchMethodeAndInternalPasswordModel("كل السندات"));
        spinnerArray.add(new SearchMethodeAndInternalPasswordModel("الإجمالي"));
//        spinnerArray.add(new SearchMethodeAndInternalPasswordItems("التاريخ"));

        adapter=new SearchMethodeAndInternalPasswordItemsAdapter(this,spinnerArray);

    }

    private void goToAllDebitesAndDebenturesActivity(){
        intent=new Intent(getBaseContext(),AllDebitesAndDebenturesActivity.class);
        bundle=new Bundle();

        bundle.putString("customer_name",customer_name_edit_text.getText().toString());
        bundle.putString("activity","search_for_debentures");
        intent.putExtras(bundle);
        startActivity(intent);

    }
    private void goToAmountOfDebetAndDebenturesForCustomerActivity(){
        intent=new Intent(getBaseContext(), AmountOfDebetesAndDebenturesForCustomerActivity.class);
        bundle=new Bundle();

        bundle.putString("customer_name",customer_name_edit_text.getText().toString());
        bundle.putString("activity","search_for_debentures");
        intent.putExtras(bundle);
        startActivity(intent);

    }



    private void getAllCustomers(AutoCompleteTextView textView){

        names_resulte=db.getAllCustomers();
        names_values_list=new ArrayList<String>(names_resulte.values());
        names_adapter=new ArrayAdapter(this,android.R.layout.select_dialog_item,names_values_list);
        textView.setAdapter(names_adapter);

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