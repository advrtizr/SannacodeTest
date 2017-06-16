package com.advrtizr.sannacodetest.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.advrtizr.sannacodetest.Constants;
import com.advrtizr.sannacodetest.R;
import com.advrtizr.sannacodetest.model.AuthenticationStatusListener;
import com.advrtizr.sannacodetest.presenter.LoginPresenter;
import com.advrtizr.sannacodetest.presenter.LoginPresenterImpl;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity implements LoginView, AuthenticationStatusListener, FirebaseAuth.AuthStateListener {

    private SignInButton signInButton;
    private LoginPresenter presenter;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenterImpl(this);
        firebaseAuth = FirebaseAuth.getInstance();

        signInButton = (SignInButton) findViewById(R.id.btnSignIn);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.signIn();
            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
                presenter.authenticate(account, this);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void sendSignIntent(GoogleApiClient mGoogleApiClient) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    @Override
    public void onSuccess(FirebaseAuth firebaseAuth) {
        hideProgressDialog();
        if (firebaseAuth.getCurrentUser() != null) {
            Toast.makeText(LoginActivity.this, "Authentication successful.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, ContactsActivity.class));
        }
    }

    @Override
    public void onFailure() {
        Toast.makeText(LoginActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            googleSignOut();
        }
    }
}
