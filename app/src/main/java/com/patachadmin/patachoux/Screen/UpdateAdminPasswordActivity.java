package com.patachadmin.patachoux.Screen;

import static com.patachadmin.patachoux.Utils.Constant.getAdminEmail;
import static com.patachadmin.patachoux.Utils.Constant.getAdminPassword;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patachadmin.patachoux.R;

public class UpdateAdminPasswordActivity extends AppCompatActivity {
    private EditText etLoginEmail, etLoginPassword;
    DatabaseReference myRef;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_admin_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

    @Override
    protected void onStart() {
        etLoginEmail.setText(getAdminEmail(this));
        etLoginPassword.setText(getAdminPassword(this));
        super.onStart();
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
    public void updateData(View view) {

        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();
        if (validate(email, password)) requestLogin(email, password);
    }
    private void requestLogin(String email, String password) {
        loadingDialog.show();

        myRef.child("AdminEmail").setValue(email);
        myRef.child("AdminPassword").setValue(password);
        loadingDialog.dismiss();
        finish();


    }
}