package com.advrtizr.sannacodetest.presenter;

import android.content.Context;
import android.content.Intent;

import com.advrtizr.sannacodetest.listeners.AuthMessageListener;
import com.advrtizr.sannacodetest.listeners.AuthProgressListener;
import com.advrtizr.sannacodetest.listeners.UserChangeListener;
import com.advrtizr.sannacodetest.model.FirebaseConnection;
import com.advrtizr.sannacodetest.model.LoginModel;
import com.advrtizr.sannacodetest.view.LoginView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenterImpl implements LoginPresenter, UserChangeListener, AuthProgressListener, AuthMessageListener {

    private LoginView view;
    private LoginModel connection;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
        connection = new FirebaseConnection((Context) view, this, this, this);
    }

    @Override
    public void requestCurrentUser() {
        FirebaseUser currentUser = connection.passCurrentUser();
        if (currentUser != null) {
            view.updateUI(currentUser);
        }
    }

    @Override
    public void beginSignOut() {
        connection.signOut();
    }

    @Override
    public void passIntentResult(int requestCode, Intent data) {
        connection.executeSignIn(requestCode, data);
    }

    @Override
    public GoogleApiClient requestGoogleApiClient() {
        return connection.passGoogleApiClient();
    }

    @Override
    public void onUserChanged(FirebaseUser firebaseUser) {
        view.updateUI(firebaseUser);
    }

    @Override
    public void authenticationStarted() {
        view.showProgress();
    }

    @Override
    public void authenticationFinished() {
        view.hideProgress();
    }

    @Override
    public void onMessageReceived(String message) {
        if (!message.equals("")) {
            view.showMessage(message);
        }
    }
}
