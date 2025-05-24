package com.example.Eprocad;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ResetPasswordScreen_1 extends AppCompatActivity {

    private TextView sent_id;
    private AppCompatImageButton bt_voltar;
    private EditText edit_email_reset_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.reset_password_screen_1);

        Objects.requireNonNull(getSupportActionBar()).hide();
        IniciarComponentes();

        sent_id.setOnClickListener(this::SentEmailResetPassword);

        bt_voltar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void SentEmailResetPassword(View view) {
        String email = edit_email_reset_password.getText().toString().trim().toLowerCase();

        // Verifica se o campo está preenchido
        if (!TextUtils.isEmpty(email)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Users").whereEqualTo("Email", email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog dialog = getDialog();
                                dialog.show();

                            } else {
                                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                                Snackbar snackbar = Snackbar.make(view, "Falha ao enviar: " + errorMessage, Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        }

                        @NonNull
                        private AlertDialog getDialog() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle("E-mail enviado!");
                            builder.setMessage("O link para redefinir sua senha foi enviado para o e-mail informado.");

                            builder.setPositiveButton("OK", (dialog, which) -> {
                                dialog.dismiss();
                                finish();
                            });

                            return builder.create();
                        }
                    });
                } else {
                    // E-mail não cadastrado no Firestore
                    Snackbar snackbar = Snackbar.make(view, "E-mail não cadastrado ou inválido", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            });
        } else {
            // Campo de e-mail vazio
            Snackbar snackbar = Snackbar.make(view, "Por favor, insira um e-mail!", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }
    }

    private void IniciarComponentes() {
        sent_id = findViewById(R.id.sent);
        bt_voltar = findViewById(R.id.bt_voltar);
        edit_email_reset_password = findViewById(R.id.edit_email_reset_password);
    }
}