package com.example.Eprocad;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.activity.EdgeToEdge;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpScreen extends AppCompatActivity {
    private EditText edit_name_sign_up,edit_age,edit_email_sing_up,edit_password_sign_up,edit_confirm_password_sign_up;
    private Button bt_sent;
    String[] sign_up_messages = {"Preencha todos os campos", "Cadastro realizado com sucesso"};
    String userId;
    private ProgressBar progressBar;
    private AppCompatImageButton bt_voltar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();
        containerComponents();

        bt_sent.setOnClickListener(v -> {
            String name = edit_name_sign_up.getText().toString();
            String age = edit_age.getText().toString();
            String email = edit_email_sing_up.getText().toString();
            String password = edit_password_sign_up.getText().toString();
            String confirm_password = edit_confirm_password_sign_up.getText().toString();

            if (name.isEmpty() || age.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()){
                Snackbar snackbar = Snackbar.make(v,sign_up_messages[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                RegisterUser(name, age, email, password, confirm_password, v);
            }


        });
        bt_voltar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void validarCadastro(String name, String age, String password, String confirm_password) throws IllegalArgumentException {
        // Validações personalizadas
        if (name == null || name.trim().length() < 5) {
            throw new IllegalArgumentException("O nome deve ter pelo menos 5 caracteres.");
        }

        try {
            int idade = Integer.parseInt(age);
            if (idade > 110) {
                throw new IllegalArgumentException("A idade não pode ser maior que 110.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A idade deve ser um número válido.");
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")) {
            throw new IllegalArgumentException(
                    "Mínimo 8 caracteres com pelo menos uma maiúscula, minúscula, número e caractere especial (!, @, #, etc.)");
        }

        if (!password.equals(confirm_password)) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }
    }

    public void RegisterUser(String name, String age, String email, String password, String confirm_password, View v) {
        try {
            validarCadastro(name, age, password, confirm_password);

            // Exibe a ProgressBar antes de iniciar o cadastro
            progressBar.setVisibility(View.VISIBLE);

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Cadastro bem-sucedido
                            SaveUser();
                            // Redireciona para a tela de login após o cadastro
                            new Handler().postDelayed(() -> {
                                progressBar.setVisibility(View.GONE); // Esconde a ProgressBar ao redirecionar
                                Intent intent = new Intent(v.getContext(), LoginScreen.class);
                                v.getContext().startActivity(intent);
                            }, 3000); // Delay de 3 segundos para redirecionar
                        } else {
                            // Caso ocorra um erro no cadastro
                            progressBar.setVisibility(View.GONE); // Esconde a ProgressBar
                            String erro = getString(task);

                            Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                    });
        } catch (IllegalArgumentException e) {
            // Esconde a ProgressBar em caso de erro
            progressBar.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(v, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }
    }

    @NonNull
    private static String getString(Task<AuthResult> task) {
        String erro;
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (FirebaseAuthWeakPasswordException e) {
            erro = "Digite uma senha com no mínimo 6 caracteres";
        } catch (FirebaseAuthUserCollisionException e) {
            erro = "E-mail já cadastrado";
        } catch (FirebaseAuthInvalidCredentialsException e) {
            erro = "E-mail inválido";
        } catch (Exception e) {
            erro = "Erro ao cadastrar usuário";
        }
        return erro;
    }


    private void containerComponents(){
            edit_name_sign_up = findViewById(R.id.edit_name_sign_up);
            edit_age = findViewById(R.id.edit_age);
            edit_email_sing_up = findViewById(R.id.edit_email_sing_up);
            edit_password_sign_up = findViewById(R.id.edit_password_sign_up);
            edit_confirm_password_sign_up = findViewById(R.id.edit_confirm_password_sign_up);
            bt_sent = findViewById(R.id.bt_sent);
            progressBar = findViewById(R.id.progressbar);
            bt_voltar = findViewById(R.id.bt_voltar);


        }
        private void SaveUser(){
            String name = edit_name_sign_up.getText().toString();
            String age = edit_age.getText().toString();
            String email = edit_email_sing_up.getText().toString().toLowerCase();
            String password = edit_password_sign_up.getText().toString();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> users = new HashMap<>();
            users.put("Name", name);
            users.put("Age", age);
            users.put("Email", email);
            users.put("Password", password);

            userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            DocumentReference documentReference = db.collection("Users").document(userId);
            documentReference.set(users).addOnSuccessListener(unused -> Log.d("db", "Dados salvos")).addOnFailureListener(e -> Log.d("db_error", "Erro ao salvar os dados" + e));
        }
}
