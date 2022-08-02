package com.mokh.deoon.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mokh.deoon.R;
import com.mokh.deoon.helper.Shared_Helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestingActivity extends AppCompatActivity {

    TextView t;
    Button b;

    String user_name,path;
    String getUserNameFromSharedPereference,getUserNameFromSharedPereference2;


    private FirebaseFirestore db=FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

//        t=(TextView) findViewById(R.id.testTextView);

    /*    getUserNameFromSharedPereference=Shared_Helper.getkey(this,getUserNameFromSharedPereference+".db");
        getUserNameFromSharedPereference2=Shared_Helper.getkey(this,"user_name");

        path="/data/data/com.mokh.deoon/databases/azraq.db";

*/


        Shared_Helper.putKey(getBaseContext(),"user_name","alazraq");

        user_name=Shared_Helper.getkey(this,"user_name");

        b=(Button) findViewById(R.id.test_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestingActivity.this,Login_activity.class));
            }
        });

    }

    public void clickkkk(View view) {
//        Toast.makeText(getBaseContext(), user_name, Toast.LENGTH_SHORT).show();
//        loadData();
        getData();


    }

    private void getData(){

//        databaseReference=FirebaseDatabase.getInstance("https://deoon-52921.firebaseio.com").getReference().child("all_accounts");
        //databaseReference=FirebaseDatabase.getInstance("https://deoon-52921.firebaseio.com").getReferenceFromUrl();





//      databaseReference= FirebaseDatabase.getInstance().getReference("alazraq").child("all_accounts").child(user_name);
        // databaseReference;

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("all_accounts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Toast.makeText(getBaseContext(), snapshot.getKey(), Toast.LENGTH_SHORT).show();
                }
//                    Toast.makeText(getBaseContext(), String.valueOf(dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Toast.makeText(getBaseContext(), String.valueOf(dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });*/




    }

    private void startFirebaseForCheckingTheDate(){
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

    /*    My_firebase_class my_firebase_class=new My_firebase_class(this,getUserNameFromSharedPereference2,Integer.valueOf(date),path,getUserNameFromSharedPereference,getUserNameFromSharedPereference2);
        my_firebase_class.checkDate();
*/

    }


    private void loadData(){
        db.collection("users").document(user_name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String date=documentSnapshot.getString("date");
                        Toast.makeText(getBaseContext(), date, Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getBaseContext(), "is not exsist", Toast.LENGTH_SHORT).show();
                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}