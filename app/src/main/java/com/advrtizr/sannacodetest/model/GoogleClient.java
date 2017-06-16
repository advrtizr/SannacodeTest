package com.advrtizr.sannacodetest.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.advrtizr.sannacodetest.R;
import com.advrtizr.sannacodetest.view.LoginActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

public class GoogleClient {

    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    public GoogleClient(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    private GoogleSignInOptions googleSignInOptions(){
        // Configure Google Sign In
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

    }

    public GoogleApiClient initializeGoogleClient(){
        return new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        Toast.makeText(LoginActivity.this, "connection error", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions()).build();
    }


    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, final AuthenticationStatusListener statusListener) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            statusListener.onSuccess(mAuth);
                        } else {
                            // If sign in fails, display a message to the user.
                            statusListener.onFailure();
                        }
                    }
                });
    }

    private void googleSignOut() {
        Log.i("google", "google api is connected" + mGoogleApiClient.isConnected());
        // Google sign out
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            Log.i("google", "google log out");
        }
    }


}






