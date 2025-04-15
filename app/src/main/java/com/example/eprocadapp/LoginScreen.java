package com.example.eprocadapp;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {

    private TextView forgotten_password_id;
    private TextView create_account_id;
    private EditText edit_email, edit_password;
    private AppCompatImageButton bt_eyeslash;
    private Button bt_login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_screen);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Move o aplicativo para o fundo ao invés de encerrar
                moveTaskToBack(true);
            }
        };

        // Adiciona o callback ao dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);


        Objects.requireNonNull(getSupportActionBar()).hide();
        StartComponents();
        forgotten_password_id.setOnClickListener(v -> {
            Intent intent= new Intent(LoginScreen.this, ResetPasswordScreen_1.class);
            startActivity(intent);
        });
        create_account_id.setOnClickListener(v -> {
            Intent intent= new Intent(LoginScreen.this, SignUpScreen.class);
            startActivity(intent);
        });

        bt_eyeslash.setOnClickListener(v -> {
            if (edit_password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Mostrar a senha
                edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                bt_eyeslash.setImageResource(R.drawable.ic_eyeslash_open);
            } else {
                // Ocultar a senha
                edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                bt_eyeslash.setImageResource(R.drawable.ic_eyeslash);
            }
            // Posiciona o cursor no final
            edit_password.setSelection(edit_password.getText().length());
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
        bt_login.setOnClickListener(v -> {
            String email = edit_email.getText().toString();
            String password = edit_password.getText().toString();
            if (email.isEmpty() || password.isEmpty()){
                Snackbar snackbar = Snackbar.make(v,"Preencha todos os campos",Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else {
                UserAuthentication(v);
            }
        });
    }
    private void UserAuthentication(View view) {
        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString();

        // Verifica o formato do e-mail antes de prosseguir
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar snackbar = Snackbar.make(view, "Formato de e-mail inválido.", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            return;
        }
        // Tenta autenticar o usuário com e-mail e senha diretamente
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Se bem sucedido, mostra a progression bar
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(this::ScreenUnderConstruction, 3000);
            } else {
                //Se der algum erro, aparece uma mensagem informando o erro.
                String error = getError(task);
                Snackbar snackbar = Snackbar.make(view, error, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            ScreenUnderConstruction();
        }
    }
    @NonNull
    private static String getError(Task<AuthResult> task) {
        String error;
        try {
            throw Objects.requireNonNull(task.getException());
        }catch (FirebaseAuthInvalidCredentialsException e) {
            // Senha incorreta
            error = "Senha incorreta ou e-mail não cadastrado. Verifique e tente novamente.";
        } catch (Exception e) {
            // Outros erros
            error = "Erro ao autenticar. Tente novamente.";
        }
        return error;
    }
    private void ScreenUnderConstruction(){
        Intent intent = new Intent(LoginScreen.this, ScreenUnderConstruction.class);
        startActivity(intent);
        finish();
    }
    private void StartComponents(){
        forgotten_password_id = findViewById(R.id.forgotten_password_id);
        create_account_id = findViewById(R.id.create_account_id);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        bt_login = findViewById(R.id.bt_login);
        progressBar = findViewById(R.id.progressbar);
        bt_eyeslash = findViewById(R.id.bt_eyeslash);

    }
}