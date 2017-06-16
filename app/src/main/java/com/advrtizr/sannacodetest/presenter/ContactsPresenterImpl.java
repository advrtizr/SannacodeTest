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
        model = new ContactsModelImpl((Context) view);
    }

    @Override
    public void addNewContact(Contact contact, String userId) {
        if(contact != null){
        model.addContactToDB(contact, userId);
        showContacts(userId);
        }
    }

    @Override
    public void showContacts(String userId) {
        ContactBook contactBook = model.readContactsFromDB(userId);
        view.displayContactsBook(contactBook);
    }
}
