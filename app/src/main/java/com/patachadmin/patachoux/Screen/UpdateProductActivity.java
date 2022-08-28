package com.patachadmin.patachoux.Screen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.patachadmin.patachoux.R;
import com.squareup.picasso.Picasso;

public class UpdateProductActivity extends AppCompatActivity {
    EditText product_des,product_price;
    DatabaseReference myRef;
    ImageView imageView;
    StorageReference mRef;
    private Uri imgUri;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        product_des=findViewById(R.id.product_des);
        product_price=findViewById(R.id.product_price);
        imageView=findViewById(R.id.image);
        mRef= FirebaseStorage.getInstance().getReference("Product_images");
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onStart() {
        Picasso.with(this)
                .load(getIntent().getStringExtra("pic"))
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);
        product_des.setText(getIntent().getStringExtra("description"));
        product_price.setText(getIntent().getStringExtra("price"));
        super.onStart();
    }

    public void updateProduct(View view) {
        loadingDialog.show();
        if(getIntent().getStringExtra("type").equals("Bread")){
            myRef=FirebaseDatabase.getInstance().getReference("Products").child("Bread").child(getIntent().getStringExtra("name"));
        }
        else {
            myRef=FirebaseDatabase.getInstance().getReference("Products").child("Pastry").child(getIntent().getStringExtra("name"));
        }

          if(imgUri!=null){
              StorageReference storageReference = mRef.child(System.currentTimeMillis() + "." + getFileEx(imgUri));
              storageReference.putFile(imgUri)
                      .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                              Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                              while (!urlTask.isSuccessful()) ;
                              Uri downloadUrl = urlTask.getResult();
                              myRef.child("ProductDescription").setValue(product_des.getText().toString());
                              myRef.child("ProductPrice").setValue(product_price.getText().toString());
                              myRef.child("UserImage").setValue(downloadUrl.toString());
                              loadingDialog.dismiss();
                          }
                      })
                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              loadingDialog.dismiss();
                              Toast.makeText(UpdateProductActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                          }
                      })
                      .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                          }
                      });
          }
          else {
              myRef.child("ProductDescription").setValue(product_des.getText().toString());
              myRef.child("ProductPrice").setValue(product_price.getText().toString());
              loadingDialog.dismiss();
          }


    }
    // get the extension of file
    private String getFileEx(Uri uri){
        ContentResolver cr=UpdateProductActivity.this.getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    public void selectImage(View view) {
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            imgUri  = data.getData();
            imageView.setImageURI(imgUri);
        }

    }
}