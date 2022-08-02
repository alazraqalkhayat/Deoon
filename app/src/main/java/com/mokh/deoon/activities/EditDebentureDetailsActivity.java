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

public class EditDebentureDetailsActivity extends AppCompatActivity {
    AutoCompleteTextView worker_name_edit_text;
    EditText paid_mony_edit_text,customer_name_edit_text;
    ImageView edit_image_view;

    Map<Integer,String> names_resulte;
    List<String> names_values_list;
    ArrayAdapter names_adapter;

    Database_Connection db;

    String check_employee_name;


    String customer_name_string, worker_name_string;
    int debenture_id_int, money_paied_int;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_debenture_details_activity);

        db=new Database_Connection(getBaseContext());

        getDataFromBundle();

        initViews();

        putDetailsToEditTexts();

        edit_image_view.setOnClickListener(v -> {

                if(paid_mony_edit_text.getText().toString().isEmpty()){
                    paid_mony_edit_text.setError("يلوم تعبئة هذا الحقل");

                }else{
                    if(worker_name_edit_text.getText().toString().isEmpty()){
                        worker_name_edit_text.setError("يلوم تعبئة هذا الحقل");

                    }else{
                        check_employee_name=db.getEmployeeName(worker_name_edit_text.getText().toString());

                        checkWorkerName(check_employee_name);

                    }
                }

        });

    }


    private void getDataFromBundle(){
        Bundle bundle=getIntent().getExtras();
        debenture_id_int =bundle.getInt("debenture_id");
        money_paied_int =bundle.getInt("money_paied");
        worker_name_string =bundle.getString("worker_name");
        customer_name_string =bundle.getString("customer_name");

    }

    private void initViews(){
        customer_name_edit_text=(EditText) findViewById(R.id.edite_debenture_activity_customer_name_edit_text);

        paid_mony_edit_text=(EditText)findViewById(R.id.edit_debenture_activity_paied_mony_edit_text);

        worker_name_edit_text=(AutoCompleteTextView) findViewById(R.id.edit_debenture_activity_worker_name_edit_text);
        getAllWorkers(worker_name_edit_text);

        edit_image_view =(ImageView) findViewById(R.id.edit_debenture_activity_edit_image_view);
    }

    private void putDetailsToEditTexts(){
        customer_name_edit_text.setText(customer_name_string);
        paid_mony_edit_text.setText(String.valueOf(money_paied_int));
        worker_name_edit_text.setText(worker_name_string);
    }

    public void getAllWorkers(AutoCompleteTextView textView){

        names_resulte=db.getAllEmployees();
        names_values_list=new ArrayList<String>(names_resulte.values());
        names_adapter=new ArrayAdapter(this,android.R.layout.select_dialog_item,names_values_list);
        textView.setAdapter(names_adapter);

    }

    private void checkWorkerName(String emp_name){

            if(emp_name.equalsIgnoreCase("")){
                Toasty.custom(getBaseContext(),"عذراً.. هذا العامل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

            }else{
                startAddingNewDebentureAlertDialog();
            }

    }

    private void startAddingNewDebentureAlertDialog(){

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد التعديل")
                .setContentText("هل أنت متأكد من جميع البيانات التي أدخلتها")
                .setConfirmButton("تعديل", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        int sum_of_deserved_amount=db.getSumOfDebits(customer_name_edit_text.getText().toString());

                        if (sum_of_deserved_amount < Integer.valueOf(paid_mony_edit_text.getText().toString())) {

                            startAlertDialogForCheckingThePaiedMonye();
                            sweetAlertDialog.dismissWithAnimation();

                        } else {
                            String addMessage= updateDebenture();
                            if(addMessage.equalsIgnoreCase("تمت عملية التعديل بنجاح")){
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
                .setContentText("عذرا..المبلغ المسدد اكبر من إجمالي الديون تأكد من إدخال المبلغ المسدد بالطريقه الصحيحه")
                .setCancelButton("حسناً", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
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
        }
    }

    private void changeAlertDialogToSuccessType(SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitle("تمت عملة التعديل بنجاح");
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.setCancelText("تم");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.light_green));

    }

    private void changeAlertDialogToErrorType(SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitle("فشلت عملية التعديل");
        sweetAlertDialog.setContentText("عذرا.. لم يتم التعديل حدث خطأ غير متوقع");
        sweetAlertDialog.setCancelText("حسنأً");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.red_btn_bg_color));

    }

    private String updateDebenture(){

        boolean result=db.updateDebenture(debenture_id_int,worker_name_edit_text.getText().toString(),Integer.valueOf(paid_mony_edit_text.getText().toString()));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}