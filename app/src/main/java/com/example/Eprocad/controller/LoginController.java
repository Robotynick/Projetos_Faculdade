package com.example.Eprocad.controller;

import com.example.Eprocad.R;
import com.example.Eprocad.model.UserModel;
import com.example.Eprocad.view.LoginScreen;
import com.google.firebase.auth.FirebaseUser;

public class LoginController {
    private final LoginScreen loginScreen;
    private final UserModel userModel;

    public LoginController(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
        this.userModel = new UserModel();
    }

    public void authenticateUser(String email, String password) {
        loginScreen.showLoading(true);
        userModel.signIn(email, password, new UserModel.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                loginScreen.showLoading(false);
                loginScreen.navigateToMainScreen();
            }

            @Override
            public void onFailure(String errorMessage) {
                loginScreen.showLoading(false);
                userModel.showSnackbar(loginScreen.findViewById(R.id.coordinatorLayout), errorMessage);
            }
        });
    }
}