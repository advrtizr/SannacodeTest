package com.advrtizr.sannacodetest.presenter;

import com.advrtizr.sannacodetest.model.AuthenticationStatusListener;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LoginPresenter {

    void authenticate(GoogleSignInAccount acct, AuthenticationStatusListener statusListener);
    void signIn();
    
}
