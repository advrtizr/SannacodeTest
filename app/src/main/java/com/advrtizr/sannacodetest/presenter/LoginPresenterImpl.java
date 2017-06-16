package com.advrtizr.sannacodetest.presenter;

import android.content.Context;

import com.advrtizr.sannacodetest.model.AuthenticationStatusListener;
import com.advrtizr.sannacodetest.model.GoogleClient;
import com.advrtizr.sannacodetest.view.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView view;
    private GoogleClient client;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
        client = new GoogleClient((Context)view);
    }

    @Override
    public void authenticate(GoogleSignInAccount acct, AuthenticationStatusListener statusListener) {
        view.showProgress();
        client.firebaseAuthWithGoogle(acct, statusListener);
    }

    @Override
    public void signIn() {
        GoogleApiClient googleApiClient = client.initializeGoogleClient();
        view.sendSignIntent(googleApiClient);
    }

    @Override
    public void googleSignOut() {

    }


}
