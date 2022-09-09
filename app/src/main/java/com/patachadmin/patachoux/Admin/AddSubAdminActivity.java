package com.patachadmin.patachoux.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.patachadmin.patachoux.R;
import com.patachadmin.patachoux.Screen.AddUserActivity;

import java.util.ArrayList;
import java.util.List;

public class AddSubAdminActivity extends AppCompatActivity {
    private EditText etRegisterEmail,et_user_name, etRegisterPassword, etRegisterConfirmPassword,
            et_city,et_postal_code,et_register_address,et_user_number,et_code;
    private FirebaseAuth firebaseAuth;

    DatabaseReference myRef;
    TextView tv_login;
    Button btnRegister;
    private Dialog loadingDialog;
    ArrayList<String> adminList =new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_admin);
        et_postal_code=findViewById(R.id.et_postal_code);
        et_city=findViewById(R.id.et_city);
        et_user_number=findViewById(R.id.et_user_number);
        et_register_address=findViewById(R.id.et_register_address);
        et_code=findViewById(R.id.et_code);
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
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String email = etRegisterEmail.getText().toString();
                String name = et_user_name.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String confirm_password = etRegisterConfirmPassword.getText().toString();
                String user_number =et_user_number.getText().toString();
                String register_address =et_register_address.getText().toString();
                if (validate(email,name, password, confirm_password,user_number,register_address)) requestRegister(email, password);
            }
        });
    }
    @Override
    public void onStart() {
        getAdminList();
        super.onStart();
    }






    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String name, String password, String confirm_password, String user_number, String register_address) {
        if (email.isEmpty()) etRegisterEmail.setError("Enter email!");
        else if (register_address.isEmpty()) et_user_name.setError("Enter address!");
        else if (et_city.getText().toString().isEmpty()) et_city.setError("Enter City!");
        else if (et_postal_code.getText().toString().isEmpty()) et_postal_code.setError("Enter Postal Code!");
        else if (user_number.isEmpty()) et_user_name.setError("Enter phone number!");
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
        if(adminList.contains( et_user_name.getText().toString())){
            Toast.makeText(AddSubAdminActivity.this, "name already exist" , Toast.LENGTH_LONG).show();
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
                Toast.makeText(AddSubAdminActivity.this,"Registration failed!",Toast.LENGTH_LONG).show();

            }
        };
    }

    private void add(){

        myRef=  FirebaseDatabase.getInstance().getReference("User").child(et_user_name.getText().toString());
        myRef.child("Name").setValue(et_user_name.getText().toString());
        myRef.child("Mail").setValue(etRegisterEmail.getText().toString());
        myRef.child("Address").setValue(et_register_address.getText().toString());
        myRef.child("PhoneNumber").setValue(et_user_number.getText().toString());
        myRef.child("City").setValue(et_city.getText().toString());
        myRef.child("PostalCode").setValue(et_postal_code.getText().toString());
        myRef.child("SecretCode").setValue(et_code.getText().toString());
        loadingDialog.dismiss();
        Toast.makeText(AddSubAdminActivity.this,"Registration successful",Toast.LENGTH_LONG).show();
        finish();
    }
    public void getAdminList(){
        loadingDialog.show();
        adminList.clear();
        myRef=  FirebaseDatabase.getInstance().getReference().child("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    adminList.add(dataSnapshot1.child("Name").getValue(String.class));
                }
                loadingDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}