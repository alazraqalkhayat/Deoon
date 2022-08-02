package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CheckPasswordActivity extends AppCompatActivity {

    EditText password_edit_text;
    ImageView check_image_view;

    Bundle recever_bundle,sender_bundle;
    Intent sender_intent;
    String check_activity_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_password);

        initViews();


        recever_bundle =getIntent().getExtras();
        check_activity_string = recever_bundle.getString("activty");




        password_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if(s.toString().equalsIgnoreCase(Shared_Helper.getkey(getBaseContext(),"internal_pass"))){
                                checkActivity(check_activity_string);
                        }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*check_image_view.setOnClickListener(v -> {

        });*/
    }

    private void initViews(){
        password_edit_text=(EditText) findViewById(R.id.check_password_activity_password_edit_text);
//        check_image_view=(ImageView) findViewById(R.id.check_password_activity_check_image_view);
    }

    private void checkActivity(String check_activity_string){
        if(check_activity_string.equalsIgnoreCase("all_customer_activity")){
            sender_intent=new Intent(getBaseContext(), EditCustomerDetailsActivity.class);
            sender_bundle=new Bundle();

            String customer_name= recever_bundle.getString("customer_name");
            String customer_phone_number= recever_bundle.getString("customer_phone_number");

            sender_bundle.putString("customer_name",customer_name);
            sender_bundle.putString("customer_phone_number",customer_phone_number);
            sender_intent.putExtras(sender_bundle);

            startActivity(sender_intent);
            finish();

        }else  if(check_activity_string.equalsIgnoreCase("all_workers_activity")){
            sender_intent=new Intent(getBaseContext(), EditWorkerDetailsActivity.class);
            sender_bundle=new Bundle();
            String worker_name= recever_bundle.getString("worker_name");
            sender_bundle.putString("worker_name",worker_name);

            sender_intent.putExtras(sender_bundle);
            startActivity(sender_intent);
            finish();
        }else if(check_activity_string.equalsIgnoreCase("delete_password")){
                deletePasswordDialog();
        }else if(check_activity_string.equalsIgnoreCase("edit_debit_activity")){
            sender_intent=new Intent(getBaseContext(), EditDebiteDetailsActivity.class);
            sender_bundle=new Bundle();

            int debite_id= recever_bundle.getInt("debite_id");
            int deserved_amount_int= recever_bundle.getInt("deserved_amount");
            String customer_string= recever_bundle.getString("customer");
            String description_string= recever_bundle.getString("description");
            String worker_name_string= recever_bundle.getString("worker");
            String hand_string= recever_bundle.getString("hand");

            sender_bundle.putInt("debite_id",debite_id);
            sender_bundle.putInt("deserved_amount",deserved_amount_int);
            sender_bundle.putString("customer",customer_string);
            sender_bundle.putString("description",description_string);
            sender_bundle.putString("worker",worker_name_string);
            sender_bundle.putString("hand",hand_string);

            sender_intent.putExtras(sender_bundle);
            startActivity(sender_intent);
            finish();
        }else if(check_activity_string.equalsIgnoreCase("edit_debenture_activity")){
            sender_intent=new Intent(getBaseContext(), EditDebentureDetailsActivity.class);
            sender_bundle=new Bundle();

            int debenture_id_int = recever_bundle.getInt("debenture_id");
            int money_paied_int = recever_bundle.getInt("money_paied");
            String worker_name_string = recever_bundle.getString("worker_name");
            String customer_name_string = recever_bundle.getString("customer_name");

            sender_bundle.putInt("debenture_id",debenture_id_int);
            sender_bundle.putInt("money_paied",money_paied_int);
            sender_bundle.putString("customer_name",customer_name_string);
            sender_bundle.putString("worker_name",worker_name_string);

            sender_intent.putExtras(sender_bundle);
            startActivity(sender_intent);
            finish();

        }else if(check_activity_string.equalsIgnoreCase("all_customer_activity_edit")){
            sender_intent=new Intent(getBaseContext(), EditWorkerDetailsActivity.class);
            sender_bundle=new Bundle();

            int debenture_id_int = recever_bundle.getInt("debenture_id");
            int money_paied_int = recever_bundle.getInt("money_paied");
            String worker_name_string = recever_bundle.getString("worker_name");
            String customer_name_string = recever_bundle.getString("customer_name");

            sender_bundle.putInt("debenture_id",debenture_id_int);
            sender_bundle.putInt("money_paied",money_paied_int);
            sender_bundle.putString("customer_name",worker_name_string);
            sender_bundle.putString("worker_name",customer_name_string);
            sender_bundle.putString("activty","edit_debenture_activity");

            sender_intent.putExtras(sender_bundle);
            startActivity(sender_intent);
            finish();

        }else if(check_activity_string.equalsIgnoreCase("all_customer_activity_delete")){
            sender_bundle=new Bundle();
           String customer_name=recever_bundle.getString("customer_name");
            Database_Connection db=new Database_Connection(getBaseContext());
            int sum_of_debites=db.getSumOfDebits(customer_name);
            int sum_of_debentures=db.getSumOfDebenture(customer_name);
            int adider=sum_of_debites-sum_of_debentures;

            deleteCustomerDialog(customer_name,adider);
        }else if(check_activity_string.equalsIgnoreCase("delete_debenture_activity")){
            sender_bundle=new Bundle();
            int debenture_id=recever_bundle.getInt("debenture_id");
            deleteDebentureDialog(debenture_id);
        }else if(check_activity_string.equalsIgnoreCase("delete_debite_activity")){
            sender_bundle=new Bundle();
            int debite_id=recever_bundle.getInt("debenture_id");
            deleteDebiteDialog(debite_id);
        }else if(check_activity_string.equalsIgnoreCase("clear")){
            sender_bundle=new Bundle();
            String customer_name=recever_bundle.getString("customer_name");
            removeAllDataAlertDialog(customer_name);
        }

    }

    private void deletePasswordDialog(){
        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد الحذف")
                .setContentText("هل أنت متأكد من حذف كلمة المرور")
                .setConfirmButton("تأكيد", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Shared_Helper.putKey(getBaseContext(),"internal_pass","");
                        Shared_Helper.putKey(getBaseContext(),"protiction_question","");
                        Shared_Helper.putKey(getBaseContext(),"answer_protiction_question","");

                        changeAlertDialogToSuccessType(sweetAlertDialog);

                    }
                })
                .setCancelButton("إلغاء", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dismissAleartDialog(sweetAlertDialog,"delete_password");
                    }
                }).show();
    }

    private void deleteCustomerDialog(String customer_name, int adider){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("تأكيد الحذف");
        sweetAlertDialog.setContentText("هل أنت متأكد من حذف هذا العميل .. عند الحذف ستحتذف جميع البيانات المتعلقه بهذا العميل");
        sweetAlertDialog.setConfirmButton("تأكيد", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if(adider>0){
                    changeAlertDialogToErrorTypeForDeleteCustomrt(sweetAlertDialog,adider);
                }else{
                    changeAlertDialogToSuccessType(sweetAlertDialog);
                    deleteCustomer(customer_name);
                }
            }
        });
        sweetAlertDialog.setCancelButton("إلغاء", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dismissAleartDialog(sweetAlertDialog,"delete_customer");
            }
        });
        sweetAlertDialog.show();
    }

    private void deleteDebiteDialog(int debite_id){
        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تأكيد الحذف")
                .setContentText("هل أنت متأكد من حذف هذه الفاتوره")
                .setConfirmButton("تأكيد", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        changeAlertDialogToSuccessType(sweetAlertDialog);
                        Database_Connection db=new Database_Connection(CheckPasswordActivity.this);
                        db.deletDebeteById(debite_id);
                    }
                })
                .setCancelButton("إلغاء", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dismissAleartDialog(sweetAlertDialog,"delete_debite");
                    }
                }).show();
    }

    private void deleteDebentureDialog(int debenture_id){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("تأكيد الحذف");
        sweetAlertDialog.setContentText("هل أنت متأكد من حذف هذا السند..!");
        sweetAlertDialog.setConfirmButton("تأكيد", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Database_Connection db=new Database_Connection(CheckPasswordActivity.this);
                db.deletDebenturesById(debenture_id);
                changeAlertDialogToSuccessType(sweetAlertDialog);
            }
        });
        sweetAlertDialog.setCancelButton("إلغاء", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dismissAleartDialog(sweetAlertDialog,"delete_debenture");
            }
        });
        sweetAlertDialog.show();
    }

    public void deleteCustomer(String customer_name){
        Database_Connection db=new Database_Connection(getBaseContext());
        db.deletCustomerByName(customer_name);
        db.deletAllDebitesByName(customer_name);
        db.deletAllDebenturesByName(customer_name);
    }

    private void removeAllDataAlertDialog(final String customer_name) {

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)

                .setTitleText("تأكيد الحذف")
                .setContentText("عند تصفيه الحساب سوف يتم حذف كل الديون و السندات لهذا العميل ...")
                .setConfirmButton("تأكيد", sweetAlertDialog -> {
                    startRemoveAllData(customer_name);
                    sweetAlertDialog.hideConfirmButton();
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitle("تم التصفيه بنجاح");
                    sweetAlertDialog.setContentText("");
                    sweetAlertDialog.setCancelText("تم");
                    sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.light_green));


                })
                .setCancelButton("إلغاء", sweetAlertDialog -> {
                    dismissAleartDialog(sweetAlertDialog,"clear");
                }).show();


    }

    private void startRemoveAllData(String customer_name) {
        Database_Connection db=new Database_Connection(this);
        db.deletAllDebitesByName(customer_name);
        db.deletAllDebenturesByName(customer_name);
    }

    private void changeAlertDialogToSuccessType(SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitle("تم الحذف بنجاح");
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.setCancelText("تم");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.light_green));

    }

    private void changeAlertDialogToErrorTypeForDeleteCustomrt(SweetAlertDialog sweetAlertDialog, int adider){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitle("فشلت عمليه الحذف");
        sweetAlertDialog.setContentText("لا يمكن حذف العميل في حاله ما يزال هناك مبلغ مالي متبقي عليه"+"\n"+"المبلغ المتبقي عليه هو "+adider+" ريال ");
        sweetAlertDialog.setCancelText("حسنأً");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.red_btn_bg_color));

    }

    private void dismissAleartDialog(SweetAlertDialog sweetAlertDialog,String option){
        if(sweetAlertDialog.getCancelText().equalsIgnoreCase("إلغاء")
                ||sweetAlertDialog.getCancelText().equalsIgnoreCase("حسنأً")){
            sweetAlertDialog.dismissWithAnimation();
            finish();
        }else{
            if(option.equalsIgnoreCase("delete_password")){
                setResult(RESULT_OK);
                finish();
            }else if(option.equalsIgnoreCase("delete_customer")){
                setResult(RESULT_OK);
                finish();
            }else if(option.equalsIgnoreCase("delete_debite")){
//                startActivity(new Intent(getBaseContext(),SearchForDebitsActivity.class));
                setResult(RESULT_OK);
                finish();
            }else if(option.equalsIgnoreCase("delete_debenture")){
//                startActivity(new Intent(getBaseContext(),SearchForDebentureActivity.class));
                setResult(RESULT_OK);
                finish();
            }else if(option.equalsIgnoreCase("clear")){
                setResult(RESULT_OK);
                finish();
            }
            sweetAlertDialog.dismissWithAnimation();
            finish();
        }
    }

}