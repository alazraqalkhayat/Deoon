package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.models.All_customers_model;
import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllCustomersActivity extends AppCompatActivity {

    ArrayList<All_customers_model> _all_customers_model_arr;
    ListView all_customers_list_view;
    All_customers_list_adapter all_customers_list_adapter;
    Database_Connection db;

    String ACTIVITY_STATUTE;

    int REQUEST_CODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_customers_activity);

        ACTIVITY_STATUTE="onCreate";
        REQUEST_CODE=2;

        all_customers_list_view=(ListView)findViewById(R.id.all_customers_activity_list_view);
        db=new Database_Connection(this);
        _all_customers_model_arr =new ArrayList<>();
        _all_customers_model_arr =db.getAllCustomersTOListView();

        all_customers_list_adapter=new All_customers_list_adapter(this, _all_customers_model_arr);
        all_customers_list_view.setAdapter(all_customers_list_adapter);




    }

    @Override
    protected void onPause() {
        super.onPause();
        ACTIVITY_STATUTE="onPause";
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(ACTIVITY_STATUTE.equalsIgnoreCase("onPause")){
            recreate();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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





    public class All_customers_list_adapter extends BaseAdapter {

        TextView name,phone_number;
        LinearLayout linearLayout;
        ImageView edit,delete;
        Intent intent;
        Bundle bundle;

        ArrayList<All_customers_model> arr;
        Context context;

        Database_Connection db;

        public All_customers_list_adapter(Context context, ArrayList<All_customers_model> arr) {
            this.context=context;
            this.arr = arr;

        }


        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int i) {
            return arr.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }



        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            All_customers_model current_items=arr.get(position);



            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=inflater.inflate(R.layout.items_of_all_customers_list_view,null);

            name = (TextView) v.findViewById(R.id.items_of_all_customers_list_view_customer_name_text_view);
            phone_number = (TextView) v.findViewById(R.id.items_of_all_customers_list_view_customer_phone_number_text_view);
            linearLayout=(LinearLayout)v.findViewById(R.id.items_of_all_customers_list_view_linear);
            edit=(ImageView)v.findViewById(R.id.items_of_all_customer_edit_customer_details_image_view);
            delete=(ImageView)v.findViewById(R.id.items_of_all_customer_delete_customer_image_view);


            //________________________________________

            name.setText(current_items.getCustomer_name());
            phone_number.setText(current_items.getCustomer_phone_number());
            String phone=arr.get(position).getCustomer_phone_number();
            if(phone_number.getText().toString().equalsIgnoreCase("")){
                phone_number.setText("لا يوجد رقم هاتف لهذا العميل");

            }else{
                phone_number.setText(current_items.getCustomer_phone_number());
            }

            edit.setOnClickListener(v1 -> {
//                Items_of_all_customers current_items2=arr.get(position);
                if(Shared_Helper.getkey(context,"internal_pass").equalsIgnoreCase("")){
                    showDialogForCheckPassword(arr.get(position),"edit");
                }else{
                    checkBeforeEditCustomer(arr.get(position),"check_password");
                }

            });

            delete.setOnClickListener(v12 -> {
                if(Shared_Helper.getkey(context,"internal_pass").equalsIgnoreCase("")){
                    showDialogForCheckPassword(arr.get(position),"delete");
                }else{
                    checkBeforeDeleteCustomer(arr.get(position));
                }
            });


            return v;
        }


        private void showDialogForCheckPassword(All_customers_model current_items, String option){
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitleText("كلمة المرور");
            sweetAlertDialog.setContentText("إن بياناتك معرضه للخطر .. قم بإنشاء كلمة مرور لأمان اكبر ");
            sweetAlertDialog.setConfirmButton("إنشاء", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    context.startActivity(new Intent(context, CreateInternalPasswordActivity.class));
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            sweetAlertDialog.setCancelButton("تخطي", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if(option.equalsIgnoreCase("edit")){
                        checkBeforeEditCustomer(current_items,"edit_customer");

                    }else if(option.equalsIgnoreCase("delete")){
                        db=new Database_Connection(context);
                        int sum_of_debites=db.getSumOfDebits(current_items.getCustomer_name());
                        int sum_of_debentures=db.getSumOfDebenture(current_items.getCustomer_name());
                        int adider=sum_of_debites-sum_of_debentures;
                        showDialogForDeleteCustomer(current_items,adider);
                    }
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            sweetAlertDialog.show();
        }

        private void checkBeforeEditCustomer(All_customers_model current_items, String forWaord){

            if(forWaord.equalsIgnoreCase("check_password")){
                intent =new Intent(context, CheckPasswordActivity.class);
                bundle =new Bundle();
            }else if(forWaord.equalsIgnoreCase("edit_customer")){
                intent =new Intent(context, EditCustomerDetailsActivity.class);
                bundle =new Bundle();
            }


            bundle.putString("customer_name",current_items.getCustomer_name());
            bundle.putString("customer_phone_number",current_items.getCustomer_phone_number());

            bundle.putString("activty","all_customer_activity");

            intent.putExtras(bundle);
            context.startActivity(intent);


        }

        private void checkBeforeDeleteCustomer(All_customers_model current_items){
            intent =new Intent(context, CheckPasswordActivity.class);
            bundle =new Bundle();

            bundle.putString("customer_name",current_items.getCustomer_name());
            bundle.putString("activty","all_customer_activity_delete");

            intent.putExtras(bundle);
            context.startActivity(intent);




        }

        private void showDialogForDeleteCustomer(All_customers_model current_items, int adider){
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitleText("تأكيد الحذف");
            sweetAlertDialog.setContentText("هل أنت متأكد من حذف هذا العميل .. عند الحذف ستحتذف جميع البيانات المتعلقه بهذا العميل");
            sweetAlertDialog.setConfirmButton("تأكيد", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if(adider>0){
                        changeAlertDialogToErrorType(sweetAlertDialog,adider);
                    }else{
                        changeAlertDialogToSuccessType(sweetAlertDialog);
                        deleteCustomer(current_items.getCustomer_name());
                    }
                }
            });
            sweetAlertDialog.setCancelButton("إلغاء", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dismissAleartDialog(sweetAlertDialog);
                }
            });
            sweetAlertDialog.show();
        }

        private void dismissAleartDialog(SweetAlertDialog sweetAlertDialog){
            if(sweetAlertDialog.getCancelText().equalsIgnoreCase("إلغاء")
                    ||sweetAlertDialog.getCancelText().equalsIgnoreCase("حسنأً")){
                sweetAlertDialog.dismissWithAnimation();
            }else{
                sweetAlertDialog.dismissWithAnimation();
                finish();
                startActivity(new Intent(getBaseContext(),AllCustomersActivity.class));
            }
        }

        private void changeAlertDialogToSuccessType(SweetAlertDialog sweetAlertDialog){
            sweetAlertDialog.hideConfirmButton();
            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitle("تمت عملية الحذف بنجاح");
            sweetAlertDialog.setContentText("");
            sweetAlertDialog.setCancelText("تم");
            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.light_green));

        }

        private void changeAlertDialogToErrorType(SweetAlertDialog sweetAlertDialog,int adider){
            sweetAlertDialog.hideConfirmButton();
            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitle("فشلت عمليه الحذف");
            sweetAlertDialog.setContentText("لا يمكن حذف العميل في حاله ما يزال هناك مبلغ مالي متبقي عليه"+"\n"+"المبلغ المتبقي عليه هو "+adider+" ريال ");
            sweetAlertDialog.setCancelText("حسنأً");
            sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.red_btn_bg_color));

        }

        public void deleteCustomer(String customer_name){
            db=new Database_Connection(context);
            db.deletCustomerByName(customer_name);
            db.deletAllDebitesByName(customer_name);
            db.deletAllDebenturesByName(customer_name);
        }


    }


}
