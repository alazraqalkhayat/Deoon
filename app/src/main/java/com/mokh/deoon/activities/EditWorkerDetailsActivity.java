package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class EditWorkerDetailsActivity extends AppCompatActivity {

    EditText worker_name_edit_text;
    ImageView edit;

    Database_Connection db;

    Bundle get_details_from_all_customer_activity_bundle;
    String get_worker_name_from_bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_worker_details_activity);

        initViews();

        getDetailsFromAllCustomerBundle();

        db=new Database_Connection(this);

        edit.setOnClickListener(v -> {
            if(worker_name_edit_text.getText().toString().equalsIgnoreCase(get_worker_name_from_bundle)){
                Toasty.custom(getBaseContext(),"لم يتم إجراء أي تعديل في البيانات",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

            }else{
                if(worker_name_edit_text.getText().toString().isEmpty()){
                    worker_name_edit_text.setError("يلزم تعبئة هذا الحقل");

                }else{
                    String customer=db.getEmployeeName(worker_name_edit_text.getText().toString());
                    if(customer.equalsIgnoreCase("")){
                        EdiWorkerDetailsAlertDialog();
                    }else{
                        Toasty.custom(getBaseContext(),"هذا العامل موجود مسبقاً",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                    }
                }


            }
        });


    }

    private void initViews(){
        worker_name_edit_text =(EditText) findViewById(R.id.edit_worker_details_activity_worker_name_edit_text);

        edit=(ImageView)findViewById(R.id.edit_worker_details_activity_edit_image_view);

    }

    private void getDetailsFromAllCustomerBundle(){
        get_details_from_all_customer_activity_bundle= getIntent().getExtras();
        get_worker_name_from_bundle =get_details_from_all_customer_activity_bundle.getString("worker_name");
        worker_name_edit_text.setText(get_worker_name_from_bundle);

    }

    private void EdiWorkerDetailsAlertDialog(){
        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد التعديل")
                .setContentText("في حالة التعديل سيتم تعديل إسم العامل في كل الفواتير والسندات المسجل فيها..!")
                .setConfirmButton("تأكيد", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        boolean result=db.updateWorkerOnWorkersTable(get_worker_name_from_bundle,worker_name_edit_text.getText().toString());
                        boolean result2=db.updateWorkerNameOnDebitsTable(get_worker_name_from_bundle,worker_name_edit_text.getText().toString());
                        boolean result3=db.updateWorkerNameOnDebenturesTable(get_worker_name_from_bundle,worker_name_edit_text.getText().toString());
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
            startActivity(new Intent(getBaseContext(),AllWorkersActivity.class));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_home_and_back_meu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.all_home_and_back_menu_home){
            startActivity(new Intent(this, HomeActivity.class));
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