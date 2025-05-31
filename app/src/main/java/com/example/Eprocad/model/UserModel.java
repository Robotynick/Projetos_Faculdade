package com.example.Eprocad.model;

import android.graphics.Color;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserModel {
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public UserModel() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void signIn(String email, String password, AuthCallback callback) {
        if (!isValidEmail(email)) {
            callback.onFailure("E-mail inválido. Verifique e tente novamente.");
            return;
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess(getCurrentUser());
            } else {
                callback.onFailure(getFirebaseAuthError(task));
            }
        });
    }

    private String getFirebaseAuthError(Task<AuthResult> task) {
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (FirebaseAuthWeakPasswordException e) {
            return "Digite uma senha com no mínimo 6 caracteres.";
        } catch (FirebaseAuthUserCollisionException e) {
            return "E-mail já cadastrado.";
        } catch (FirebaseAuthInvalidCredentialsException e) {
            return "Senha incorreta ou e-mail não cadastrado.";
        } catch (FirebaseAuthInvalidUserException e) {
            return "Usuário não encontrado. Verifique o e-mail e tente novamente.";
        } catch (FirebaseNetworkException e) {
            return "Erro de conexão. Verifique sua internet e tente novamente.";
        } catch (Exception e) {
            return "Erro inesperado. Entre em contato com o suporte.";
        }
    }


    public void validateRegistration(String name, String age, String email, String password, String confirm_password) throws IllegalArgumentException {
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

        // Validação do formato de e-mail antes da chamada ao Firebase
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres, incluindo uma maiúscula, um número e um caractere especial.");
        }

        if (!password.equals(confirm_password)) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }
    }

    public void registerUser(String name, String age, String email, String password, String confirmPassword, AuthCallback callback) {
        try {
            validateRegistration(name, age, email, password, confirmPassword);

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            saveUser(name, age, email);
                            callback.onSuccess(task.getResult().getUser());
                        } else {
                            callback.onFailure(getFirebaseAuthError(task));
                        }
                    });
        } catch (IllegalArgumentException e) {
            callback.onFailure(e.getMessage());
        }
    }

    private void saveUser(String name, String age, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("Name", name);
        userData.put("Age", age);
        userData.put("Email", email);

        db.collection("Users").document(email).set(userData);
    }

    public boolean isValidEmail(@NonNull String email) {
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public void showSnackbar(View v, String message) {
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.WHITE);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);

        void onFailure(String errorMessage);
    }
}