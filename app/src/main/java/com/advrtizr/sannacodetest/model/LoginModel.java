package com.advrtizr.sannacodetest.model;

import android.content.Intent;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;

public interface LoginModel {

    void executeSignIn(int requestCode, Intent data);
    FirebaseUser passCurrentUser();
    GoogleApiClient passGoogleApiClient();
    void signOut();
}
