package com.advrtizr.sannacodetest.presenter;

import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;

public interface LoginPresenter {
    void requestCurrentUser();
    void beginSignOut();
    void passIntentResult(int requestCode, Intent data);
    GoogleApiClient requestGoogleApiClient();
}
