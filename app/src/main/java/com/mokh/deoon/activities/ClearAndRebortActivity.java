package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.models.Debentures_model;
import com.mokh.deoon.models.Depits_model;
import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class ClearAndRebortActivity extends AppCompatActivity {

    Button clear_button, rebort_button;
    AutoCompleteTextView name_of_customer;

    Map<Integer,String> names_resulte;
    List<String> names_values_list;
    ArrayAdapter names_adapter;

    Database_Connection db;

    ArrayList<Depits_model> debite_items;
    ArrayList<Debentures_model> debentures_items;

    String check_customer_name,date_time;


    PdfDocument pdfDocument;

    Intent intent;
    Bundle bundle;
    int REQUEST_CODE;
    int count_of_debentures,count_of_debites,debite_page_hight,debenture_page_hight
            ,total_of_debite_Y,total_of_debenture_Y,adider_Y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_and_rebort_activity);

        db=new Database_Connection(this);
        date_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a").format(new Date());

        REQUEST_CODE=5;

        iniViews();

        clear_button.setOnClickListener(v -> {
            debite_items = db.getAllDebitsForPDF(name_of_customer.getText().toString());
            debentures_items = db.getAllDebentureForPDF(name_of_customer.getText().toString());

            if(name_of_customer.getText().toString().isEmpty()){
                name_of_customer.setError("يلزم تعبئة هذا الحقل");
            }else{
                check_customer_name = db.getCustomerName(name_of_customer.getText().toString());
                if (check_customer_name.equalsIgnoreCase("")) {
                    Toasty.custom(getBaseContext(),"عذرا.. هذا العميل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                } else {
                    if (debite_items.size() == 0 && debentures_items.size() == 0) {
                        Toasty.custom(getBaseContext(),"لا يوجد بيانات لهذا العميل حاليا",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                    } else {
                        if(Shared_Helper.getkey(this,"internal_pass").equalsIgnoreCase("")){
                            showDialogForCheckPassword(name_of_customer.getText().toString());
                        }else{
                            checkBeforeEditDebite(name_of_customer.getText().toString());
                        }

                    }
                }
            }
        });

        rebort_button.setOnClickListener(v -> {

            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

                debite_items = db.getAllDebitsForPDF(name_of_customer.getText().toString());
                debentures_items = db.getAllDebentureForPDF(name_of_customer.getText().toString());

                if(name_of_customer.getText().toString().isEmpty()){
                    name_of_customer.setError("يلزم تعبئة هذا الحقل");
                }else{
                    check_customer_name = db.getCustomerName(name_of_customer.getText().toString());
                    if (check_customer_name.equalsIgnoreCase("")) {
                        Toasty.custom(getBaseContext(),"عذرا.. هذا العميل غير موجود",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                    } else {
                        if (debite_items.size() == 0 && debentures_items.size() == 0) {
                            Toasty.custom(getBaseContext(),"لا يوجد بيانات لهذا العميل حاليا",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                        } else {
                            sendRebort(name_of_customer.getText().toString());
                        }
                    }
                }

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            }


        });
    }


    private void iniViews(){

        clear_button =(Button) findViewById(R.id.clear_and_rebort_activity_clear_button);
        rebort_button =(Button) findViewById(R.id.clear_and_rebort_activity_rebort_button);
        name_of_customer=(AutoCompleteTextView)findViewById(R.id.clear_and_rebort_customer_name_edit_text);
        getAllCustomers(name_of_customer);


    }

    private void getAllCustomers(AutoCompleteTextView textView){

        names_resulte=db.getAllCustomers();
        names_values_list=new ArrayList<String>(names_resulte.values());
        names_adapter=new ArrayAdapter(this,android.R.layout.select_dialog_item,names_values_list);
        textView.setAdapter(names_adapter);

    }

    private void removeAllDataAlertDialog(final String customer_name) {

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)

                .setTitleText("تأكيد الحذف")
                .setContentText("عند تصفيه الحساب سوف يتم حذف كل الديون و السندات لهذا العميل ...")
                .setConfirmButton("تأكيد", sweetAlertDialog -> {
                    startRemoveAllData(customer_name);
                    changeAlertDialogToSuccessType(sweetAlertDialog,"تمت التصفيه بنجاح");

                })
                .setCancelButton("إلغاء", sweetAlertDialog -> {
                        dismissAleartDialog(sweetAlertDialog);
                }).show();


    }

    private void dismissAleartDialog(SweetAlertDialog sweetAlertDialog){
        if(sweetAlertDialog.getCancelText().equalsIgnoreCase("إلغاء")
                ||sweetAlertDialog.getCancelText().equalsIgnoreCase("حسنأً")){
            sweetAlertDialog.dismissWithAnimation();
        }else{
            sweetAlertDialog.dismissWithAnimation();
//            finish();
            startActivity(new Intent(getBaseContext(),ClearAndRebortActivity.class));
        }
    }

    private void changeAlertDialogToSuccessType(SweetAlertDialog sweetAlertDialog,String message){
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitle(message);
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.setCancelText("تم");
        sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(getResources().getColor(R.color.light_green));


    }

    private void startRemoveAllData(String customer_name) {
        db.deletAllDebitesByName(customer_name);
        db.deletAllDebenturesByName(customer_name);
    }

    private void sendRebort(String customer_name) {
        debite_items = db.getAllDebitsForPDF(customer_name);
        debentures_items = db.getAllDebentureForPDF(customer_name);


        count_of_debentures=db.getCountOfAllDebentures(customer_name);
        count_of_debites=db.getCountOfAllDebits(customer_name);

        limitHightOfDeitePage(debite_items.size());
        limitHightOfDebenturePage(debentures_items.size());


        pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint details_paint = new Paint();
        PdfDocument.PageInfo mypageinfo1 = new PdfDocument.PageInfo.Builder(1200, debite_page_hight, 1).create();
        PdfDocument.Page mypage1 = pdfDocument.startPage(mypageinfo1);
        Canvas canvas = mypage1.getCanvas();

        if(debite_items.size()>0){


            paint.setTextSize(70f);
            canvas.drawText("عميلنا الكريم : " + debite_items.get(0).getCustomer_name(), mypageinfo1.getPageWidth() / 2, 200, paint);


            paint.setTextSize(50f);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("رقم", 1100, 400, paint);
            canvas.drawText("الفاتورة", 1050, 460, paint);
            canvas.drawText("الوصف", 900, 400, paint);
            canvas.drawText("بيد", 650, 400, paint);
            canvas.drawText("من", 450, 400, paint);
            canvas.drawText("التاريخ", 250, 400, paint);
            canvas.drawText("المبلغ", 30, 400, paint);

            canvas.drawLine(10, 300, 1200, 300, paint);
            canvas.drawLine(10, 500, 1200, 500, paint);

            details_paint.setTextSize(30f);
            details_paint.setTextAlign(Paint.Align.LEFT);


            int startYPosition = 600;
            for (Depits_model items : debite_items) {

                paint.setTextSize(50f);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(String.valueOf(items.getDepit_id()), 1100, startYPosition, details_paint);
                canvas.drawText(items.getDescription(), 950, startYPosition, details_paint);
                canvas.drawText(items.getHand(), 630, startYPosition, details_paint);
                canvas.drawText(items.getEmployee_name(), 450, startYPosition, details_paint);
                canvas.drawText(items.getDate(), 140, startYPosition, details_paint);
                canvas.drawText(String.valueOf(items.getDeserved_amount()), 30, startYPosition, details_paint);

                canvas.drawLine(10, startYPosition + 20, 1200, startYPosition + 20, paint);


                startYPosition += 100;
            }


            int final_line = startYPosition - 80;
            canvas.drawLine(10, 300, 10, final_line, paint);
            canvas.drawLine(1190, 300, 1190, final_line, paint);
            canvas.drawLine(1040, 300, 1040, final_line, paint);
            canvas.drawLine(720, 300, 720, final_line, paint);
            canvas.drawLine(520, 300, 520, final_line, paint);
            canvas.drawLine(380, 300, 380, final_line, paint);
            canvas.drawLine(130, 300, 130, final_line, paint);


        }
        pdfDocument.finishPage(mypage1);


        PdfDocument.PageInfo mypageinfo2 = new PdfDocument.PageInfo.Builder(1200, debenture_page_hight, 2).create();
        PdfDocument.Page mypage2 = pdfDocument.startPage(mypageinfo2);
        Canvas canvas2 = mypage2.getCanvas();


        if(debentures_items.size()>0){


            paint.setTextSize(70f);
            canvas2.drawText("عميلنا الكريم " + debentures_items.get(0).getCustomer_name(), mypageinfo1.getPageWidth() / 2, 200, paint);

            paint.setTextSize(50f);
            paint.setTextAlign(Paint.Align.LEFT);

            canvas2.drawText("رقم السند", 1000, 400, paint);
            paint.setTextAlign(Paint.Align.CENTER);

            canvas2.drawText("المستلم", 800, 400, paint);
            canvas2.drawText("التاريخ", 500, 400, paint);
            canvas2.drawText("المبـــلغ", 160, 400, paint);

            canvas2.drawLine(10, 300, 1200, 300, paint);
            canvas2.drawLine(10, 500, 1200, 500, paint);


            int startYPosition2 = 600;

            if (debentures_items.size() == 0) {

                paint.setTextSize(50f);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas2.drawText("0", 1100, startYPosition2, details_paint);
                canvas2.drawText("لا يوجد", 880, startYPosition2, details_paint);
                canvas2.drawText("لا يوجد", 530, startYPosition2, details_paint);
                canvas2.drawText("0", 150, startYPosition2, details_paint);

                canvas2.drawLine(10, startYPosition2 + 20, 1200, startYPosition2 + 20, paint);


            } else {

                for (Debentures_model debentures_model : debentures_items) {

                    paint.setTextSize(50f);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas2.drawText(String.valueOf(debentures_model.getDebenture_id()), 1100, startYPosition2, details_paint);
                    canvas2.drawText(debentures_model.getEmployee_name(), 880, startYPosition2, details_paint);
                    canvas2.drawText(debentures_model.getDate(), 380, startYPosition2, details_paint);
                    canvas2.drawText(String.valueOf(debentures_model.getMoney_paied()), 150, startYPosition2, details_paint);

                    canvas2.drawLine(10, startYPosition2 + 20, 1200, startYPosition2 + 20, paint);


                    startYPosition2 += 100;
                }


            }


            int final_line2 = startYPosition2 - 80;
            canvas2.drawLine(10, 300, 10, final_line2, paint);

            canvas2.drawLine(1190, 300, 1190, final_line2, paint);

            canvas2.drawLine(990, 300, 990, final_line2, paint);

            canvas2.drawLine(640, 300, 640, final_line2, paint);

            canvas2.drawLine(300, 300, 300, final_line2, paint);



        }

        pdfDocument.finishPage(mypage2);

        int totla_of_debites = db.getSumOfDebits(customer_name);
        int totla_of_debentures = db.getSumOfDebenture(customer_name);
        int almotabaqi = totla_of_debites - totla_of_debentures;

        PdfDocument.PageInfo mypageinfo3 = new PdfDocument.PageInfo.Builder(1200, 1000, 3).create();
        PdfDocument.Page mypage3 = pdfDocument.startPage(mypageinfo3);
        Canvas canvas3 = mypage3.getCanvas();

        paint.setTextAlign(Paint.Align.CENTER);
        canvas3.drawText("إجمالي الديون : " + String.valueOf(totla_of_debites), mypageinfo3.getPageWidth() / 2, 100, paint);
        canvas3.drawText("إجمالي المدفوعات : " + String.valueOf(totla_of_debentures), mypageinfo3.getPageWidth() / 2, 200, paint);
        canvas3.drawText("المتبقي : " + String.valueOf(almotabaqi), mypageinfo3.getPageWidth() / 2, 300, paint);


        pdfDocument.finishPage(mypage3);



        File file=new File(Environment.getExternalStorageDirectory(),"/deoon");
        File file1;
        File file2;
        if(file.exists()){
            file1=new File(Environment.getExternalStorageDirectory(),"/deoon/"+customer_name+".pdf");


            if(file1.exists()){
                startAlertDialogForCreatingThePDF_File(customer_name);

                //Toast.makeText(this, "wxist", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    pdfDocument.writeTo(new FileOutputStream(file1));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                pdfDocument.close();
                Toasty.custom(getBaseContext(),"تمت العمليه بنجاح "+Environment.getExternalStorageDirectory()+"/deoon",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();

            }

        }else{
            file.mkdir();
            file1=new File(Environment.getExternalStorageDirectory(),"/deoon/"+customer_name+".pdf");
            try {
                pdfDocument.writeTo(new FileOutputStream(file1));

            } catch (IOException e) {
                e.printStackTrace();
            }


            Toasty.custom(getBaseContext(),"تمت العمليه بنجاح "+Environment.getExternalStorageDirectory()+"/deoon",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();



        }


    }

    public void startAlertDialogForCreatingThePDF_File(final String c_name) {


        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)

                .setTitleText("تأكيد الحذف")
                .setContentText("عميلنا الكريم... هناك كشف حساب سابق لهذا العميل")
                .setConfirmButton("جديد", sweetAlertDialog -> {
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    File file1=new File(Environment.getExternalStorageDirectory(),"/deoon/"+c_name+date_time+".pdf");
                    try {
                        pdfDocument.writeTo(new FileOutputStream(file1));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pdfDocument.close();
//                    changeAlertDialogToSuccessType(sweetAlertDialog,"تمت العمليه بنجاح");
                    sweetAlertDialog.dismissWithAnimation();
                    Toasty.custom(getBaseContext(),"تمت العمليه بنجاح "+Environment.getExternalStorageDirectory()+"/deoon",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();


                })
                .setCancelButton("إستبدال", sweetAlertDialog -> {
                    File file1=new File(Environment.getExternalStorageDirectory(),"/deoon/"+c_name+".pdf");
                    try {
                        pdfDocument.writeTo(new FileOutputStream(file1));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pdfDocument.close();
//                    changeAlertDialogToSuccessType(sweetAlertDialog,"تمت العمليه بنجاح");
                    sweetAlertDialog.dismissWithAnimation();
                    Toasty.custom(getBaseContext(),"تمت العمليه بنجاح "+Environment.getExternalStorageDirectory()+"/deoon",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();

                }).show();
    }

    private void showDialogForCheckPassword(String customer_name){

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("كلمة المرور")
                .setContentText("إن بياناتك معرضه للخطر .. قم بإنشاء كلمة مرور لأمان اكبر")
                .setConfirmButton("إنشاء", sweetAlertDialog -> {
                    startActivity(new Intent(this, CreateInternalPasswordActivity.class));
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelButton("تخطي", sweetAlertDialog -> {
                    removeAllDataAlertDialog(customer_name);

                    sweetAlertDialog.dismissWithAnimation();
                }).show();

    }

    private void checkBeforeEditDebite(String customer_name){

        intent=new Intent(this, CheckPasswordActivity.class);
        bundle=new Bundle();

        bundle.putString("customer_name",customer_name);

        bundle.putString("activty","clear");

        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.just_back, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if(id==R.id.just_back_menu_header_back){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void limitHightOfDeitePage(int count){
        debite_page_hight=count*230;

    /*    System.out.println(count);
        if(count>=0 && count<=13){
            debite_page_hight=3000;
        }else if(count>13 && count<=26){
            debite_page_hight=6000;
        }else if(count>26 && count<=39){
            debite_page_hight=9000;
        }else if(count>39 && count<=52){
            debite_page_hight=12000;
        }else if(count>52 && count<=65){
            debite_page_hight=15000;
        }else if(count>65 && count<=78){
            debite_page_hight=18000;
        }else if(count>78 && count<=91){
            debite_page_hight=21000;
        }else if(count>91 && count<=104){
            debite_page_hight=24000;
        }else if(count>104 && count<=117){
            debite_page_hight=27000;
        }else if(count>117 && count<=130){
            debite_page_hight=300000;
        }else {
            debite_page_hight=300000;

        }
*/
    }

    private void limitHightOfDebenturePage(int count){
        debenture_page_hight=count*230;
      /*  if(count>=0 && count<=13){
            debenture_page_hight=3000;
        }else if(count>13 && count<=26){
            debenture_page_hight=6000;
        }else if(count>26 && count<=39){
            debenture_page_hight=9000;
        }else if(count>39 && count<=52){
            debenture_page_hight=12000;
        }else if(count>52 && count<=65){
            debenture_page_hight=15000;
        }else if(count>65 && count<=78){
            debenture_page_hight=18000;
        }else if(count>78 && count<=91){
            debenture_page_hight=21000;
        }else if(count>91 && count<=104){
            debenture_page_hight=24000;
        }else if(count>104 && count<=117){
            debenture_page_hight=27000;
        }else if(count>117 && count<=130){
            debenture_page_hight=300000;
        }else {
            debenture_page_hight=300000;
        }

*/
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}