package com.advrtizr.sannacodetest.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.advrtizr.sannacodetest.Constants;
import com.advrtizr.sannacodetest.R;
import com.advrtizr.sannacodetest.listeners.AuthMessageListener;
import com.advrtizr.sannacodetest.listeners.AuthProgressListener;
import com.advrtizr.sannacodetest.listeners.UserChangeListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseConnection implements LoginModel, GoogleApiClient.OnConnectionFailedListener {

    private Context context;
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private UserChangeListener userChangeListener;
    private AuthProgressListener authProgressListener;
    private AuthMessageListener authMessageListener;

    public FirebaseConnection(Context context, UserChangeListener userChangeListener, AuthProgressListener authProgressListener, AuthMessageListener authMessageListener) {
        this.context = context;
        this.userChangeListener = userChangeListener;
        this.authProgressListener = authProgressListener;
        this.authMessageListener = authMessageListener;
        firebaseAuth = FirebaseAuth.getInstance();
        initializeClient();
    }

    // [START config_sign in]
    private GoogleSignInOptions configureGSO() {
        // Configure Google Sign In
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }
    // [END config_sign in]

    // [START initializing client]
    private void initializeClient() {
        // Creating google client object
        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, configureGSO())
                .build();
    }
    // [END initializing client]

    // [START auth with google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        // [START_EXCLUDE silent]
        authProgressListener.authenticationStarted();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            userChangeListener.onUserChanged(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            authMessageListener.onMessageReceived(context.getString(R.string.auth_failed));
                        }
                        // [START_EXCLUDE]
                        authProgressListener.authenticationFinished();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth with google]

    @Override
    public void executeSignIn(int requestCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update a message appropriately
                authMessageListener.onMessageReceived(context.getString(R.string.auth_failed));
                userChangeListener.onUserChanged(null);
            }
        }
    }

    @Override
    public FirebaseUser passCurrentUser() {
        // passing the already logged in user
        return firebaseAuth.getCurrentUser();
    }

    @Override
    public GoogleApiClient passGoogleApiClient() {
        // passing google client object for sign in intent
        return googleApiClient;
    }
    @Override
    public void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                userChangeListener.onUserChanged(null);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        authMessageListener.onMessageReceived(context.getString(R.string.google_play_error));
    }
}
