package com.example.Eprocad.view;

import com.example.Eprocad.controller.SignUpController;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.Eprocad.R;
import com.example.Eprocad.model.UserModel;

import java.util.Objects;

public class SignUpScreen extends AppCompatActivity {
    private EditText edit_name_sign_up, edit_age, edit_email_sing_up, edit_password_sign_up, edit_confirm_password_sign_up;
    private Button bt_sent;
    String[] sign_up_messages = {"Preencha todos os campos", "Cadastro realizado com sucesso"};
    private ProgressBar progressBar;
    private AppCompatImageButton bt_voltar;
    private SignUpController signUpController;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.sign_up_screen);

        containerComponents();

        Objects.requireNonNull(getSupportActionBar()).hide();

        signUpController = new SignUpController(this);
        userModel = new UserModel();

        bt_sent.setOnClickListener(v -> {
            String name = edit_name_sign_up.getText().toString();
            String age = edit_age.getText().toString();
            String email = edit_email_sing_up.getText().toString();
            String password = edit_password_sign_up.getText().toString();
            String confirm_password = edit_confirm_password_sign_up.getText().toString();

            if (name.isEmpty() || age.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
                userModel.showSnackbar(findViewById(R.id.coordinatorLayout), sign_up_messages[0]);

            } else {
                signUpController.registerUser(name, age, email, password, confirm_password);
            }

        });

        bt_voltar.setOnClickListener(v -> finish());

    }

    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void containerComponents() {
        edit_name_sign_up = findViewById(R.id.edit_name_sign_up);
        edit_age = findViewById(R.id.edit_age);
        edit_email_sing_up = findViewById(R.id.edit_email_sing_up);
        edit_password_sign_up = findViewById(R.id.edit_password_sign_up);
        edit_confirm_password_sign_up = findViewById(R.id.edit_confirm_password_sign_up);
        bt_sent = findViewById(R.id.bt_sent);
        progressBar = findViewById(R.id.progressbar);
        bt_voltar = findViewById(R.id.bt_voltar);
    }

    public void navigateToLoginScreen() {
        Intent intent = new Intent(SignUpScreen.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}