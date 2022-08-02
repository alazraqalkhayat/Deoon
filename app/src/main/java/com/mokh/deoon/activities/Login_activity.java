package com.mokh.deoon.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

import java.util.HashMap;
import java.util.Map;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class Login_activity extends AppCompatActivity {

    EditText user_name,password;
    TextView forgate_password,signup;
    Button login,free;
    String get_user_name_from_sh;

    ImageView login_image_view;

    //encoding Strings
    String v1="co";
    String v2="m.mo";
    String v3="kh.d";
    String v4="eoon";

    DatabaseReference databaseReference;

    SweetAlertDialog reRegesterAlertDialog;


    //how to use validation patern matcher in android studio

    ACProgressFlower login_progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if(getPackageName().compareTo(v1+v2+v3+v4)!=0){
            String error=null;
            error.getBytes();
        }


        get_user_name_from_sh= Shared_Helper.getkey(this,"user_name");

        if(!get_user_name_from_sh.equalsIgnoreCase("")) {

            if(get_user_name_from_sh.equalsIgnoreCase("time_finish")){
                startAlertDialogForReRegestr();
            }else{
                startActivity(new Intent(this,HomeActivity.class));
                this.finish();
            }

        }

        initViews();

        initProgressDialog();

        forgate_password.setOnClickListener(view -> {
            Toasty.custom(getBaseContext(),"يرجى التواصل مع مطور التطبيق لإستعاده كلمه المرور الخاصه بك..!",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
            startActivity(new Intent(Login_activity.this, AboutUsActivity.class));

        });

        signup.setOnClickListener(view ->
                startActivity(new Intent(Login_activity.this,SubscribeActivity.class)
        ));


        free.setOnClickListener(view -> {
            Shared_Helper.putKey(Login_activity.this,"user_name","free");
            startActivity(new Intent(Login_activity.this,HomeActivity.class));
            finish();
        });

        login.setOnClickListener(view -> {
            login();
        });


    }

    private void initViews(){
        login_image_view=(ImageView)findViewById(R.id.login_activity_image_view);
        Animation instagram_animation= AnimationUtils.loadAnimation(this,R.anim.right_to_left);
        login_image_view.startAnimation(instagram_animation);

        user_name=(EditText) findViewById(R.id.login_activity_user_name_edit_text);
        password=(EditText) findViewById(R.id.login_activity_password_edit_text);

        forgate_password=(TextView)findViewById(R.id.login_activity_forgate_password);
        initTextViewsColor(forgate_password,21,31,"هل نسيت كلمه مرورك ؟ إنقر هنا..");

        free=(Button) findViewById(R.id.login_activity_freebutton);
        login=(Button)findViewById(R.id.login_in_button);

        signup=(TextView)findViewById(R.id.login_activity_signup);
        initTextViewsColor(signup,16,28,"لا تمتلك حساب ؟ إشترك الآن..");
    }
    //
    private void initTextViewsColor(TextView textView,int start,int end,String message){
        String sinup_string=message;
        SpannableString spannableString=new SpannableString(sinup_string);
        ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(getResources().getColor((R.color.golden)));
        spannableString.setSpan(foregroundColorSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);

    }

    private void initProgressDialog(){
        login_progress_dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .text("جاري تسجيل الدخول")
                .themeColor(this.getResources().getColor(R.color.light_green))
                .fadeColor(this.getResources().getColor(R.color.golden))
                .textSize(30)
                .bgCornerRadius(20)
                .build();
    }

    private void login(){
        if(isConnected()){
            if(user_name.getText().toString().isEmpty()){
                user_name.setError("يلزم تعبئه هذا الحقل");

            }else {
                if(password.getText().toString().isEmpty()){
                    password.setError("يلزم تعبئه هذا الحقل");
                }else{
                    login_progress_dialog.show();
                    startFireBaseLogin(user_name.getText().toString(),password.getText().toString());
                }
            }
        }else{
            Toasty.custom(getBaseContext(),"لا يوجد اتصال بالبيانات .. تأكد من إتصالك بالانترنت",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
        }

    }
    private void startFireBaseLogin(String user_name,String password){


        databaseReference= FirebaseDatabase.getInstance().getReference().child("all_accounts").child(user_name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String base_user_name=(String)dataSnapshot.child("user_name").getValue();
                String base_password=(String)dataSnapshot.child("password").getValue();
                Long availability=(Long) dataSnapshot.child("availability").getValue();

                if(base_password.equalsIgnoreCase("java.util.Random")){
                        try {
                            if(login_progress_dialog.isShowing()){
                                login_progress_dialog.dismiss();
                            }
                        }catch (Exception e){}
                    startAlertDialogForReRegestr();
                }else{
                    if(user_name.equalsIgnoreCase(base_user_name)){
                        if(password.equalsIgnoreCase(base_password)){
                            if(availability==0){

                                Toasty.custom(getBaseContext(),"تم تسجيل الدخول بنجاح",R.drawable.true_icon,R.color.light_green,Toasty.LENGTH_LONG,true,true).show();

                                if(login_progress_dialog.isShowing()){
                                    login_progress_dialog.dismiss();
                                }
                                Shared_Helper.putKey(getBaseContext(),"user_name",user_name);
                                startActivity(new Intent(getBaseContext(),HomeActivity.class));

                                Map<String,Object> map=new HashMap<>();
                                map.put("availability",1);
                                databaseReference.updateChildren(map);

                                finish();

                            }else{
                                Toasty.custom(getBaseContext(),"هذا الحساب مستخدم في جهاز آخر حاليا",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                                if(login_progress_dialog.isShowing()){
                                    login_progress_dialog.dismiss();
                                }

                            }

                        }else{
                            Toasty.custom(getBaseContext(),"تأكـد من كتابه كلمة المرور بشكل صحيح",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                            if(login_progress_dialog.isShowing()){
                                login_progress_dialog.dismiss();
                            }
                        }
                    }else{
                        Toasty.custom(getBaseContext(),"تأكـد من كتابه إسم المستخدم بشكل صحيح",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                        if(login_progress_dialog.isShowing()){
                            login_progress_dialog.dismiss();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                try {
                    if(login_progress_dialog.isShowing()){
                        login_progress_dialog.dismiss();
                        Toasty.custom(getBaseContext(),"حدث خطأ غير متوقع .. تأكد من إتصالك بالإنترنت ..!",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                    }
                }catch (Exception e){}

            }
        });

/*        my_firebase_class=new My_firebase_class(this,user_name,password);
        items_of_fire_base_data_bases=new ArrayList<>();

        my_firebase_class.checkUserNameAndPasswordFromFirebase();*/

//        availability

    }

    private void startAlertDialogForReRegestr(){

        reRegesterAlertDialog=new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        reRegesterAlertDialog.setContentText("عميلنا الكريم... لقد انتهت فتره الصلاحيه المحدده لك وتم رفع أخر نسخه لبيانات ولا زالت محفوظه لدينا يرجى تجديد الاشتراك لمتابعه الإستخدام");
        reRegesterAlertDialog.setTitle("تجديد الاشتراك");
        reRegesterAlertDialog.hideConfirmButton();
        reRegesterAlertDialog.setCancelable(false);

        reRegesterAlertDialog.setCancelButton("حسنــاً", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Shared_Helper.putKey(Login_activity.this,"user_name","");
                sweetAlertDialog.dismissWithAnimation();
            }
        });

        reRegesterAlertDialog.show();

    }

    public boolean isConnected(){
        ConnectivityManager cm=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();

        return (networkInfo!=null && networkInfo.isConnected());
    }


}



