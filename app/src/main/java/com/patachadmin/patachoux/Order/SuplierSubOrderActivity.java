package com.patachadmin.patachoux.Order;

import static com.patachadmin.patachoux.Order.SplierMainActivity.suplierOrderList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachoux.Model.Cart;
import com.patachadmin.patachoux.R;

import java.util.ArrayList;

public class SuplierSubOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    ArrayList<Cart> orderList =new ArrayList<Cart>();
    DatabaseReference databaseReference;

    TextView user_name,user_address,user_number,suplier_name,price_text;
    int index=0;
    int totalPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suplier_sub_order);
        price_text=findViewById(R.id.price_text);
        user_number=findViewById(R.id.user_number);
        user_address=findViewById(R.id.user_address);
        user_name=findViewById(R.id.user_name);
        suplier_name=findViewById(R.id.suplier_name);
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
        index=getIntent().getIntExtra("index",-1);
        user_address.setText("Address : "+suplierOrderList.get(index).getUserAddress());
        user_number.setText("Number : "+suplierOrderList.get(index).getUserNumber());
        user_name.setText("Name : "+suplierOrderList.get(index).getUserName());
          if(!suplierOrderList.get(index).getSuplierName().equals("none")){
              suplier_name.setText("Suplier Name : "+suplierOrderList.get(index).getSuplierName());
          }

        getProductsData();
        super.onStart();
    }
    public void getProductsData(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Order")
                .child(suplierOrderList.get(index).getOrderId()).child("OrderItems");
        loadingDialog.show();
        orderList.clear();
        totalPrice=0;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    orderList.add(new Cart(
                            postSnapshot.child("ProductName").getValue(String.class)
                            ,postSnapshot.child("ProductPrice").getValue(String.class)
                            ,postSnapshot.child("ProductQuantity").getValue(String.class)
                            ,postSnapshot.child("ProductImage").getValue(String.class)));
                    totalPrice=totalPrice+(Integer.valueOf(postSnapshot.child("ProductPrice").getValue(String.class))*Integer.valueOf(postSnapshot.child("ProductQuantity").getValue(String.class)));


                }
                price_text.setText("Total Price : "+totalPrice+" $");
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
            View v= LayoutInflater.from(SuplierSubOrderActivity.this).inflate(R.layout.item_slip,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.product_name.setText(orderList.get(position).getProductName());
            holder.price.setText("$ "+orderList.get(position).getProductPrice());
            holder.product_quantity.setText("Quantity :"+orderList.get(position).getProductQuantity());
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView product_name,product_quantity,price;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                product_name=itemView.findViewById(R.id.product_name);
                cardView=itemView.findViewById(R.id.cardView);
                product_quantity=itemView.findViewById(R.id.product_quantity);
                price=itemView.findViewById(R.id.product_price);
            }
        }
    }
}