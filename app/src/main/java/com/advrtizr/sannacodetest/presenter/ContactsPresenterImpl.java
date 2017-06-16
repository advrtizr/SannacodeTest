package com.advrtizr.sannacodetest.presenter;

import android.content.Context;

import com.advrtizr.sannacodetest.model.Contact;
import com.advrtizr.sannacodetest.model.ContactBook;
import com.advrtizr.sannacodetest.model.ContactsModel;
import com.advrtizr.sannacodetest.model.ContactsModelImpl;
import com.advrtizr.sannacodetest.view.ContactsView;

public class ContactsPresenterImpl implements ContactsPresenter {

    private ContactsView view;
    private ContactsModel model;

    public ContactsPresenterImpl(ContactsView view) {
        this.view = view;
        // creating a model object
        model = new ContactsModelImpl((Context) view);
    }
    // method adds a new contact to database
    @Override
    public void addNewContact(Contact contact, String userId) {
        // checking if passed contact and user id not null
        if (contact != null && !userId.equals("")) {
            // passing them to model
            model.addContactToDB(contact, userId);
            showContacts(userId, null);
        }
    }
    // method shows all contacts that in database
    @Override
    public void showContacts(String userId, String sort) {
        // passing user id and sorting parameter to model
        ContactBook contactBook = model.readContactsFromDB(userId, sort);
        // passing accepted contacts from model to view (Activity)
        view.displayContactsBook(contactBook);
    }
}
