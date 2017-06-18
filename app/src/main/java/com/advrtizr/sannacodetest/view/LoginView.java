package com.advrtizr.sannacodetest.view;

import com.google.firebase.auth.FirebaseUser;

public interface LoginView {
    void updateUI(FirebaseUser firebaseUser);
    void showProgress();
    void hideProgress();
    void showMessage(String message);
}
