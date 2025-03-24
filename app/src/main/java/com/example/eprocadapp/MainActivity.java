package com.example.eprocadapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView forgotten_password_id;
    private TextView create_account_id;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        IniciarComponentes();

        forgotten_password_id.setOnClickListener(v -> {
            Intent intent= new Intent(MainActivity.this, FormResetPassword1.class);
            startActivity(intent);
        });
        create_account_id.setOnClickListener(v -> {
            Intent intent= new Intent(MainActivity.this, FormSignUp.class);
            startActivity(intent);
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        }
    private void IniciarComponentes(){
        forgotten_password_id = findViewById(R.id.forgotten_password_id);
        create_account_id = findViewById(R.id.create_account_id);

    }

}