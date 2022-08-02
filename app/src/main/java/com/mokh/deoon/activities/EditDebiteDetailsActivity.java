package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class EditDebiteDetailsActivity extends AppCompatActivity {
    Database_Connection db;

    AutoCompleteTextView  name_of_worker,hand;
    EditText description,deserved_amount,name_of_customer;

    ImageView edit_image_view;

    Map<Integer,String> names_resulte;
    List<String> names_values_list;
    ArrayAdapter names_adapter;


    String check_worker_name;

    String customer_string,description_string,hand_string,worker_name_string;
    int debite_id,deserved_amount_int;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_debite_details_activity);

        db=new Database_Connection(getBaseContext());


        getDataFromBundle();

        initviews();

        putDetailsToEditTexts();

        edit_image_view.setOnClickListener(v -> {

                if(description.getText().toString().isEmpty()){
                    description.setError("يلزم تعبئة هذا الحقل");

                }else{
                    if(deserved_amount.getText().toString().isEmpty()){
                        deserved_amount.setError("يلزم تعبئة هذا الحقل");

                    }else{
                        if(hand.getText().toString().isEmpty()){
                            hand.setError("يلزم تعبئة هذا الحقل");

                        }else{

                            if(name_of_worker.getText().toString().isEmpty()){
                                name_of_worker.setError("يلزم تعبئة هذا الحقل");

                            }else{
                                check_worker_name =db.getEmployeeName(name_of_worker.getText().toString());
                                CheckWorkerName(check_worker_name);
                            }
                        }
                    }
                }
            });

    }

    private void getDataFromBundle(){
        Bundle bundle=getIntent().getExtras();
        debite_id=bundle.getInt("debite_id");
        deserved_amount_int=bundle.getInt("deserved_amount");
        customer_string=bundle.getString("customer");
        description_string=bundle.getString("description");
        worker_name_string=bundle.getString("worker");
        hand_string=bundle.getString("hand");

    }

    private void initviews(){


        name_of_customer=(EditText) findViewById(R.id.edit_debits_activity_customer_name_edit_text);

        description=(EditText) findViewById(R.id.edit_debits_activity_description_edit_text);

        deserved_amount=(EditText) findViewById(R.id.edit_debits_activity_deserved_amount_edit_text);

        hand=(AutoCompleteTextView) findViewById(R.id.edit_debits_activity_hand_edit_text);
        getAllCustomers(hand);

        name_of_worker =(AutoCompleteTextView)findViewById(R.id.edit_debits_activity_worker_name_edit_text);
        getAllWorkers(name_of_worker);

        edit_image_view=(ImageView)findViewById(R.id.edit_debits_activity_edit_image_view);
    }

    private void putDetailsToEditTexts(){
        name_of_customer.setText(customer_string);
        description.setText(description_string);
        hand.setText(hand_string);
        name_of_worker.setText(worker_name_string);
        deserved_amount.setText(String.valueOf(deserved_amount_int));

    }

    private void getAllCustomers(AutoCompleteTextView textView){

        names_resulte=db.getAllCustomers();
        names_values_list=new ArrayList<String>(names_resulte.values());
        names_adapter=new ArrayAdapter(this,android.R.layout.select_dialog_item,names_values_list);
        textView.setAdapter(names_adapter);

    }

    private void getAllWorkers(AutoCompleteTextView textView){

        names_resulte=db.getAllEmployees();
        names_values_list=new ArrayList<String>(names_resulte.values());
        names_adapter=new ArrayAdapter(this,android.R.layout.select_dialog_item,names_values_list);
        textView.setAdapter(names_adapter);

    }

    private void CheckWorkerName(String check_worker_name){

            if(check_worker_name.equalsIgnoreCase("")){
                Toasty.custom(this,"عذراً.. هذا العامل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
            }else{
                startEdittingDebitAlertDialog();
            }

    }

    private void startEdittingDebitAlertDialog(){

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد التعديل")
                .setContentText("هل أنت متأكد من جميع البيانات التي أدخلتها")
                .setConfirmButton("تعديل", sweetAlertDialog -> {
                    String updateMessage=updateDebit();
                    if(updateMessage.equalsIgnoreCase("تمت عملية التعديل بنجاح")){
                        changeAlertDialogToSuccessType(sweetAlertDialog);
                        //clearAll();

                    }else{
                        changeAlertDialogToErrorType(sweetAlertDialog);
                    }

                })
                .setCancelButton("إلغاء", sweetAlertDialog ->
                        dismissAleartDialog(sweetAlertDialog)).show();

    }

    private void dismissAleartDialog(SweetAlertDialog sweetAlertDialog){
        if(sweetAlertDialog.getCancelText().equalsIgnoreCase("إلغاء")
                ||sweetAlertDialog.getCancelText().equalsIgnoreCase("حسنأً")){
            sweetAlertDialog.dismissWithAnimation();
        }else{
            sweetAlertDialog.dismissWithAnimation();
            finish();
        }
    }

    private void changeAlertDialogToSuccessType(SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitle("تمت عملية التعديل بنجاح");
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.setCancelText("تم");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.light_green));

    }

    private void changeAlertDialogToErrorType(SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitle("فشلت عمليه التعديل");
        sweetAlertDialog.setContentText("عذرا.. لم يتم التعديل حدث خطأ غير متوقع");
        sweetAlertDialog.setCancelText("حسنأً");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.red_btn_bg_color));

    }

    private String updateDebit(){

        boolean result=db.updateDebit(debite_id,name_of_worker.getText().toString(),description.getText().toString(),Integer.valueOf(deserved_amount.getText().toString()),hand.getText().toString());
        String updateMessage;
        if(result==true){
            Toasty.custom(getBaseContext(),"",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();
            updateMessage="تمت عملية التعديل بنجاح";

        }else{
            updateMessage="فشلت عمليه التعديل";
        }

        return updateMessage;
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



}