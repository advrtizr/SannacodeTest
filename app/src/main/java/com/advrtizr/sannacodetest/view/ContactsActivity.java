package com.advrtizr.sannacodetest.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.advrtizr.sannacodetest.R;
import com.advrtizr.sannacodetest.model.Contact;
import com.advrtizr.sannacodetest.model.ContactAdapter;
import com.advrtizr.sannacodetest.model.ContactBook;
import com.advrtizr.sannacodetest.model.ContactsDBHelper;
import com.advrtizr.sannacodetest.model.ImageLoadTask;
import com.advrtizr.sannacodetest.listeners.ImageStatusListener;
import com.advrtizr.sannacodetest.presenter.ContactsPresenter;
import com.advrtizr.sannacodetest.presenter.ContactsPresenterImpl;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsActivity extends AppCompatActivity implements ContactsView, ImageStatusListener {

    @BindView(R.id.rw_contactsList) RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private ContactsPresenter presenter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        // getting firebase instance
        firebaseAuth = FirebaseAuth.getInstance();
        // toolbar initialization
        initializeToolbar();
        // user id initialization
        initializeUserId();
        // creating presenter
        presenter = new ContactsPresenterImpl(this);
        // setting FAB
        setFloatingActionButton();
        // retrieving user data from firebase
        getProfileInfo();
        // requesting contacts from model via presenter
        presenter.showContacts(userId, null);
    }

    public void initializeToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        getSupportActionBar().setTitle("");
    }

    private void initializeUserId(){
        // check on null
        // if passed retrieving user id
        if(firebaseAuth.getCurrentUser() != null){
            userId = firebaseAuth.getCurrentUser().getUid();
        }
    }

    private void initializeRecycler(ContactBook contactBook) {
        ContactAdapter adapter = new ContactAdapter(this, contactBook);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }


    private void getProfileInfo(){
        TextView profileTitle = (TextView) findViewById(R.id.tv_profile_title);
        // creating load task for icon image
        ImageLoadTask loadTask = new ImageLoadTask(this);
        // check on null
        // if passed retrieving user photo url and title
        if(firebaseAuth.getCurrentUser() != null){
            String imagePath = String.valueOf(firebaseAuth.getCurrentUser().getPhotoUrl());
            loadTask.execute(imagePath);
            String title = firebaseAuth.getCurrentUser().getDisplayName();
            profileTitle.setText(title);
        }
    }

    // creating dialog window to accept entries for new contact
    private void runEntryDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
        // inflating a view
        View view = LayoutInflater.from(this).inflate(R.layout.entry_dialog, null);
        // initializing views
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
                // checking is any field is empty
                if(!etName.getText().toString().isEmpty()
                        && !etLastName.getText().toString().isEmpty()
                        && !etPhone.getText().toString().isEmpty()
                        && !etEmail.getText().toString().isEmpty()){
                    // creating a contact with entered information
                    Contact contact = new Contact();
                    contact.setName(etName.getText().toString());
                    contact.setLastName(etLastName.getText().toString());
                    contact.setPhoneNumber(etPhone.getText().toString());
                    contact.setEmail(etEmail.getText().toString());
                    // passing a contact via presenter
                    presenter.addNewContact(contact, userId);
                    dialog.hide();
                }else{
                    Toast.makeText(ContactsActivity.this, R.string.entry_form_warning, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // run dialog to accept new entries
                runEntryDialog();
            }
        });
    }
    // interface method  implemented by this activity
    @Override
    public void displayContactsBook(ContactBook book) {
        if (book.getContactList() != null) {
            // initializing recycler and passing contacts
            initializeRecycler(book);
        }
    }
    // interface method implemented by this activity
    @Override
    public void onImageDownloaded(Bitmap bitmap) {
        // initializing circle image view to display icon from load task
        CircleImageView profileImage = (CircleImageView) findViewById(R.id.iv_profile_image);
        profileImage.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // if construction for database sorting
        if (id == R.id.menu_sort_by_name) {
            presenter.showContacts(userId, ContactsDBHelper.KEY_NAME);
            return true;
        }else if(id == R.id.menu_sort_by_last_name){
            presenter.showContacts(userId, ContactsDBHelper.KEY_LASTNAME);
        }else if(id == R.id.menu_sort_by_phone){
            presenter.showContacts(userId, ContactsDBHelper.KEY_PHONE);
        }else if(id == R.id.menu_sort_by_email){
            presenter.showContacts(userId, ContactsDBHelper.KEY_EMAIL);
        }
        return super.onOptionsItemSelected(item);
    }
}
