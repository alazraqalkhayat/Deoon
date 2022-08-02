package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

import es.dmoral.toasty.Toasty;

public class EditInternalPassActivity extends AppCompatActivity {

    EditText last_pass_edit_text, password_edit_text,confirm_pass_edit_text;
    LinearLayout last_password_linear_layout,password_linear_layouy,confirm_password_linear_layout;
    ImageView edit_image_view;
    TextView forgate_pass_text_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_internal_pass_activity);


        init();



        forgate_pass_text_view.setOnClickListener(v ->
                startActivity(new Intent(getBaseContext(), ProtictionQquestionaActivity.class)
        ));

        last_pass_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equalsIgnoreCase(Shared_Helper.getkey(getBaseContext(),"internal_pass"))){
                    last_pass_edit_text.setEnabled(false);
                    password_linear_layouy.setVisibility(View.VISIBLE);
                    confirm_password_linear_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edit_image_view.setOnClickListener(v -> {

            if(last_pass_edit_text.getText().toString().isEmpty()){
                last_pass_edit_text.setError("يلزم تعبئه هذا الحقل");

            }else{
                if(!last_pass_edit_text.getText().toString().equalsIgnoreCase(Shared_Helper.getkey(getBaseContext(),"internal_pass"))){
                    Toasty.custom(getBaseContext(),"تأكد من كتابة كلمة المرور السابقه بشكل صحيح..!",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                }else{

                    if(password_edit_text.getText().toString().isEmpty()){
                        password_edit_text.setError("يلزم تعبئه هذا الحقل");
                    }else {
                        if (confirm_pass_edit_text.getText().toString().isEmpty()){
                            confirm_pass_edit_text.setError("يلزم تعبئه هذا الحقل");

                        }else{
                            if(password_edit_text.getText().toString().length()<6){
                                password_edit_text.setError("كلمة المرور افل من 6 حروف");
                            }else{
                                if(!password_edit_text.getText().toString().equalsIgnoreCase(confirm_pass_edit_text.getText().toString())){
                                    Toasty.custom(getBaseContext(),"تأكد من كتابة كلمة المرور بشكل صحيح..!",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                                }else{
                                    Shared_Helper.putKey(getBaseContext(),"internal_pass", password_edit_text.getText().toString());
                                    finish();
                                }
                            }

                        }
                    }
                }
            }




        });

    }

    private void init(){

        last_pass_edit_text=(EditText) findViewById(R.id.edit_internal_pass_activity_last_password_edit_text);
        password_edit_text =(EditText) findViewById(R.id.edit_internal_pass_activity_password_edit_text);
        confirm_pass_edit_text=(EditText) findViewById(R.id.edit_internal_pass_activity_confirm_password_edit_text);

        last_password_linear_layout=(LinearLayout)findViewById(R.id.edit_internal_pass_activity_last_password_linear_layout);
        password_linear_layouy=(LinearLayout)findViewById(R.id.edit_internal_pass_activity_password_linear_layout);
        password_linear_layouy.setVisibility(View.GONE);
        confirm_password_linear_layout=(LinearLayout)findViewById(R.id.edit_internal_pass_activity_confirm_password_linear_layout);
        confirm_password_linear_layout.setVisibility(View.GONE);


        forgate_pass_text_view=(TextView)findViewById(R.id.edit_internal_pass_activity_forgate_pass_text_view);
        edit_image_view =(ImageView) findViewById(R.id.edit_internal_pass_activity_edit_image_view);


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