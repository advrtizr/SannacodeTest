package com.advrtizr.sannacodetest.listeners;

import com.google.firebase.auth.FirebaseUser;

public interface UserChangeListener {
    void onUserChanged(FirebaseUser firebaseUser);
}
