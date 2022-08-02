package com.mokh.deoon.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;
import com.mokh.deoon.activities.CheckPasswordActivity;
import com.mokh.deoon.activities.CreateInternalPasswordActivity;
import com.mokh.deoon.activities.EditDebentureDetailsActivity;
import com.mokh.deoon.activities.SearchForDebentureActivity;
import com.mokh.deoon.models.Debentures_model;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Debentures_adapter extends RecyclerView.Adapter<Debentures_adapter.Debentures_view_holder> {

    ArrayList<Debentures_model> debentures = new ArrayList<Debentures_model>();


    Context context;

    Intent intent;
    Bundle bundle;

    Database_Connection db;

    public Debentures_adapter(Context context, ArrayList<Debentures_model> debentures){

        this.context=context;
        this.debentures=debentures;
    }

    @NonNull
    @Override
    public Debentures_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.debentures_items,parent,false);

        return new Debentures_view_holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Debentures_view_holder holder, int position) {

        Debentures_model current_items=debentures.get(position);



        holder.name_of_customer.setText(current_items.getCustomer_name());
        holder.number_of_debenture.setText(String.valueOf(current_items.getDebenture_id()));
        holder.date.setText(current_items.getDate());
        holder.name_of_employee.setText(current_items.getEmployee_name());
        holder.paied_money.setText(String.valueOf(current_items.getMoney_paied()));

        holder.edit_debenture_details_image_button.setOnClickListener(v -> {
            if(Shared_Helper.getkey(context,"internal_pass").equalsIgnoreCase("")){
                showDialogForCheckPassword(debentures.get(position),"edit");
            }else{
                checkBeforeEditDebenture(debentures.get(position),"check_password");
            }

        });

        holder.delete_debenture_image_button.setOnClickListener(v -> {
            if(Shared_Helper.getkey(context,"internal_pass").equalsIgnoreCase("")){
                showDialogForCheckPassword(debentures.get(position),"delete");
            }else{
                checkBeforeDeleteDebenture(debentures.get(position));
            }
        });


    }

    private void showDialogForCheckPassword(Debentures_model current_items, String option){

        new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("كلمة المرور")
                .setContentText("إن بياناتك معرضه للخطر .. قم بإنشاء كلمة مرور لأمان اكبر")
                .setConfirmButton("إنشاء", sweetAlertDialog -> {
                    context.startActivity(new Intent(context, CreateInternalPasswordActivity.class));
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelButton("تخطي", sweetAlertDialog -> {
                    if(option.equalsIgnoreCase("edit")){
                        checkBeforeEditDebenture(current_items,"edit_debenture");

                    }else if(option.equalsIgnoreCase("delete")){
                        showDialogForDeletedebenture(current_items);
                    }

                    sweetAlertDialog.dismissWithAnimation();
                }).show();

    }

    private void checkBeforeEditDebenture(Debentures_model current_items, String forWoard){

        if(forWoard.equalsIgnoreCase("check_password")){
            intent=new Intent(context, CheckPasswordActivity.class);
            bundle=new Bundle();
        }else if(forWoard.equalsIgnoreCase("edit_debenture")){
            intent=new Intent(context, EditDebentureDetailsActivity.class);
            bundle=new Bundle();
        }
        bundle.putInt("debenture_id",current_items.getDebenture_id());
        bundle.putInt("money_paied",current_items.getMoney_paied());
        bundle.putString("customer_name",current_items.getCustomer_name());
        bundle.putString("worker_name",current_items.getEmployee_name());
        bundle.putString("activty","edit_debenture_activity");

        intent.putExtras(bundle);
        context.startActivity(intent);


    }

    private void checkBeforeDeleteDebenture(Debentures_model current_items){


            intent = new Intent(context, CheckPasswordActivity.class);
            bundle = new Bundle();
            bundle.putInt("debenture_id", current_items.getDebenture_id());
            bundle.putString("activty", "delete_debenture_activity");
            intent.putExtras(bundle);
            context.startActivity(intent);

    }

    private void showDialogForDeletedebenture(Debentures_model current_items){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("تأكيد الحذف");
        sweetAlertDialog.setContentText("هل أنت متأكد من حذف هذا السند..!");
        sweetAlertDialog.setConfirmButton("تأكيد", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
               db=new Database_Connection(context);
               db.deletDebenturesById(current_items.getDebenture_id());
               changeAlertDialogToSuccessType(sweetAlertDialog);
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
            context.startActivity(new Intent(context, SearchForDebentureActivity.class));
        }
    }

    private void changeAlertDialogToSuccessType(SweetAlertDialog sweetAlertDialog){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitle("تمت عملية الحذف بنجاح");
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.setCancelText("تم");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(context.getResources().getColor(R.color.light_green));

    }

    private void changeAlertDialogToErrorType(SweetAlertDialog sweetAlertDialog,int adider){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitle("فشلت عمليه الحذف");
        sweetAlertDialog.setContentText("لم يتم حذف السند.. حصل خطأ غير متوفع ..!");
        sweetAlertDialog.setCancelText("حسنأً");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(context.getResources().getColor(R.color.red_btn_bg_color));

    }


    @Override
    public int getItemCount() {
        return debentures.size();
    }


    public class Debentures_view_holder extends RecyclerView.ViewHolder{


        public TextView name_of_customer,date,number_of_debenture,paied_money,name_of_employee;
        public ImageButton edit_debenture_details_image_button,delete_debenture_image_button;

        public Debentures_view_holder(@NonNull View itemView) {
            super(itemView);
            name_of_customer = (TextView) itemView.findViewById(R.id.debentures_items_customer_name_text_view);
            number_of_debenture=(TextView)itemView.findViewById(R.id.debentures_items_number_of_debentures_text_view);
            date = (TextView) itemView.findViewById(R.id.debentures_items_date_of_debentures_text_view);
            paied_money = (TextView) itemView.findViewById(R.id.debentures_items_paied_money_of_debentures_text_view);
            name_of_employee=(TextView)itemView.findViewById(R.id.debentures_items_employee_of_debentures_text_view);
            edit_debenture_details_image_button=(ImageButton)itemView.findViewById(R.id.depits_items_edit_debite_details_image_button);
            delete_debenture_image_button=(ImageButton)itemView.findViewById(R.id.depits_items_delete_debite_image_button);


        }
    }

}
