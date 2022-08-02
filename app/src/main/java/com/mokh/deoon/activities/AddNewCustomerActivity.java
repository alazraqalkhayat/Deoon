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
import android.widget.Toast;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class AddNewCustomerActivity extends AppCompatActivity {


    EditText customer_phone_number_edit_text;
    ImageView add,cancel;

    AutoCompleteTextView customer_name_edit_text;

    Database_Connection db;
    Map<Integer,String> names_resulte;
    List<String> names_values_list;
    ArrayAdapter names_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_customer_activity);

        db=new Database_Connection(getBaseContext());
        initViews();


        add.setOnClickListener(v -> {

            if(customer_name_edit_text.getText().toString().isEmpty()){
                customer_name_edit_text.setError("يلزم تعبئة هذا الحقل");
            }else{
                if(customer_phone_number_edit_text.getText().toString().isEmpty()){
                    startAddingAlertDialog(customer_name_edit_text.getText().toString(),customer_phone_number_edit_text.getText().toString());
                }else{
                  validatePhoneNumber(customer_phone_number_edit_text.getText().toString());
                }

                //startAddingAlertDialog();

            }


        });

        cancel.setOnClickListener(v -> {
            if(customer_name_edit_text.getText().toString().equalsIgnoreCase("")
                    &&customer_phone_number_edit_text.getText().toString().equalsIgnoreCase("")){
                Toasty.custom(getBaseContext(),"جميع الحقول فارغه",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

            }else{
                customer_name_edit_text.setText("");
                customer_phone_number_edit_text.setText("");
            }
        });
    }


    private void initViews(){
        customer_name_edit_text=(AutoCompleteTextView) findViewById(R.id.add_new_customer_activity_customer_name_edit_text);
        getAllCustomers(customer_name_edit_text);

        customer_phone_number_edit_text=(EditText) findViewById(R.id.add_new_customer_activity_phone_number_edit_text);

        add=(ImageView) findViewById(R.id.add_new_customer_activity_add_image_view);
        cancel=(ImageView) findViewById(R.id.add_new_customer_activity_cancel_image_view);



    }

    public void startAddingAlertDialog(final String cust_name, final String cust_phone_number){


        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)

                .setTitleText("تأكيد الإضافه")
                .setContentText("أنت لم تدخل رقم هاتف للعميل لن يظهر أي رقم في حاله البحث عن هذا العميل ....هل تريد تأكيد الإضافه ؟")
                .setConfirmButton("إضافه", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        String addMessage=addnewCustomer(cust_name,cust_phone_number);
                        if(addMessage.equalsIgnoreCase("تمت إضافه العميل بنجاح")){
                            changeAlertDialogToSuccessType(sweetAlertDialog);
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

    private void dismissAleartDialog(SweetAlertDialog sweetAlertDialog){
        if(sweetAlertDialog.getCancelText().equalsIgnoreCase("إلغاء")
                ||sweetAlertDialog.getCancelText().equalsIgnoreCase("حسنأً")){
            sweetAlertDialog.dismissWithAnimation();
        }else{
            sweetAlertDialog.dismissWithAnimation();
            finish();
            startActivity(new Intent(getBaseContext(),AddNewCustomerActivity.class));
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
        sweetAlertDialog.setContentText("عذرا.. لم يتم إضافه العميل..قد يكون الإسم المضاف موجوداً من قبل");
        sweetAlertDialog.setCancelText("حسنأً");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.red_btn_bg_color));

    }

    private void getAllCustomers(AutoCompleteTextView textView){

        names_resulte=db.getAllCustomers();
        names_values_list=new ArrayList<String>(names_resulte.values());
        names_adapter=new ArrayAdapter(this,android.R.layout.select_dialog_item,names_values_list);
        textView.setAdapter(names_adapter);

    }

    private String addnewCustomer(String cust_name,String cust_phone_number){

        String addMessage="";
        boolean result=db.insertCustomer(cust_name,cust_phone_number);

        if(result==true){
            addMessage= "تمت إضافه العميل بنجاح";
            customer_name_edit_text.setText("");
            customer_phone_number_edit_text.setText("");
        }else{
            addMessage= "عذرا.. لم يتم إضافه العميل..قد يكون الإسم المضاف موجوداً من قبل";
        }

        return  addMessage;
    }

    private void validatePhoneNumber(String customer_phone_number){
        if(customer_phone_number.length()>9){
            customer_phone_number_edit_text.setError("رقم الهاتف الذي ادخلته اكبر من 9 ارقام");

        }else if(customer_phone_number.length()<9){
            customer_phone_number_edit_text.setError("رقم الهاتف الذي ادخلته اصغر من 9 ارقام");

        }else{

            char first_letter= (char) customer_phone_number.charAt(0);
            int second_letter=(char) customer_phone_number.charAt(1);

            if(first_letter=='7'){
                if(second_letter=='7'||second_letter=='3'
                        ||second_letter=='1'||second_letter=='0'){
                    String addMessage=addnewCustomer(customer_name_edit_text.getText().toString(),customer_phone_number_edit_text.getText().toString());

                    if(addMessage.equalsIgnoreCase("تمت إضافه العميل بنجاح")){
                        Toasty.custom(getBaseContext(),addMessage,R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();

                    }else{
                        Toasty.custom(getBaseContext(),addMessage,R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                    }

                    AddNewCustomerActivity.this.recreate();

                }else{
                    customer_phone_number_edit_text.setError("طريقة إدخال خاطئه");
                }
            }else{
                customer_phone_number_edit_text.setError("طريقة إدخال خاطئه");
            }

        }
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
