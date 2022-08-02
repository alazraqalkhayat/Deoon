package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class AddNewDebiteActivity extends AppCompatActivity {


    Database_Connection db;

    AutoCompleteTextView name_of_customer, name_of_worker,hand;
    EditText  description,deserved_amount;

    ImageView add_image_view,cancel_image_view;

    Map<Integer,String> names_resulte;
    List<String> names_values_list;
    ArrayAdapter names_adapter;


    String check_worker_name,check_customer_name;

    long pressed_back;
    private Toast back_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_debite_activity);

        db=new Database_Connection(getBaseContext());

        initviews();

        add_image_view.setOnClickListener(v -> {

            if(name_of_customer.getText().toString().isEmpty()){
                name_of_customer.setError("يلزم تعبئة هذا الحقل");

            }else {
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

                                if(Integer.valueOf(deserved_amount.getText().toString())<=0){
                                    deserved_amount.setError("لقد ادخلت قيمه غير صحيحه");

                                }else{
                                    String date_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a").format(new Date());
                                    check_customer_name=db.getCustomerName(name_of_customer.getText().toString());
                                    check_worker_name =db.getEmployeeName(name_of_worker.getText().toString());
                                    Check_customer_name_and_employee_name(check_customer_name, check_worker_name,date_time);
                                }

                            }
                        }
                    }
                }
            }
        });

        cancel_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_of_customer.getText().toString().isEmpty()
                        &&description.getText().toString().isEmpty()
                        &&deserved_amount.getText().toString().isEmpty()
                        &&hand.getText().toString().isEmpty()
                        &&name_of_worker.getText().toString().isEmpty()){
                    Toasty.custom(getBaseContext(),"جميع الحقول بالفعل فارغه",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                }else{
                    startcancelingAlertDialog();
                }
            }
        });

    }


    private void initviews(){

        add_image_view=(ImageView)findViewById(R.id.new_debits_activity_add_image_view);
        cancel_image_view=(ImageView)findViewById(R.id.new_debits_activity_cancel_image_view);

        name_of_customer=(AutoCompleteTextView)findViewById(R.id.new_debits_activity_customer_name_edit_text);
        getAllCustomers(name_of_customer);

        description=(EditText) findViewById(R.id.new_debits_activity_description_edit_text);

        deserved_amount=(EditText) findViewById(R.id.new_debits_activity_deserved_amount_edit_text);

        hand=(AutoCompleteTextView) findViewById(R.id.new_debits_activity_hand_edit_text);
        getAllCustomers(hand);

        name_of_worker =(AutoCompleteTextView)findViewById(R.id.new_debits_activity_worker_name_edit_text);
        getAllWorkers(name_of_worker);

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

    private void Check_customer_name_and_employee_name(String cus_name,String emp_name,String date){



        if(cus_name.equalsIgnoreCase("")){
            Toasty.custom(this,"عذرا.. هذا العميل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
            startAddingNewCustomerAndNewDebitAlertDialog(date,emp_name);
        }else{

            if(emp_name.equalsIgnoreCase("")){
                Toasty.custom(this,"عذراً.. هذا العامل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
            }else{
                startAddingNewDebitAlertDialog(date);
            }
        }
    }

    private void startAddingNewDebitAlertDialog(final String date){

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد الإضافه")
                .setContentText("هل أنت متأكد من جميع البيانات التي أدخلتها")
                .setConfirmButton("إضافه", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String addMessage=insetrDebite(date);
                        if(addMessage.equalsIgnoreCase("تمت إضافه الدين بنجاح")){
                            changeAlertDialogToSuccessType(sweetAlertDialog);
                            //clearAll();

                        }else{
                            changeAlertDialogToErrorType(sweetAlertDialog);
                        }

                    }
                })
                .setCancelButton("إلغاء", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dismissAleartDialog(sweetAlertDialog);
                    }
                }).show();

    }

    private void startAddingNewCustomerAndNewDebitAlertDialog(final String date, final String emp_name){


        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد الإضافه")
                .setContentText("إضافه كـ عميل جديد مع إضافه الفاتوره")
                .setConfirmButton("إضافه", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if(emp_name.equalsIgnoreCase("")){
                            Toasty.custom(getBaseContext(),"عذراً.. هذا العامل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                            sweetAlertDialog.dismissWithAnimation();
                        }else{

                            insertCustomer();
                            String addMessage=insetrDebite(date);
                            if(addMessage.equalsIgnoreCase("تمت إضافه الدين بنجاح")){
                                changeAlertDialogToSuccessType(sweetAlertDialog);
                                //clearAll();

                            }else{
                              changeAlertDialogToErrorType(sweetAlertDialog);
                            }

                        }


                    }
                })
                .setCancelButton("إلغاء", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dismissAleartDialog(sweetAlertDialog);
                    }
                }).show();

    }

    private void startcancelingAlertDialog(){

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد الإلغاء")
                .setContentText("هل أنت متأكد الغاء هذه الفاتوره")
                .setConfirmButton("تأكيد", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        changeAlertDialogToSuccessType(sweetAlertDialog);

                    }
                })
                .setCancelButton("إلغاء", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }

    private String insetrDebite(String date){
        String addMessage="";
        boolean result =db.insertNewDepite(name_of_customer.getText().toString(),name_of_worker.getText().toString(),description.getText().toString(),Integer.valueOf(deserved_amount.getText().toString()),hand.getText().toString(),date);
        if(result==true){
            addMessage="تمت إضافه الدين بنجاح";
        }else{
            addMessage="عذرا.. لم يتم إضافه الدين";
        }
        return addMessage;
    }

    private void insertCustomer(){
        boolean result=db.insertCustomerName(name_of_customer.getText().toString());
        if(result==true){
            Toasty.custom(getBaseContext(),"تمت إضافه العميل بنجاح",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();

        }else{
            Toasty.custom(getBaseContext(),"عذرا.. لم يتم إضافه العميل..قد يكون الإسم المضاف موجوداً من قبل",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

        }
    }

    private void dismissAleartDialog(SweetAlertDialog sweetAlertDialog){
        if(sweetAlertDialog.getCancelText().equalsIgnoreCase("إلغاء")
                ||sweetAlertDialog.getCancelText().equalsIgnoreCase("حسنأً")){
                sweetAlertDialog.dismissWithAnimation();
        }else{
            sweetAlertDialog.dismissWithAnimation();
            finish();
            startActivity(new Intent(getBaseContext(),AddNewDebiteActivity.class));
        }
    }

    private void changeAlertDialogToSuccessType(SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitle("تمت الإضافه بنجاح");
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.setCancelText("تم");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.light_green));

    }

    private void changeAlertDialogToErrorType(SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitle("فشلت الإضافه");
        sweetAlertDialog.setContentText("عذرا.. لم يتم إضافه الدين حصل خطأ غير متوقع");
        sweetAlertDialog.setCancelText("حسنأً");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.red_btn_bg_color));

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