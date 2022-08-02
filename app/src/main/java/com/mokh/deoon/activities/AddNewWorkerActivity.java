package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AddNewWorkerActivity extends AppCompatActivity {


    AutoCompleteTextView worker_name_edit_text;
    ImageView add,cancel;

    Database_Connection db;
    Map<Integer,String> names_resulte;
    List<String> names_values_list;
    ArrayAdapter names_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_worker_activity);

        db=new Database_Connection(getBaseContext());

        initViews();


        add.setOnClickListener(v -> {
            if(worker_name_edit_text.getText().toString().isEmpty()){
                //startAddingAlertDialog();
                worker_name_edit_text.setError("يلزم تعبئة هذا الحقل");

            }else{
                boolean result=db.insertEmployee(worker_name_edit_text.getText().toString());

                if(result==true){
                    Toasty.custom(getBaseContext(),"تمت إضافه العامل بنجاح", R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();

                }else{
                    Toasty.custom(getBaseContext(),"عذرا.. لم يتم إضافه العامل..قد يكون الإسم المضاف موجوداً من قبل",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                }
                recreate();

            }
        });

        cancel.setOnClickListener(v -> {
            if(worker_name_edit_text.getText().toString().equalsIgnoreCase("")){

                Toasty.custom(getBaseContext(),"اسم العامل بالفعل فارغ",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

            }else{
                worker_name_edit_text.setText("");
            }
        });
    }

    private void initViews(){
        worker_name_edit_text=(AutoCompleteTextView) findViewById(R.id.add_new_worker_activity_worker_name_edit_text);
        getAllemployees(worker_name_edit_text);

        add=(ImageView) findViewById(R.id.add_new_worker_activity_add_image_view);
        cancel=(ImageView) findViewById(R.id.add_new_worker_activity_cancel_image_view);
    }

    public void getAllemployees(AutoCompleteTextView textView){

        names_resulte=db.getAllEmployees();
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