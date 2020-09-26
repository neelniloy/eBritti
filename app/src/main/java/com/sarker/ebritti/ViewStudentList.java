package com.sarker.ebritti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewStudentList extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference StuRef;

    private RecyclerView sRecyclerView;
    private String current_user_id;
    private ImageView back;
    private ImageView empty;
    private EditText search;

    FirebaseRecyclerAdapter<StudentInfo,SearchViewHolder> firebaseRecyclerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_view_student_list);


        mfirebaseAuth = FirebaseAuth.getInstance();
        current_user_id = mfirebaseAuth.getCurrentUser().getUid();
        StuRef = FirebaseDatabase.getInstance().getReference("StudentData").child(current_user_id);

        back = findViewById(R.id.back);
        empty = findViewById(R.id.emptya_data);
        search = findViewById(R.id.search_field);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        sRecyclerView = findViewById(R.id.student_list_rv);
        sRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //        linearLayoutManager.setReverseLayout(true);
        //        linearLayoutManager.setStackFromEnd(true);
        sRecyclerView.setLayoutManager(linearLayoutManagerV);

        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {

                    searchStudent(search.getText().toString());


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        searchStudent(search.getText().toString());



    }

    private void searchStudent(String search) {


        FirebaseRecyclerOptions<StudentInfo> options =
                new FirebaseRecyclerOptions.Builder<StudentInfo>()
                        .setQuery(StuRef.orderByChild("studentName").startAt(search).endAt(search+"\uf8ff"),StudentInfo.class)
                        .build();



        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<StudentInfo, SearchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchViewHolder holder, final int i, @NonNull StudentInfo search) {

                final String key = firebaseRecyclerAdapter.getRef(i).getKey();

                holder.studentName.setText(search.getStudentName());
                holder.studentClass.setText(search.getStudentClass());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), StudentDetails.class);
                        intent.putExtra("student_id", key);
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.student_item, parent, false);

                return new SearchViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                // if there are no chat messages, show a view that invites the user to add a message
                empty.setVisibility(
                        firebaseRecyclerAdapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE
                );
            }

        };




        firebaseRecyclerAdapter.startListening();
        sRecyclerView.setAdapter(firebaseRecyclerAdapter);



    }

    //view holder
    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        public TextView studentName,studentClass;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.full_name);
            studentClass = itemView.findViewById(R.id._class);

        }
    }

}