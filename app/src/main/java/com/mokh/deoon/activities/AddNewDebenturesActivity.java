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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class AddNewDebenturesActivity extends AppCompatActivity {

    AutoCompleteTextView customer_name_edit_text,worker_name_edit_text;
    EditText paid_mony_edit_text;
    ImageView add,cancel;

    Map<Integer,String> names_resulte;
    List<String> names_values_list;
    ArrayAdapter names_adapter;

    Database_Connection db;

    String check_employee_name,check_customer_name;

    long pressed_back;
    private Toast back_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_debentures_activity);

        db=new Database_Connection(getBaseContext());

        initViews();

        add.setOnClickListener(v -> {
            if(customer_name_edit_text.getText().toString().isEmpty()){
                customer_name_edit_text.setError("يلوم تعبئة هذا الحقل");

            }else{
                if(paid_mony_edit_text.getText().toString().isEmpty()){
                    paid_mony_edit_text.setError("يلوم تعبئة هذا الحقل");

                }else{
                    if(worker_name_edit_text.getText().toString().isEmpty()){
                        worker_name_edit_text.setError("يلوم تعبئة هذا الحقل");

                    }else{
                        if(Integer.valueOf(paid_mony_edit_text.getText().toString())<=0){
                            paid_mony_edit_text.setError("لقد ادخلت قيمه غير صحيحه");
                        }else{
                            String date_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a").format(new Date());
                            check_customer_name=db.getCustomerName(customer_name_edit_text.getText().toString());
                            check_employee_name=db.getEmployeeName(worker_name_edit_text.getText().toString());

                            Check_customer_name_and_employee_name(check_customer_name,check_employee_name,date_time);
                        }

                    }
                }
            }
        });

        cancel.setOnClickListener(v -> {
            if(customer_name_edit_text.getText().toString().isEmpty()
                    &&paid_mony_edit_text.getText().toString().isEmpty()
                    &&worker_name_edit_text.getText().toString().isEmpty()){
                Toasty.custom(getBaseContext(),"جميع الحقول بالفعل فارغه ",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

            }else{
                startcancelingAlertDialog();
            }
        });
    }

    private void initViews(){
        customer_name_edit_text=(AutoCompleteTextView) findViewById(R.id.new_debenture_activity_customer_name_edit_text);
        getAllCustomers(customer_name_edit_text);

        paid_mony_edit_text=(EditText)findViewById(R.id.new_debenture_activity_paied_mony_edit_text);

        worker_name_edit_text=(AutoCompleteTextView) findViewById(R.id.new_debenture_activity_worker_name_edit_text);
        getAllWorkers(worker_name_edit_text);

        add=(ImageView) findViewById(R.id.new_debenture_activity_add_image_view);
        cancel=(ImageView) findViewById(R.id.new_debenture_activity_cancel_image_view);
    }

    public void getAllCustomers(AutoCompleteTextView textView){

        names_resulte=db.getAllCustomers();
        names_values_list=new ArrayList<String>(names_resulte.values());
        names_adapter=new ArrayAdapter(this,android.R.layout.select_dialog_item,names_values_list);
        textView.setAdapter(names_adapter);

    }

    public void getAllWorkers(AutoCompleteTextView textView){

        names_resulte=db.getAllEmployees();
        names_values_list=new ArrayList<String>(names_resulte.values());
        names_adapter=new ArrayAdapter(this,android.R.layout.select_dialog_item,names_values_list);
        textView.setAdapter(names_adapter);

    }

    private void Check_customer_name_and_employee_name(String cus_name,String emp_name,String date){



        if(cus_name.equalsIgnoreCase("")){
            Toasty.custom(getBaseContext(),"عذرا.. هذا العميل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

        }else{

            if(emp_name.equalsIgnoreCase("")){
                Toasty.custom(getBaseContext(),"عذراً.. هذا العامل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();


            }else{
                startAddingNewDebentureAlertDialog(date);
            }
        }
    }

    private void startAddingNewDebentureAlertDialog(final String date){

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد الإضافه")
                .setContentText("هل أنت متأكد من جميع البيانات التي أدخلتها")
                .setConfirmButton("إضافه", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        int sum_of_debites=db.getSumOfDebits(customer_name_edit_text.getText().toString());
                        int sum_of_debentures=db.getSumOfDebenture(customer_name_edit_text.getText().toString());
                        int adider=sum_of_debites-sum_of_debentures;
                        if (adider < Integer.valueOf(paid_mony_edit_text.getText().toString())) {

                            startAlertDialogForCheckingThePaiedMonye();
                            sweetAlertDialog.dismissWithAnimation();

                        } else {
                            String addMessage=insertNewDebeneture(date);
                            if(addMessage.equalsIgnoreCase("تمت إضافه السند بنجاح")){
                                changeAlertDialogToSuccessType(sweetAlertDialog);
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

    private void startAlertDialogForCheckingThePaiedMonye(){

        new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                .setTitleText("خطأ في التسديد")
                .setContentText("عذرا..المبلغ المسدد اكبر من المبلغ المتبقي تأكد من إدخال المبلغ المسدد بالطريقه الصحيحه")
                .hideConfirmButton()
                .setCancelButton("حسناً", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }

    private void startcancelingAlertDialog(){

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد الإلغاء")
                .setContentText("هل أنت متأكد الغاء هذه السند")
                .setConfirmButton("تأكيد", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        changeAlertDialogToSuccessType(sweetAlertDialog);
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
            startActivity(new Intent(getBaseContext(),AddNewDebenturesActivity.class));
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
        sweetAlertDialog.setContentText("عذرا.. لم يتم إضافه السند حصل خطأ غير متوقع");
        sweetAlertDialog.setCancelText("حسنأً");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.red_btn_bg_color));

    }

    private String insertNewDebeneture(String date){

        String addMessage="";
        boolean result= db.insertNewDebenture(customer_name_edit_text.getText().toString(),worker_name_edit_text.getText().toString(),Integer.valueOf(paid_mony_edit_text.getText().toString()),date);
        if(result==true){
            addMessage="تمت إضافه السند بنجاح";
        }else{
            Toasty.custom(getBaseContext(),"عذرا.. لم يتم إضافه السند",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
            addMessage="عذرا.. لم يتم إضافه السند";
        }

        return addMessage;
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