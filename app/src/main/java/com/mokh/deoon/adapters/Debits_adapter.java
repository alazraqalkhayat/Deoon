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
import com.mokh.deoon.activities.EditDebiteDetailsActivity;
import com.mokh.deoon.activities.SearchForDebentureActivity;
import com.mokh.deoon.models.Depits_model;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Debits_adapter extends RecyclerView.Adapter<Debits_adapter.Debits_view_holder> {

    ArrayList<Depits_model> depitsitem;


    Context context;

    Intent intent;
    Bundle bundle;

    public Debits_adapter(Context context, ArrayList<Depits_model> depitsitem){

        this.context =context;
        this.depitsitem=depitsitem;
    }


    @Override
    public Debits_view_holder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        //View v= LayoutInflater.from(parent.getContext()).inflate(R.layout-v21.debits_items,parent,false);
        View v=LayoutInflater.from(context).inflate(R.layout.debits_items,parent,false);


        return new Debits_view_holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Debits_view_holder holder, int position) {

        Depits_model current_items=depitsitem.get(position);




        holder.name_of_customer.setText(current_items.getCustomer_name());
        holder.number_of_depite.setText(String.valueOf(current_items.getDepit_id()));
        holder.date.setText(current_items.getDate());
        holder.description.setText("    "+current_items.getDescription());
        holder.hand.setText(current_items.getHand());
        holder.name_of_employee.setText(current_items.getEmployee_name());
        holder.deserved_amount.setText(String.valueOf(current_items.getDeserved_amount()));

        holder.edit_debite_details_image_button.setOnClickListener(v -> {
            if(Shared_Helper.getkey(context,"internal_pass").equalsIgnoreCase("")){
                showDialogForCheckPassword(depitsitem.get(position),"edit_debite");
            }else{
                checkBeforeEditDebite(depitsitem.get(position),"check_password");
            }



        });

        holder.delete_debit_image_button.setOnClickListener(v -> {
            if(Shared_Helper.getkey(context,"internal_pass").equalsIgnoreCase("")){
                showDialogForCheckPassword(depitsitem.get(position),"delete_debite");
            }else{
                checkBeforeDeleteDebenture(depitsitem.get(position));
            }

        });


    }

    private void showDialogForCheckPassword(Depits_model current_items, String option){

        new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("كلمة المرور")
                .setContentText("إن بياناتك معرضه للخطر .. قم بإنشاء كلمة مرور لأمان اكبر")
                .setConfirmButton("إنشاء", sweetAlertDialog -> {
                    context.startActivity(new Intent(context, CreateInternalPasswordActivity.class));
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelButton("تخطي", sweetAlertDialog -> {
                    if(option.equalsIgnoreCase("edit_debite")){
                        checkBeforeEditDebite(current_items,"edit_debite");
                    }else if (option.equalsIgnoreCase("delete_debite")){
                        showDialogForDeletedebite(current_items);
                    }
                    sweetAlertDialog.dismissWithAnimation();
                }).show();

}

    private void checkBeforeEditDebite(Depits_model current_items, String forWoard){

        if(forWoard.equalsIgnoreCase("check_password")){
            intent=new Intent(context, CheckPasswordActivity.class);
            bundle=new Bundle();
        }else if(forWoard.equalsIgnoreCase("edit_debite")){
            intent=new Intent(context, EditDebiteDetailsActivity.class);
            bundle=new Bundle();
        }
        bundle.putInt("debite_id",current_items.getDepit_id());
        bundle.putInt("deserved_amount",current_items.getDeserved_amount());
        bundle.putString("customer",current_items.getCustomer_name());
        bundle.putString("description",current_items.getDescription());
        bundle.putString("worker",current_items.getEmployee_name());
        bundle.putString("hand",current_items.getHand());

        bundle.putString("activty","edit_debit_activity");

        intent.putExtras(bundle);
        context.startActivity(intent);


    }

    private void checkBeforeDeleteDebenture(Depits_model current_items){

        intent = new Intent(context, CheckPasswordActivity.class);
        bundle = new Bundle();
        bundle.putInt("debenture_id", current_items.getDepit_id());
        bundle.putString("activty", "delete_debite_activity");
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    private void showDialogForDeletedebite(Depits_model current_items){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("تأكيد الحذف");
        sweetAlertDialog.setContentText("هل أنت متأكد من حذف هذه الفاتوره..!");
        sweetAlertDialog.setConfirmButton("تأكيد", sweetAlertDialog1 -> {
            Database_Connection db=new Database_Connection(context);
            db.deletDebeteById(current_items.getDepit_id());
            changeAlertDialogToSuccessType(sweetAlertDialog1);
        });
        sweetAlertDialog.setCancelButton("إلغاء", sweetAlertDialog12 -> dismissAleartDialog(sweetAlertDialog12));
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

    @Override
    public int getItemCount() {
        return depitsitem.size();
    }


    public class Debits_view_holder extends RecyclerView.ViewHolder{

        public TextView name_of_customer,date,number_of_depite,description,hand,name_of_employee,deserved_amount;
        public ImageButton edit_debite_details_image_button,delete_debit_image_button;

        public Debits_view_holder(@NonNull View itemView) {
            super(itemView);

            name_of_customer =(TextView)itemView.findViewById(R.id.depits_items_customer_name_text_view);
            number_of_depite=(TextView)itemView.findViewById(R.id.depits_items_number_of_depite_text_view);
            date = (TextView)itemView.findViewById(R.id.depits_items_date_of_depite_text_view);
            description=(TextView)itemView.findViewById(R.id.depits_items_description_of_depite_text_view);
            hand=(TextView)itemView.findViewById(R.id.depits_items_hand_of_depite_text_view);
            name_of_employee=(TextView)itemView.findViewById(R.id.depits_items_employee_of_depite_text_view);
            deserved_amount=(TextView)itemView.findViewById(R.id.depits_items_deserved_amount_of_depite_text_view);
            edit_debite_details_image_button=(ImageButton)itemView.findViewById(R.id.depits_items_edit_debite_details_image_button);
            delete_debit_image_button=(ImageButton)itemView.findViewById(R.id.depits_items_delete_debite_image_button);

        }
    }

}
