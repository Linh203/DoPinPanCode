package com.example.dopinpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.Manager.FoodListManager;
import com.example.dopinpan.Model.Carts;
import com.example.dopinpan.Model.Order;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.Model.User;


import com.example.dopinpan.ViewHolder.CartAdapter;
import com.example.dopinpan.ViewHolder.CartsViewHolder;
import com.example.dopinpan.zalopay.CreateOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TextView txtTotoal;

    private Button btnplaceorder;
    private List<Order> cart = new ArrayList<>();
    private List<Carts> carts1 = new ArrayList<>();


    private CartAdapter adapter;


    private LinearLayout cartback;

    private FirebaseDatabase database;

    private ImageView btnBack;
    private FirebaseRecyclerAdapter<Carts, CartsViewHolder> adapters;
    private DatabaseReference requests, table_user, carts;
    String amout = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        txtTotoal = findViewById(R.id.total);
        btnplaceorder = findViewById(R.id.placeorder);
        btnBack=findViewById(R.id.btn_back2);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        carts = database.getReference("Carts");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);


        recyclerView = findViewById(R.id.listcart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        new Database(Cart.this).cleanCart();
        loadFoods();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnplaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                ProgressDialog dialog = new ProgressDialog(Cart.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cart.size() > 0) {
                            //   showAlertDialog();
                            showDialogPayment();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(Cart.this, "Giỏ Hàng Trống", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                }, 2000);

            }
        });

    }


    @SuppressLint("MissingInflatedId")
    private void showDialogPayment() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        // builder.setMessage("Please Fill All Information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pay = inflater.inflate(R.layout.payment_layout, null);

        EditText edtadrss = layout_pay.findViewById(R.id.edt_address);
        TextView txtTotal = layout_pay.findViewById(R.id.oder_total1);
        TextView txtPhone = layout_pay.findViewById(R.id.oder_phone1);

        Button btnPayLocal = layout_pay.findViewById(R.id.btn_paylocal);
        Button btnZaloPay = layout_pay.findViewById(R.id.btn_zalopay);

        builder.setView(layout_pay);
        if (Common.currentUser.getAddress().equals("null"))
            edtadrss.setText("");
        else
            edtadrss.setText(Common.currentUser.getAddress().toString());
        txtTotal.setText(txtTotoal.getText().toString());
        txtPhone.setText(Common.currentUser.getPhone());

        btnPayLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                ProgressDialog dialog = new ProgressDialog(Cart.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        Request request = new Request(Common.currentUser.getPhone().toString(), Common.currentUser.getName().toString(), edtadrss.getText().toString(), amout, cart,"0", formattedDate);
                        requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                        Map<String, Object> addressUpdate = new HashMap<>();
                        addressUpdate.put("address", edtadrss.getText().toString());
                        table_user = database.getReference("User");
                        if (Common.currentUser.getAddress().equals("null")) {
                            table_user.child(Common.currentUser.getPhone()).updateChildren(addressUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Cart.this, "Đã Lưu Địa Chỉ", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Cart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (!edtadrss.getText().toString().equals(Common.currentUser.getAddress())) {
                            table_user.child(Common.currentUser.getPhone()).updateChildren(addressUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Cart.this, "Đã Thay Đổi Địa Chỉ !", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Cart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.child(Common.currentUser.getPhone()).getValue(User.class);
                                Common.currentUser = user;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        String phone = Common.currentUser.getPhone().toString();
                        Query CartInCart = carts.orderByChild("userPhone").equalTo(phone);
                        CartInCart.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot post : snapshot.getChildren()) {
                                    post.getRef().removeValue();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        new Database(getBaseContext()).cleanCart();
                        Toast.makeText(Cart.this, "Đặt Hàng Thành Công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 2000);
            }
        });

        btnZaloPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                ProgressDialog dialog = new ProgressDialog(Cart.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CreateOrder orderApi = new CreateOrder();
                        try {
                            JSONObject data = orderApi.createOrder(amout);
                            String code = data.getString("return_code");
                            if (code.equals("1")) {
                                String token = data.getString("zp_trans_token");
                                ZaloPaySDK.getInstance().payOrder(Cart.this, token, "demozpdk://app", new PayOrderListener() {
                                    @Override
                                    public void onPaymentSucceeded(String s, String s1, String s2) {
//                                        Request request = new Request(Common.currentUser.getPhone().toString(), Common.currentUser.getName().toString(), edtadrss.getText().toString(), txtTotoal.getText().toString(), cart);
//                                        requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
//
//                                        Map<String, Object> addressUpdate = new HashMap<>();
//                                        addressUpdate.put("address", edtadrss.getText().toString());
//                                        table_user = database.getReference("User");
//                                        table_user.child(Common.currentUser.getPhone()).updateChildren(addressUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                Toast.makeText(Cart.this, "Address Was Updated !", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(Cart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                User user = snapshot.child(Common.currentUser.getPhone()).getValue(User.class);
//                                                Common.currentUser = user;
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });
//
//                                        new Database(getBaseContext()).cleanCart();
                                        Toast.makeText(Cart.this, "Thanh Toán Thành Công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onPaymentCanceled(String s, String s1) {

                                    }

                                    @Override
                                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }


    private void loadFoods() {
        String phone = Common.currentUser.getPhone().toString();
        adapters = new FirebaseRecyclerAdapter<Carts, CartsViewHolder>(Carts.class, R.layout.cart_layout, CartsViewHolder.class, carts.orderByChild("userPhone").equalTo(phone)) {
            @Override
            protected void populateViewHolder(CartsViewHolder cartsViewHolder, Carts carts, int i) {
                cartsViewHolder.txtcartName.setText(carts.getProductName() + " x" + carts.getQuantity());

                cartsViewHolder.txtcartPrice.setText("$ " + Integer.parseInt(carts.getPrice()) * Integer.parseInt(carts.getQuantity()));


                TextDrawable drawable = TextDrawable.builder().buildRound("" + carts.getQuantity(), Color.RED);
                cartsViewHolder.imageView.setImageDrawable(drawable);


                new Database(Cart.this).addToCart(new Order(
                        carts.getProductId()
                        , carts.getProductName()
                        , carts.getQuantity()
                        , carts.getPrice()
                        , carts.getDiscount()));

                cart = new Database(Cart.this).getCart();
                int total = 0;
                for (Order order : cart) {
                    total += (Integer.parseInt(order.getQuantity())) * (Integer.parseInt(order.getPrice()));
                }
                Locale locale = new Locale("en", "US");
                NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                txtTotoal.setText(format.format(total));
                amout=String.valueOf(total);

                cartsViewHolder.btnDeleteCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteCarts(adapters.getRef(i).getKey());
                    }
                });

            }
        };
        adapters.notifyDataSetChanged();
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapters));
    }

    private void deleteCarts(String key) {
        Handler handler = new Handler();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thông Báo");
        alertDialog.setIcon(R.drawable.baseline_warning_24);
        alertDialog.setMessage("Vui Lòng Xác Nhận Xóa ?");

        alertDialog.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProgressDialog dialog = new ProgressDialog(Cart.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        carts.child(key).removeValue();
                        new Database(Cart.this).cleanCart();
                        loadFoods();
                        dialog.dismiss();
                        Toast.makeText(Cart.this, "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

}