package com.patachadmin.patachoux.Admin;

import static com.patachadmin.patachoux.Utils.Constant.setUserLoginStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachoux.Model.Suplier;
import com.patachadmin.patachoux.Order.SplierMainActivity;
import com.patachadmin.patachoux.R;
import com.patachadmin.patachoux.Screen.AddUserActivity;
import com.patachadmin.patachoux.Screen.LoginActivity;
import com.patachadmin.patachoux.Screen.MainActivity;
import com.patachadmin.patachoux.Screen.SuplierActivity;
import com.patachadmin.patachoux.Screen.UpdateAdminPasswordActivity;
import com.patachadmin.patachoux.Screen.UserActivity;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dRef;
    ArrayList<Suplier> suplierArrayList =new ArrayList<Suplier>();
    ArrayAdapter arrayAdapter;

    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        recyclerView=findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {

        getAdminData();
        super.onStart();
    }
    public void getAdminData(){
        dRef=  FirebaseDatabase.getInstance().getReference("SubAdmin");
        loadingDialog.show();
        suplierArrayList.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    suplierArrayList.add(new Suplier(postSnapshot.child("Name").getValue(String.class)
                            ,postSnapshot.child("Mail").getValue(String.class)));

                }
                arrayAdapter =new ArrayAdapter();
                recyclerView.setAdapter(arrayAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(AdminMainActivity.this).inflate(R.layout.item_suplier,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.name.setText(suplierArrayList.get(position).getName());
            holder.email.setText(suplierArrayList.get(position).getEmail());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final CharSequence[] options = {"Delete", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Delete")) {
                                dialog.dismiss();
                                deleteAccount(position);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return suplierArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView name,email;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                email=itemView.findViewById(R.id.email);
                name=itemView.findViewById(R.id.name);
                cardView=itemView.findViewById(R.id.cardView);
            }
        }
    }

    public void deleteAccount(int position){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dRef=  FirebaseDatabase.getInstance().getReference("SubAdmin").child(suplierArrayList.get(position).getName());
                    dRef.removeValue();
                    getAdminData();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logoutUser:
                setUserLoginStatus(AdminMainActivity.this,false);
                startActivity(new Intent(AdminMainActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case R.id._suplier:
                startActivity(new Intent(AdminMainActivity.this, SuplierActivity.class));
            case R.id.add_sub_Admin:
                startActivity(new Intent(AdminMainActivity.this, AddSubAdminActivity.class));
            case R.id._user:
                startActivity(new Intent(AdminMainActivity.this, UserActivity.class));
                return true;
            case R.id.setting_account:
                startActivity(new Intent(AdminMainActivity.this, UpdateAdminPasswordActivity.class));
                return true;
            case R.id.view_order:
                startActivity(new Intent(AdminMainActivity.this, SplierMainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}