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
import android.widget.ListView;
import android.widget.TextView;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.models.All_workrers_model;
import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllWorkersActivity extends AppCompatActivity {

    ArrayList<All_workrers_model> items_of_all_workers_arr;
    ListView all_workers_list_view;
    All_workers_list_adapter all_workers_list_adapter;
    Database_Connection db;
    String ACTIVITY_STATUTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_workers_activity);

        ACTIVITY_STATUTE="onCreate";

        all_workers_list_view =(ListView)findViewById(R.id.all_workers_activity_list_view);
        db=new Database_Connection(this);
        items_of_all_workers_arr =new ArrayList<>();
        items_of_all_workers_arr =db.getAllWorkersTOListView();

        all_workers_list_adapter =new All_workers_list_adapter(this, items_of_all_workers_arr);
        all_workers_list_view.setAdapter(all_workers_list_adapter);

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


    public class All_workers_list_adapter extends BaseAdapter {

        TextView name;
        ImageView edit;
        Intent go_to_edit_worker_details_activity_intent;
        Bundle go_to_edit_worker_details_activity_bunle;

        ArrayList<All_workrers_model> arr;
        Context context;

        public All_workers_list_adapter(Context context, ArrayList<All_workrers_model> arr) {
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

            All_workrers_model current_items=arr.get(position);



            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=inflater.inflate(R.layout.items_of_all_workers_list_view,null);

            name = (TextView) v.findViewById(R.id.items_of_all_workers_list_view_worker_name_text_view);
            edit=(ImageView)v.findViewById(R.id.items_of_all_workers_edit_customer_details_image_view);


            //________________________________________

            name.setText(current_items.getCustomer_name());

            edit.setOnClickListener(v1 -> {

                if(Shared_Helper.getkey(context,"internal_pass").equalsIgnoreCase("")){
                    showDialog(arr.get(position));
                }else{
                    checkBeforeEditWorker(arr.get(position),"check_password");
                }


            });

            return v;
        }


        private void showDialog(All_workrers_model current_items){
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
                    checkBeforeEditWorker(current_items,"edit_worker");
                }
            });
            sweetAlertDialog.show();
        }

        private void checkBeforeEditWorker(All_workrers_model current_items, String forwaord){
            if(forwaord.equalsIgnoreCase("check_password")){
                go_to_edit_worker_details_activity_intent =new Intent(context, CheckPasswordActivity.class);
                go_to_edit_worker_details_activity_bunle =new Bundle();

            }else if(forwaord.equalsIgnoreCase("edit_worker")){
                go_to_edit_worker_details_activity_intent =new Intent(context, EditWorkerDetailsActivity.class);
                go_to_edit_worker_details_activity_bunle =new Bundle();
            }

            go_to_edit_worker_details_activity_bunle.putString("worker_name",current_items.getCustomer_name());
            go_to_edit_worker_details_activity_bunle.putString("activty","all_workers_activity");

            go_to_edit_worker_details_activity_intent.putExtras(go_to_edit_worker_details_activity_bunle);
            context.startActivity(go_to_edit_worker_details_activity_intent);

        }



    }

}