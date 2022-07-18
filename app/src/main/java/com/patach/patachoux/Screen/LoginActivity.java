package com.patach.patachoux.Screen;

import static com.patach.patachoux.Utils.Constant.setAdminEmail;
import static com.patach.patachoux.Utils.Constant.setAdminPassword;
import static com.patach.patachoux.Utils.Constant.setUserLoginStatus;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.patachoux.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etLoginEmail, etLoginPassword;
    DatabaseReference myRef;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        etLoginEmail =findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        myRef = FirebaseDatabase.getInstance().getReference("SuperAdmin");


    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String password) {
        if (email.isEmpty()) etLoginEmail.setError("Enter email!");
        else if (!email.contains("@")||!email.contains(".")) etLoginEmail.setError("Enter valid email!");
        else if (password.isEmpty()) etLoginPassword.setError("Enter password!");
        else if (password.length()<6) etLoginPassword.setError("Password must be at least 6 characters!");
        else return true;
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void loginAdmin(View view) {

        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();
        if (validate(email, password)) requestLogin(email, password);
    }
    private void requestLogin(String email, String password) {
        loadingDialog.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // get the admin email and password from the firebase
                String dbmail = dataSnapshot.child("AdminEmail").getValue().toString();
                String dbpass = dataSnapshot.child("AdminPassword").getValue().toString();
                // validate the email and password
                if (email.equals(dbmail) && password.equals(dbpass)) {
                    // open the admin dashboard screen
                    setUserLoginStatus(LoginActivity.this,true);

                    setAdminEmail(LoginActivity.this,email);
                    setAdminPassword(LoginActivity.this,password);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}