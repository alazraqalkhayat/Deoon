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

public class ProtictionQquestionaActivity extends AppCompatActivity {

    EditText  answer_protiction_question_edit_text;
    Spinner protiction_questions_spinner;
    ImageView check_image_view;


    ArrayList<SearchMethodeAndInternalPasswordModel> spinnerArray;
    SearchMethodeAndInternalPasswordItemsAdapter adapter;

    String protiction_qiestion_string;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protiction_question_activity);


        init();

        initSpinner();

        if (protiction_questions_spinner!=null){

            protiction_questions_spinner.setAdapter(adapter);
            protiction_questions_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    SearchMethodeAndInternalPasswordModel items=(SearchMethodeAndInternalPasswordModel)adapterView.getSelectedItem();
                    protiction_qiestion_string=items.getItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


        check_image_view.setOnClickListener(v -> {
            if(answer_protiction_question_edit_text.getText().toString().isEmpty()){
                answer_protiction_question_edit_text.setError("يلزم تعبئه هذا الحقل");
            }else{

                if(!protiction_qiestion_string.equalsIgnoreCase(Shared_Helper.getkey(getBaseContext(),"protiction_question"))){
                    Toasty.custom(getBaseContext(),"سؤال الأمان غير مطابق لسؤال الأمان الذي اخترته أثناء إنشاء كلمة المرور..!",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();

                }else{
                    if(!answer_protiction_question_edit_text.getText().toString().equalsIgnoreCase(Shared_Helper.getkey(getBaseContext(),"answer_protiction_question"))){
                        Toasty.custom(getBaseContext(),"اجابة سؤال الأمان غير صحيحه..!",R.drawable.warning_24dp,R.color.golden,Toasty.LENGTH_LONG,true,true).show();
                    }else{
                        startActivity(new Intent(getBaseContext(), CreateInternalPasswordActivity.class));
                        finish();
                    }
                }
            }
        });
    }


    private void init(){
        protiction_questions_spinner =(Spinner) findViewById(R.id.protiction_question_activity_protiction_questions_spinner);
        answer_protiction_question_edit_text =(EditText) findViewById(R.id.protiction_question_activity__answer_the_protiction_question_edit_text);
        check_image_view =(ImageView) findViewById(R.id.protiction_question_activity_check_button);


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