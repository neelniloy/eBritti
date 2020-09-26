package com.sarker.ebritti;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button addStudent,viewStudent;
    private ImageView signOut;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);

        addStudent = findViewById(R.id.add_student);
        viewStudent = findViewById(R.id.view_student);
        signOut = findViewById(R.id.signOut);

        mAuth = FirebaseAuth.getInstance();

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(MainActivity.this,AddStudent.class);
                startActivity(n);


            }
        });



        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent n = new Intent(MainActivity.this,ViewStudentList.class);
                startActivity(n);

            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Are you sure you want to log out?");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.show();
                                progressDialog.setMessage("Signing Out...");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAuth.signOut();
                                        progressDialog.dismiss();
                                        finishAffinity();
                                        startActivity(new Intent(MainActivity.this, Login.class));
                                    }
                                },1500);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                Window v =((AlertDialog)alert11).getWindow();
                //view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                v.setBackgroundDrawableResource(R.drawable.custom_button_so);

                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

                Button btnPositive = alert11.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alert11.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);


            }
        });
    }
}