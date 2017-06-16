package com.advrtizr.sannacodetest.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.advrtizr.sannacodetest.Constants;
import com.advrtizr.sannacodetest.R;
import com.advrtizr.sannacodetest.model.ImageLoadTask;
import com.advrtizr.sannacodetest.listeners.ImageStatusListener;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, ImageStatusListener {

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private TextView userName;
    private TextView signInAsk;
    private CircleImageView userImage;
    private Button signInBtn;
    private Button signOutBtn;
    private Button goToContactsBtn;
    private LinearLayout userContainer;
    private LinearLayout signInContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // views
        userName = (TextView) findViewById(R.id.tv_user);
        signInAsk = (TextView) findViewById(R.id.tv_main_screen_ask);
        userImage = (CircleImageView) findViewById(R.id.civ_user_image);
        signInBtn = (Button) findViewById(R.id.btn_sign_in);
        signOutBtn = (Button) findViewById(R.id.btn_sign_out);
        goToContactsBtn = (Button) findViewById(R.id.btn_start);
        userContainer = (LinearLayout) findViewById(R.id.user_container);
        signInContainer = (LinearLayout) findViewById(R.id.sign_in_container);

        // setting default visibility state
        userContainer.setVisibility(View.INVISIBLE);
        signInContainer.setVisibility(View.GONE);

        // button listeners
        signInBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
        goToContactsBtn.setOnClickListener(this);

        // initializing client
        initializeClient();

        // initializing auth
        mAuth = FirebaseAuth.getInstance();

    }

    private void initializeClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, configureGSO())
                .build();
    }

    private GoogleSignInOptions configureGSO() {
        // [START config_sign in]
        // Configure Google Sign In
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_sign in]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START sign in]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    // [END sign in]

    // [START sign out ]
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }
    // [END sign out]

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        // making views visible or not, depending on user state
        if (user != null) {
            userName.setText(user.getDisplayName());
            ImageLoadTask imageLoadTask = new ImageLoadTask(this);
            String imageURL = String.valueOf(user.getPhotoUrl());
            imageLoadTask.execute(imageURL);

            signInAsk.setVisibility(View.GONE);
            signInBtn.setVisibility(View.GONE);
            signInContainer.setVisibility(View.VISIBLE);
            userContainer.setVisibility(View.VISIBLE);
        } else {
            signInBtn.setVisibility(View.VISIBLE);
            signInContainer.setVisibility(View.GONE);
            userContainer.setVisibility(View.GONE);
            signInAsk.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_sign_in) {
            signIn();
        } else if (id == R.id.btn_sign_out) {
            signOut();
        } else if (id == R.id.btn_start) {
            startActivity(new Intent(LoginActivity.this, ContactsActivity.class));
        }
    }

    @Override
    public void onImageDownloaded(Bitmap bitmap) {
        userImage.setImageBitmap(bitmap);
    }
}
