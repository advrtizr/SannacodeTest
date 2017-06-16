package com.advrtizr.sannacodetest.presenter;

import com.advrtizr.sannacodetest.model.Contact;

public interface ContactsPresenter {
    void addNewContact(Contact contact, String userId);
    void showContacts(String userId, String sort);
}
