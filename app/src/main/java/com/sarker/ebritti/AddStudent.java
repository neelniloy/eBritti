package com.sarker.ebritti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddStudent extends AppCompatActivity {

    private ImageView back;
    private TextInputEditText Name,Class, roll,nomini, phone;
    private TextInputLayout nameEditTextLayout,classTextLayout,phoneEditTextLayout, rollEditTextLayout, nominiEditTextLayout;
    private Button btnAdd;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String user_id,gender="";
    private DatabaseReference StuRef;
    private RadioButton male, female, custom;
    private CheckBox jan_mar,apr_jun,jul_sep,oct_dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_student);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAdd = findViewById(R.id.btn_add_student);

        Name = findViewById(R.id.et_student_name);
        Class = findViewById(R.id.et_class);
        roll = findViewById(R.id.et_roll);
        phone = findViewById(R.id.et_phone);
        nomini = findViewById(R.id.et_nomini);

        male = findViewById(R.id.g_m);
        female = findViewById(R.id.g_f);
        custom = findViewById(R.id.g_c);

        jan_mar = findViewById(R.id.jan_mar);
        apr_jun = findViewById(R.id.apr_jun);
        jul_sep = findViewById(R.id.jul_sep);
        oct_dec = findViewById(R.id.oct_dec);

        nameEditTextLayout = findViewById(R.id.editTextNameLayout);
        classTextLayout = findViewById(R.id.editTextClassLayout);
        rollEditTextLayout = findViewById(R.id.editTextRollLayout);
        phoneEditTextLayout = findViewById(R.id.editTextPhoneLayout);
        nominiEditTextLayout = findViewById(R.id.editTextNominiLayout);



        mAuth = FirebaseAuth.getInstance();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = Name.getText().toString();
                String Class1 = Class.getText().toString();
                String roll1 = roll.getText().toString();
                String nomini1 = nomini.getText().toString();
                String phone1 = phone.getText().toString();


                if (name.isEmpty()) {
                    nameEditTextLayout.setError("*Name required");
                    Name.requestFocus();

                }
                else if (Class1.isEmpty()) {
                    classTextLayout.setError("*Class required");
                    Class.requestFocus();
                }
                else if (roll1.isEmpty()) {
                    rollEditTextLayout.setError("*Roll required");
                    roll.requestFocus();
                }
                else if (!(male.isChecked() || female.isChecked() || custom.isChecked())) {

                        Toast.makeText(AddStudent.this, "Select Gender", Toast.LENGTH_SHORT).show();

                }
                else if (nomini1.isEmpty()) {
                    nominiEditTextLayout.setError("*Nomini required");
                    nomini.requestFocus();
                }

                else if (phone1.isEmpty()) {
                    phoneEditTextLayout.setError("*Phone required");
                    phone.requestFocus();
                }
                else if (phone1.length() != 11){
                    phoneEditTextLayout.setError("*Minimum length of a Number should be 11");
                    phone.requestFocus();
                }
                else if (!Patterns.PHONE.matcher(phone1).matches()){
                    phoneEditTextLayout.setError("*Enter a valid Phone Number");
                    phone.requestFocus();
                }
                else if (!(name.isEmpty() && Class1.isEmpty() && roll1.isEmpty() && nomini1.isEmpty() && phone1.isEmpty())) {

                    progressDialog = new ProgressDialog(AddStudent.this);
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Adding data...");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();

                            sendUserData();

                        }
                    },2500);




                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AddStudent.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void sendUserData() {

        user_id = mAuth.getCurrentUser().getUid();
        StuRef = FirebaseDatabase.getInstance().getReference().child("StudentData").child(user_id);


        String name = Name.getText().toString();
        String Class1 = Class.getText().toString();
        String roll1 = roll.getText().toString();
        String nomini1 = nomini.getText().toString();
        String phone1 = phone.getText().toString();
        String saveCurrentDate, saveCurrentTime,jm="No",aj="No",js="No",od="No";

        if (male.isChecked()){
            gender = "Male";
        }
        if (female.isChecked()) {
            gender = "Female";
        }
        if (custom.isChecked()){
            gender = "Custom";
        }

        if (jan_mar.isChecked()){
            jm = "Yes";
        }
        if (apr_jun.isChecked()){
            aj = "Yes";
        }
        if (jul_sep.isChecked()){
            js = "Yes";
        }
        if (oct_dec.isChecked()){
            od = "Yes";
        }



        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        Map reg = new HashMap();

        reg.put("studentName", name);
        reg.put("studentClass", Class1);
        reg.put("studentRoll", roll1);
        reg.put("studentGender", gender);
        reg.put("studentNomini", nomini1);
        reg.put("studentPhone", phone1);
        reg.put("teacherUID", user_id);
        reg.put("JanMar", jm);
        reg.put("AprJun", aj);
        reg.put("JulSep", js);
        reg.put("OctDec", od);
        reg.put("addedTime",saveCurrentTime);
        reg.put("addedDate",saveCurrentDate);

        StuRef.child(name.replaceAll("\\s+", "")+getDateInMillis(saveCurrentTime)).updateChildren(reg);


        progressDialog.dismiss();
        Toast.makeText(AddStudent.this, "Details Successfully Added", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(AddStudent.this, MainActivity.class));
        finish();


    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "hh:mm a");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}