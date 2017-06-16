package com.advrtizr.sannacodetest.model;

import com.google.firebase.auth.FirebaseAuth;

public interface AuthenticationStatusListener {
    void onSuccess(FirebaseAuth firebaseAuth);
    void onFailure();
}
