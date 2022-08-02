package com.mokh.deoon.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mokh.deoon.helper.Database_Connection;
import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class DataActivity extends AppCompatActivity {

    Button upload_data_button, download_data_button;

    String check_employee_name,check_customer_name;
    String getUserNameFromSharedPereference,getUserNameFromSharedPereference2;
    String path;

    Database_Connection db;
    StorageReference mStorageRef;
    DatabaseReference databaseReference;

    ACProgressFlower date_progress_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_activity);

        initViews();

        getUserNameFromSharedPereference= Shared_Helper.getkey(this,getUserNameFromSharedPereference+".db");
        getUserNameFromSharedPereference2=Shared_Helper.getkey(this,"user_name");

        db=new Database_Connection(getBaseContext());
        path="/data/data/com.mokh.deoon/databases/azraq.db";

        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("all_accounts").child(Shared_Helper.getkey(this,"user_name"));



        upload_data_button.setOnClickListener(v -> {
                if(Shared_Helper.getkey(this,"user_name").equalsIgnoreCase("free")){
                    Toasty.custom(getBaseContext(),"اشترك بالخدمه لكي تستطيع رفع البيانات",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                    startActivity(new Intent(this, SubscribeActivity.class));

                }else{
                    if(isConnected()){
                        uploadDataAlertDialog();
                    }else{
                        Toasty.custom(getBaseContext(),"لا يوجد اتصال بالبيانات .. تأكد من إتصالك بالانترنت",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                    }
                }

        });

        download_data_button.setOnClickListener(v -> {

            if(Shared_Helper.getkey(this,"user_name").equalsIgnoreCase("free")){
                Toasty.custom(getBaseContext(),"اشترك بالخدمه لكي تستطيع إسترحاع البيانات",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                startActivity(new Intent(this, SubscribeActivity.class));

            }else{
                if(isConnected()){
                    downloadDataAlertDialog();
                }else{
                    Toasty.custom(getBaseContext(),"لا يوجد اتصال بالبيانات .. تأكد من إتصالك بالانترنت",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                }
            }
        });


    }

    private void initViews(){

        upload_data_button =(Button)findViewById(R.id.data_activity_export_data_button);
        download_data_button =(Button) findViewById(R.id.data_activity_import_data_button);

    }

    private void initProgressDialog(String message,int textSize){
        date_progress_dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .text(message)
                .themeColor(this.getResources().getColor(R.color.light_green))
                .fadeColor(this.getResources().getColor(R.color.golden))
                .textSize(textSize)
                .bgCornerRadius(15)
                .build();
    }

    public void checkDate(String date){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long base_date=(long)dataSnapshot.child("date").getValue();

                if(Integer.valueOf((int) base_date) < Integer.valueOf(date)){

                    try {
                        uploadFileToPhone();
                    }catch (Exception e){}
                    uploadFileToServer();

                    startChangePassword();

                    Shared_Helper.putKey(DataActivity.this,"user_name","time_finish");
                    startActivity(new Intent(DataActivity.this, Login_activity.class));

                }else{

                    try {
                        uploadFileToPhone();
                    } catch (Exception e) {
                    }
                    uploadFileToServer();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void uploadFileToPhone() throws IOException {

        final String inFileName = path;

        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()+"/database_copy.db";
        //String outFileName = Environment.getExternalStorageState(new File("/moaid/com.mokh.simpleaccountingsystem/databases/foo.db"));

        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        // Close the streams
        output.flush();
        output.close();
        fis.close();

    }

    private void uploadFileToServer(){


        Uri file = Uri.fromFile(new File(path));

        String chiled="deoon/"+getUserNameFromSharedPereference2+".db";
        StorageReference riversRef = mStorageRef.child(chiled);

        riversRef.putFile(file)
                .addOnSuccessListener(taskSnapshot -> {
                    Toasty.custom(getBaseContext(),"تم رفع البيانات بنجاح",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();
                    date_progress_dialog.dismiss();
//                    startActivity(new Intent(DataActivity.this,HomeActivity.class));
                    uploadFileToRecavory();
                })
                .addOnFailureListener(exception -> {
                    Toasty.custom(getBaseContext(),"لم يتم رفع البيانات حصل خطاء غير متوقع .. تأكد من اتصالك بالانترنت",R.drawable.warning_24dp,R.color.red,Toasty.LENGTH_LONG,true,true).show();
                    date_progress_dialog.dismiss();

                });
    }


    /**
     * to uploade the DB to the recavory file
     */
    private void uploadFileToRecavory(){

        initProgressDialog("جاري رفع البيانات الى النسخه الاحتياطيه",20);
        date_progress_dialog.show();

        Uri file = Uri.fromFile(new File(path));
        String date_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a").format(new Date());

        String chiled="deoonRecavory/"+getUserNameFromSharedPereference2+"-"+date_time+".db";
        StorageReference riversRef = mStorageRef.child(chiled);

        riversRef.putFile(file)
                .addOnSuccessListener(taskSnapshot -> {
                    Toasty.custom(getBaseContext(),"تم رفع البيانات الى النسخه الإحتياطيه بنجاح",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();
                    date_progress_dialog.dismiss();
                    startActivity(new Intent(DataActivity.this,HomeActivity.class));
                })
                .addOnFailureListener(exception -> {
                    Toasty.custom(getBaseContext(),"لم يتم رفع البيانات حصل خطاء غير متوقع .. تأكد من اتصالك بالانترنت",R.drawable.warning_24dp,R.color.red,Toasty.LENGTH_LONG,true,true).show();
                    date_progress_dialog.dismiss();

                });
    }

    private void startChangePassword(){

        String s= Random.class.getName();
        Map<String,Object> hash_map=new HashMap<>();
        hash_map.put("password",s);
        databaseReference.updateChildren(hash_map);


    }

    private void downloadFileFromServer() {

        String chiled="deoon/"+getUserNameFromSharedPereference2+".db";
        StorageReference riversRef = mStorageRef.child(chiled);

        File file1=new File("/data/data/com.mokh.deoon/databases","azraq.db");

        riversRef.getFile(file1).addOnSuccessListener(taskSnapshot -> {
            Toasty.custom(getBaseContext(),"تم إسترجاع البيانات بنجاح",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();
            date_progress_dialog.dismiss();
            startActivity(new Intent(DataActivity.this, HomeActivity.class));
        }).addOnFailureListener(e -> {
            Toasty.custom(getBaseContext(),"لم يتم إسترجاع البيانات حصل خطاء غير متوقع .. تأكد من إتصالك بالإنترنت",R.drawable.warning_24dp,R.color.red,Toasty.LENGTH_LONG,true,true).show();
            date_progress_dialog.dismiss();
        }).addOnSuccessListener(taskSnapshot -> {
            Toasty.custom(getBaseContext(),"تم إسترجاع البيانات بنجاح",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();
            date_progress_dialog.dismiss();
            startActivity(new Intent(DataActivity.this, HomeActivity.class));

        });

    }

    private void uploadDataAlertDialog(){

        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)

                .setTitleText("تـحــذيــر")
                .setContentText("عميلنا الكريم... سوف يتم إستبدال اخر نسخه للبيانات بهذه النسخه وعمل نسخه احتياطيه لها في حال اردت الرجوع اليها ")
                .setConfirmButton("تأكيد", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();

                    initProgressDialog("جاري رفع البيانات",30);
                    date_progress_dialog.show();
                    String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    checkDate(date);
                })
                .setCancelButton("إلغاء", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();

                }).show();

    }

    private void downloadDataAlertDialog() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)

                .setTitleText("تـحــذيــر")
                .setContentText("عميلنا الكريم... عند استرجاعك للبيانات ستفقد البيانات الموجوده حالياً وسيتم استرجاع أخر نسخه قمت برفعها الى السيرفر")
                .setConfirmButton("تأكيد", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    initProgressDialog("جاري إسترجاع البيانات",30);
                    date_progress_dialog.show();
                    downloadFileFromServer();

                })
                .setCancelButton("إلغاء", sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                }).show();
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


    public boolean isConnected(){
        ConnectivityManager cm=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();

        return (networkInfo!=null && networkInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}