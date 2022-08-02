package com.mokh.deoon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mokh.deoon.R;
import com.mokh.deoon.models.SearchMethodeAndInternalPasswordModel;
import com.mokh.deoon.adapters.SearchMethodeAndInternalPasswordItemsAdapter;
import com.mokh.deoon.helper.Shared_Helper;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CreateInternalPasswordActivity extends AppCompatActivity {


    EditText pass_edit_text,confirm_pass_edit_text, answer_protiction_question_edit_text;
    Spinner protiction_questions_spinner;
    ImageView save_image_view;


    ArrayList<SearchMethodeAndInternalPasswordModel> spinnerArray;
    SearchMethodeAndInternalPasswordItemsAdapter adapter;

    String protiction_qiestion_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_internal_password_activity);


        initViews();

        initSpinner();


        if (protiction_questions_spinner!=null){

            protiction_questions_spinner.setAdapter(adapter);
            protiction_questions_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    SearchMethodeAndInternalPasswordModel items=(SearchMethodeAndInternalPasswordModel)adapterView.getSelectedItem();
                    //Toast.makeText(Sp_login_activity.this, items.getName_of_shop(), Toast.LENGTH_SHORT).show();
                    protiction_qiestion_string=items.getItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


        save_image_view.setOnClickListener(v -> {

            if(pass_edit_text.getText().toString().isEmpty()){
                pass_edit_text.setError("يلزم تعبئه هذا الحقل");
            }else{
                if(confirm_pass_edit_text.getText().toString().isEmpty()){
                    confirm_pass_edit_text.setError("يلزم تعبئه هذا الحقل");
                }else{
                    if(answer_protiction_question_edit_text.getText().toString().isEmpty()){
                        answer_protiction_question_edit_text.setError("يلزم تعبئه هذا الحقل");
                    }else{
                        if(pass_edit_text.getText().toString().length()<6){
                            pass_edit_text.setError("كلمة المرور افل من 6 حروف");
                        }else{
                            if(!pass_edit_text.getText().toString().equalsIgnoreCase(confirm_pass_edit_text.getText().toString())){
                                Toasty.custom(getBaseContext(),"تأكد من كتابة كلمة المرور بشكل صحيح..!",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                            }else{
                                Shared_Helper.putKey(getBaseContext(),"internal_pass",pass_edit_text.getText().toString());
                                Shared_Helper.putKey(getBaseContext(),"protiction_question",protiction_qiestion_string);
                                Shared_Helper.putKey(getBaseContext(),"answer_protiction_question",answer_protiction_question_edit_text.getText().toString());


                                finish();
                            }
                        }

                    }
                }
            }

        });


    }



    private void initViews(){

        pass_edit_text=(EditText) findViewById(R.id.add_internal_pass_activity_password_edit_text);
        confirm_pass_edit_text=(EditText) findViewById(R.id.add_internal_pass_activity_confirm_password_edit_text);
        answer_protiction_question_edit_text =(EditText) findViewById(R.id.add_internal_pass_activity_answer_the_protiction_question_edit_text);

        protiction_questions_spinner =(Spinner) findViewById(R.id.add_internal_pass_activity_protiction_questions_spinner);
        save_image_view =(ImageView) findViewById(R.id.add_internal_pass_activity_save_image_view);


    }

    private void initSpinner(){

        spinnerArray=new ArrayList<>();
        spinnerArray.add(new SearchMethodeAndInternalPasswordModel("ماهي اول مدينه قمت بالسفر اليها؟"));
        spinnerArray.add(new SearchMethodeAndInternalPasswordModel("من هو صديق طفولتك المقرب؟"));
        spinnerArray.add(new SearchMethodeAndInternalPasswordModel("ماهي اول مدرسه قمت بالالتحاق بها؟"));
        spinnerArray.add(new SearchMethodeAndInternalPasswordModel("ما اسم افضل كتاب قراءته بحياتك ؟"));

        adapter=new SearchMethodeAndInternalPasswordItemsAdapter(this,spinnerArray);

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