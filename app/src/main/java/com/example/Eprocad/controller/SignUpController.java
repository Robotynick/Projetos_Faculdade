package com.example.Eprocad.controller;

import com.example.Eprocad.R;
import com.example.Eprocad.model.UserModel;
import com.example.Eprocad.view.SignUpScreen;
import com.google.firebase.auth.FirebaseUser;

public class SignUpController {
    private final SignUpScreen signUpScreen;
    private final UserModel userModel;

    public SignUpController(SignUpScreen signUpScreen) {
        this.signUpScreen = signUpScreen;
        this.userModel = new UserModel();

    }

    public void registerUser(String name, String age, String email, String password, String confirmPassword) {
        signUpScreen.showLoading(true);
        userModel.registerUser(name, age, email, password, confirmPassword, new UserModel.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                signUpScreen.showLoading(false);
                signUpScreen.navigateToLoginScreen();
            }

            @Override
            public void onFailure(String errorMessage) {
                signUpScreen.showLoading(false);
                userModel.showSnackbar(signUpScreen.findViewById(R.id.coordinatorLayout), errorMessage);
            }
        });
    }
}