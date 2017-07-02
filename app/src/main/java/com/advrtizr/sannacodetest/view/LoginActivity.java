package com.advrtizr.sannacodetest.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.advrtizr.sannacodetest.Constants;
import com.advrtizr.sannacodetest.R;
import com.advrtizr.sannacodetest.listeners.ImageStatusListener;
import com.advrtizr.sannacodetest.model.ImageLoadTask;
import com.advrtizr.sannacodetest.presenter.LoginPresenter;
import com.advrtizr.sannacodetest.presenter.LoginPresenterImpl;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends BaseActivity implements View.OnClickListener, ImageStatusListener, LoginView {

    @BindView(R.id.tv_user) TextView userName;
    @BindView(R.id.tv_main_screen_ask) TextView signInAsk;
    @BindView(R.id.civ_user_image) CircleImageView userImage;
    @BindView(R.id.btn_sign_in) Button signInBtn;
    @BindView(R.id.btn_sign_out) Button signOutBtn;
    @BindView(R.id.btn_start) Button goToContactsBtn;
    @BindView(R.id.user_container) LinearLayout userContainer;
    @BindView(R.id.sign_in_container) LinearLayout signInContainer;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // setting default visibility state
        userContainer.setVisibility(View.INVISIBLE);
        signInContainer.setVisibility(View.GONE);

        // button listeners
        signInBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
        goToContactsBtn.setOnClickListener(this);

        // initializing presenter
        presenter = new LoginPresenterImpl(this);
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // сheck if user is signed in (non-null) and update UI accordingly
        presenter.requestCurrentUser();
    }
    // [END on_start_check_user]

    private void signIn() {
        // retrieving google client object for intent
        GoogleApiClient googleApiClient = presenter.requestGoogleApiClient();
        // start sign in
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        presenter.passIntentResult(requestCode, data);
    }

    @Override
    public void updateUI(FirebaseUser user) {
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
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_sign_in) {
            signIn();
        } else if (id == R.id.btn_sign_out) {
            presenter.beginSignOut();
        } else if (id == R.id.btn_start) {
            startActivity(new Intent(LoginActivity.this, ContactsActivity.class));
        }
    }

    @Override
    public void onImageDownloaded(Bitmap bitmap) {
        // callback from image load task
        userImage.setImageBitmap(bitmap);
    }
}
