package com.advrtizr.sannacodetest.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.advrtizr.sannacodetest.R;
import com.advrtizr.sannacodetest.model.Contact;
import com.advrtizr.sannacodetest.model.ContactAdapter;
import com.advrtizr.sannacodetest.model.ContactBook;
import com.advrtizr.sannacodetest.model.ImageLoadTask;
import com.advrtizr.sannacodetest.model.ImageStatusListener;
import com.advrtizr.sannacodetest.presenter.ContactsPresenter;
import com.advrtizr.sannacodetest.presenter.ContactsPresenterImpl;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsActivity extends AppCompatActivity implements ContactsView, ImageStatusListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ContactsPresenter presenter;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private String userId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initializeToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.rw_contactsList);
        firebaseAuth = FirebaseAuth.getInstance();
        initializeUserId();
        presenter = new ContactsPresenterImpl(this);
        setFirebaseAuthStateListener();
        setFloatingActionButton();
        getProfileInfo();
        presenter.showContacts(userId);

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void initializeToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void initializeUserId(){
        if(firebaseAuth.getCurrentUser() != null){
            userId = firebaseAuth.getCurrentUser().getUid();
        }
    }

    private void initializeRecycler(ContactBook contactBook) {
        adapter = new ContactAdapter(this, contactBook);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }


    private void getProfileInfo(){
        TextView profileTitle = (TextView) findViewById(R.id.tv_profile_title);
        ImageLoadTask loadTask = new ImageLoadTask(this);
        if(firebaseAuth.getCurrentUser() != null){
            String imagePath = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
            loadTask.execute(imagePath);
            String title = firebaseAuth.getCurrentUser().getDisplayName();
            profileTitle.setText(title);
        }
    }

    private void runEntryDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.entry_dialog, null);
        final EditText etName = (EditText) view.findViewById(R.id.et_name);
        final EditText etLastName = (EditText) view.findViewById(R.id.et_lastName);
        final EditText etPhone = (EditText) view.findViewById(R.id.et_phone);
        final EditText etEmail = (EditText) view.findViewById(R.id.et_email);
        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        dialogBuilder.setView(view);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etName.getText().toString().isEmpty()
                        && !etLastName.getText().toString().isEmpty()
                        && !etPhone.getText().toString().isEmpty()
                        && !etEmail.getText().toString().isEmpty()){
                    Contact contact = new Contact();
                    contact.setName(etName.getText().toString());
                    contact.setLastName(etLastName.getText().toString());
                    contact.setPhoneNumber(etPhone.getText().toString());
                    contact.setEmail(etEmail.getText().toString());
                    presenter.addNewContact(contact, userId);
                    dialog.hide();
                }else{
                    Toast.makeText(ContactsActivity.this, "Please fill all required fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runEntryDialog();
            }
        });
    }

    private void setFirebaseAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ContactsActivity.this, LoginActivity.class));
                }
            }
        };
    }


    @Override
    public void displayContactsBook(ContactBook book) {
        if (book.getContact() != null) {
            Log.i("book", "not null");
            initializeRecycler(book);
        }
    }

    @Override
    public void onImageDownloaded(Bitmap bitmap) {
        CircleImageView profileImage = (CircleImageView) findViewById(R.id.iv_profile_image);
        profileImage.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_log_out) {
            firebaseAuth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
