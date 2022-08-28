package com.patachadmin.patachoux.Screen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachoux.R;

import java.util.ArrayList;

public class AddSuplierActivity extends AppCompatActivity {
    private Dialog loadingDialog;
    private EditText etRegisterEmail,et_user_name, etRegisterPassword, etRegisterConfirmPassword;
    private FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    ArrayList<String> suplierList =new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suplier);

        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        firebaseAuth = FirebaseAuth.getInstance();
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etRegisterConfirmPassword = findViewById(R.id.et_register_confirm_password);
        et_user_name = findViewById(R.id.et_user_name);
    }

    @Override
    protected void onStart() {
        getSuplierList();
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void registerSuplier(View view) {
        String email = etRegisterEmail.getText().toString();
        String name = et_user_name.getText().toString();
        String password = etRegisterPassword.getText().toString();
        String confirm_password = etRegisterConfirmPassword.getText().toString();
        if (validate(email,name, password, confirm_password)) requestRegister(email, password);

    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String name, String password, String confirm_password) {
        if (email.isEmpty()) etRegisterEmail.setError("Enter email!");
        else if (name.isEmpty()) et_user_name.setError("Enter name!");
        else if (!email.contains("@")||!email.contains(".")) etRegisterEmail.setError("Enter valid email!");
        else if (password.isEmpty()) etRegisterPassword.setError("Enter password!");
        else if (password.length()<6) etRegisterPassword.setError("Password must be at least 6 characters!");
        else if (confirm_password.isEmpty()) etRegisterConfirmPassword.setError("Enter password!");
        else if (!password.equals(confirm_password)) etRegisterConfirmPassword.setError("Password not matched!");
        else return true;
        return false;
    }

    private void requestRegister(String email, String password) {
        if(suplierList.contains( et_user_name.getText().toString())){
            Toast.makeText(AddSuplierActivity.this, "name already exist" , Toast.LENGTH_LONG).show();
        }
        else {
            loadingDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getCreateUserWithEmailOnClickListener(email));
        }

    }
    private OnCompleteListener<AuthResult> getCreateUserWithEmailOnClickListener(String email) {
        return task -> {
            if (task.isSuccessful()) {
                add();
            } else {
                loadingDialog.dismiss();
                Toast.makeText(AddSuplierActivity.this,"registration failed!",Toast.LENGTH_LONG).show();
            }
        };
    }
    private void add(){
        myRef=  FirebaseDatabase.getInstance().getReference("Suplier").child(et_user_name.getText().toString());
        myRef.child("Name").setValue(et_user_name.getText().toString());
        myRef.child("Mail").setValue(etRegisterEmail.getText().toString());
        loadingDialog.dismiss();
        finish();
    }

    public void getSuplierList(){
        loadingDialog.show();
        suplierList.clear();
        myRef=  FirebaseDatabase.getInstance().getReference().child("Suplier");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                   suplierList.add(dataSnapshot1.child("Name").getValue(String.class));
                }
                loadingDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}