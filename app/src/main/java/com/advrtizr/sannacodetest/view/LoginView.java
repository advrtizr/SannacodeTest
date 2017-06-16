package com.advrtizr.sannacodetest.view;

import com.google.android.gms.common.api.GoogleApiClient;

public interface LoginView {
    void showProgress();
    void hideProgress();
    void sendSignIntent(GoogleApiClient mGoogleApiClient);
}
