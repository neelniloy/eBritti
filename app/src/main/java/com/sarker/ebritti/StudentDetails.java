package com.sarker.ebritti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentDetails extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference StuRef;
    private String current_user_id,key;
    private ImageView back,jm,aj,js,od;
    private TextView name,Class,roll,gender,nomini,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_student_details);

        name =findViewById(R.id.tv_name);
        Class =findViewById(R.id.tv_class);
        roll =findViewById(R.id.tv_roll);
        gender =findViewById(R.id.tv_gender);
        nomini =findViewById(R.id.tv_nomini);
        phone =findViewById(R.id.tv_phone);

        jm = findViewById(R.id.img_jm);
        aj = findViewById(R.id.img_aj);
        js = findViewById(R.id.img_js);
        od = findViewById(R.id.img_od);

        key = getIntent().getStringExtra("student_id");

        mfirebaseAuth = FirebaseAuth.getInstance();
        current_user_id = mfirebaseAuth.getCurrentUser().getUid();
        StuRef = FirebaseDatabase.getInstance().getReference("StudentData").child(current_user_id).child(key);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });



        StuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String n = dataSnapshot.child("studentName").getValue().toString();
                        String c = dataSnapshot.child("studentClass").getValue().toString();
                        String r = dataSnapshot.child("studentRoll").getValue().toString();
                        String g = dataSnapshot.child("studentGender").getValue().toString();
                        String nom = dataSnapshot.child("studentNomini").getValue().toString();
                        String p = dataSnapshot.child("studentPhone").getValue().toString();


                        String JM = dataSnapshot.child("JanMar").getValue().toString();
                        String AJ = dataSnapshot.child("AprJun").getValue().toString();
                        String JS = dataSnapshot.child("JulSep").getValue().toString();
                        String OD = dataSnapshot.child("OctDec").getValue().toString();

                        name.setText("Name: "+n);
                        Class.setText("Class: "+c);
                        roll.setText("Roll: "+r);
                        gender.setText("Gender: "+g);
                        nomini.setText("Nomini: "+nom);
                        phone.setText("Phone: "+p);

                        if(JM.equals("Yes")){
                            jm.setImageResource(R.drawable.ic_check);
                        }

                        if(AJ.equals("Yes")){
                            aj.setImageResource(R.drawable.ic_check);
                        }

                        if(JS.equals("Yes")){
                            js.setImageResource(R.drawable.ic_check);
                        }

                        if(OD.equals("Yes")){
                            od.setImageResource(R.drawable.ic_check);
                        }

                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentDetails.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


    }
}