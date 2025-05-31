package com.example.Eprocad.view;

import com.example.Eprocad.controller.LoginController;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.Eprocad.model.UserModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.Eprocad.R;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {

    private EditText edit_email, edit_password;
    private Button bt_login;
    private Button create_account_id;
    private AppCompatImageButton bt_eye_off;
    private Button forgotten_password_id;
    private ProgressBar progressBar;
    private LoginController loginController;
    private UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        startComponents();

        Objects.requireNonNull(getSupportActionBar()).hide();

        loginController = new LoginController(this);
        userModel = new UserModel();

        bt_login.setOnClickListener(v -> {
            String email = edit_email.getText().toString();
            String password = edit_password.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                userModel.showSnackbar(findViewById(R.id.coordinatorLayout), "Preencha todos os campos");
            } else {
                loginController.authenticateUser(email, password);
            }
        });

        create_account_id.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, SignUpScreen.class);
            startActivity(intent);
        });

        forgotten_password_id.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, ResetPasswordScreen_1.class);
            startActivity(intent);
        });

        bt_eye_off.setOnClickListener(v -> {
            if (edit_password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                edit_password.setInputType((InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));
                bt_eye_off.setImageResource(R.drawable.ic_eye);
            } else {
                edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                bt_eye_off.setImageResource(R.drawable.ic_eye_off);
            }
        });
    }

    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void navigateToMainScreen() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoginScreen.this, MainScreen.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    private void startComponents() {
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        bt_login = findViewById(R.id.bt_login);
        progressBar = findViewById(R.id.progressbar);
        create_account_id = findViewById(R.id.create_account_id);
        forgotten_password_id = findViewById(R.id.forgotten_password_id);
        bt_eye_off = findViewById(R.id.bt_eye_off);
    }
}